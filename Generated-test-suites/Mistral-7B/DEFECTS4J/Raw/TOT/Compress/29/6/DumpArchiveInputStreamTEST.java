package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test class for DumpArchiveInputStream.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "testdata.dump";
    private static final String TEST_DATA_COMPRESSED = "testdata.dump.gz";
    private static final String TEST_DATA_SPARSE = "testdata.dump.sparse";
    private static final String TEST_DATA_INVALID = "testdata.invalid.dump";

    private ArchiveInputStream input;
    private DumpArchiveEntry entry;
    private byte[] buffer;

    @Before
    public void setUp() throws Exception {
        File testFile = new File(TEST_DATA);
        if (testFile.exists()) {
            testFile.delete();
        }

        File compressedTestFile = new File(TEST_DATA_COMPRESSED);
        if (compressedTestFile.exists()) {
            compressedTestFile.delete();
        }

        File sparseTestFile = new File(TEST_DATA_SPARSE);
        if (sparseTestFile.exists()) {
            sparseTestFile.delete();
        }

        File invalidTestFile = new File(TEST_DATA_INVALID);
        if (invalidTestFile.exists()) {
            invalidTestFile.delete();
        }

        // prepare input data
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DumpArchiveOutputStream out = new DumpArchiveOutputStream(output, "test",
                ZipEncodingHelper.getZipEncoding("UTF-8"), true);

        out.putNextEntry(new DumpArchiveEntry("test.txt", "test/test.txt", 0, 10, null));
        out.write(new byte[]{"Hello, World!".getBytes()}, 0, 13);
        out.closeEntry();

        out.putNextEntry(new DumpArchiveEntry("test.bin", "test/test.bin", 0, 16, null));
        out.write(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 0, 16);
        out.closeEntry();

        out.putNextEntry(new DumpArchiveEntry("test.dir/test.txt", "test/test.dir/test.txt", 0, 10, null));
        out.closeEntry();

        out.finishStream();
        byte[] data = output.toByteArray();

        // write data to files
        try (RandomAccessFile file = new RandomAccessFile(TEST_DATA, "rw");) {
            file.write(data);
        }

        try (RandomAccessFile compressedFile = new RandomAccessFile(TEST_DATA_COMPRESSED, "rw");) {
            compressedFile.writeInt(DumpArchiveConstants.MAGIC);
            compressedFile.writeInt(DumpArchiveConstants.TP_SIZE - 8);
            compressedFile.writeByte(DumpArchiveConstants.SEGMENT_TYPE.COMPRESSED);
            compressedFile.writeBytes("0".getBytes()); // version
            compressedFile.writeInt(data.length);
            compressedFile.writeInt(data.length + 8);
            IOUtils.writeToStream(new GZIPOutputStream(compressedFile), data);
        }

        try (RandomAccessFile sparseFile = new RandomAccessFile(TEST_DATA_SPARSE, "rw");) {
            sparseFile.write(data);
            sparseFile.seek(14);
            sparseFile.writeByte(0);
            sparseFile.writeBytes("0".getBytes()); // version
            sparseFile.writeInt(data.length + 8);
        }

        try (RandomAccessFile invalidFile = new RandomAccessFile(TEST_DATA_INVALID, "rw");) {
            invalidFile.writeInt(0x12345678);
            invalidFile.writeBytes(data);
        }

        // prepare test input
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA), null);
        buffer = new byte[13];
    }

    @After
    public void tearDown() throws Exception {
        if (input != null) {
            input.close();
        }

        File testFile = new File(TEST_DATA);
        if (testFile.exists()) {
            testFile.delete();
        }

        File compressedTestFile = new File(TEST_DATA_COMPRESSED);
        if (compressedTestFile.exists()) {
            compressedTestFile.delete();
        }

        File sparseTestFile = new File(TEST_DATA_SPARSE);
        if (sparseTestFile.exists()) {
            sparseTestFile.delete();
        }

        File invalidTestFile = new File(TEST_DATA_INVALID);
        if (invalidTestFile.exists()) {
            invalidTestFile.delete();
        }
    }

    @Test
    public void testNextEntry() throws IOException, ArchiveException {
        // test next entry
        entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10, entry.getSize());

        entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.bin", entry.getName());
        Assert.assertEquals(16, entry.getSize());

        entry = input.getNextEntry();
        Assert.assertNull(entry);

        // test next entry with sparse file
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA_SPARSE), null);
        entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10, entry.getSize());

        entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.bin", entry.getName());
        Assert.assertEquals(16, entry.getSize());

        entry = input.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException, ArchiveException {
        // test read
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(13, bytesRead);
        Assert.assertArrayEquals("Hello, World!".getBytes(), buffer);

        bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, bytesRead);

        // test read with sparse file
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA_SPARSE), null);
        bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(13, bytesRead);
        Assert.assertArrayEquals("Hello, World!".getBytes(), buffer);

        bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, bytesRead);
    }

    @Test(expected = EOFException.class)
    public void testReadEndOfStream() throws IOException, ArchiveException {
        // test read end of stream
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, bytesRead);

        entry = input.getNextEntry();
        Assert.assertNull(entry);

        bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadInvalid() throws IOException, ArchiveException {
        // test read invalid
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA_INVALID), null);
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadCompressed() throws IOException, ArchiveException {
        // test read compressed
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA_COMPRESSED), null);
        int bytesRead = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        // test close
        input.close();
        entry = input.getNextEntry();
        Assert.assertNull(entry);

        // test close with sparse file
        input = new DumpArchiveInputStream(new FileInputStream(TEST_DATA_SPARSE), null);
        input.close();
        entry = input.getNextEntry();
        Assert.assertNull(entry);
    }
}