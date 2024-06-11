package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final String ENTRY_NAME = "testEntry";
    private static final int ENTRY_SIZE = 10;
    private static final int HEADER_SIZE = 40;

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;
    private InputStream inputStream;
    private Map<String, ArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Tests that a new entry can be added to the archive.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(ENTRY_NAME);
        entry.setSize(ENTRY_SIZE);
        cpioOutputStream.putNextEntry(entry);
        byte[] header = cpioOutputStream.getHdrBuffer();

        // Verify that the header was written correctly
        Assert.assertEquals(HEADER_SIZE + ENTRY_NAME.length() + 1, header.length);
        Assert.assertArrayEquals(CpioConstants.MAGIC_NEW, Arrays.copyOfRange(header, 0, 8));
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry.getInode(), 8, false), Arrays.copyOfRange(header, 8, 16));
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry.getMode(), 8, false), Arrays.copyOfRange(header, 16, 24));
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry.getUID(), 8, false), Arrays.copyOfRange(header, 24, 32));
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry.getGID(), 8, false), Arrays.copyOfRange(header, 32, 40));
        Assert.assertEquals(ENTRY_NAME.length() + 1, CpioUtil.toInt(Arrays.copyOfRange(header, 40, 44)));
    }

    /**
     * Tests that multiple entries can be added to the archive.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutNextEntryMultiple() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry(ENTRY_NAME + "1");
        entry1.setSize(ENTRY_SIZE);
        cpioOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry(ENTRY_NAME + "2");
        entry2.setSize(ENTRY_SIZE);
        cpioOutputStream.putNextEntry(entry2);

        byte[] header = cpioOutputStream.getHdrBuffer();

        // Verify that the headers were written correctly
        int headerLength = HEADER_SIZE * 2 + (ENTRY_NAME.length() + 1) * 2;
        Assert.assertEquals(headerLength, header.length);

        // Verify the first entry
        Assert.assertArrayEquals(CpioConstants.MAGIC_NEW, Arrays.copyOfRange(header, 0, 8));
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry1.getInode(), 8, false), Arrays.copyOfRange(header, 8, 16));
        // ...

        // Verify the second entry
        int pos = 40;
        Assert.assertEquals(ENTRY_NAME.length() + 1, CpioUtil.toInt(Arrays.copyOfRange(header, pos, pos + 4)));
        pos += 4;
        Assert.assertArrayEquals(CpioUtil.long2byteArray(entry2.getInode(), 8, false), Arrays.copyOfRange(header, pos, pos + 16));
        // ...
    }

    /**
     * Tests that writing data to an entry works.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testWriteData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(ENTRY_NAME);
        entry.setSize(ENTRY_SIZE);
        cpioOutputStream.putNextEntry(entry);

        byte[] data = new byte[ENTRY_SIZE];
        for (int i = 0; i < ENTRY_SIZE; i++) {
            data[i] = (byte) i;
        }
        cpioOutputStream.write(data, 0, ENTRY_SIZE);

        byte[] header = cpioOutputStream.getHdrBuffer();
        byte[] dataFromArchive = new byte[ENTRY_SIZE];
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        int read = inputStream.read(dataFromArchive, 0, ENTRY_SIZE);

        // Verify that the data was written and read back correctly
        Assert.assertEquals(ENTRY_SIZE, read);
        Assert.assertArrayEquals(data, dataFromArchive);
    }

    /**
     * Tests that the trailer is written when finish() is called.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();

        byte[] header = cpioOutputStream.getHdrBuffer();
        byte[] trailer = Arrays.copyOfRange(header, header.length - 40, header.length);

        // Verify that the trailer was written correctly
        Assert.assertEquals(CpioConstants.TRAILER_MAGIC, Arrays.copyOfRange(trailer, 0, 8));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 8, 12)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 12, 16)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 16, 20)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 20, 24)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 24, 28)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 28, 32)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 32, 36)));
        Assert.assertEquals(0, CpioUtil.toInt(Arrays.copyOfRange(trailer, 36, 40)));
    }

    /**
     * Tests that an exception is thrown when trying to write data after finish()
     * has been called.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testWriteDataAfterFinish() throws IOException {
        cpioOutputStream.finish();
        byte[] data = new byte[1];
        cpioOutputStream.write(data, 0, data.length);
    }

    /**
     * Tests that an exception is thrown when trying to write to the archive
     * after close() has been called.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testWriteDataAfterClose() throws IOException {
        cpioOutputStream.close();
        byte[] data = new byte[1];
        cpioOutputStream.write(data, 0, data.length);
    }

    /**
     * Tests that an exception is thrown when trying to add a new entry after
     * finish() has been called.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryAfterFinish() throws IOException {
        cpioOutputStream.finish();
        CpioArchiveEntry entry = new CpioArchiveEntry(ENTRY_NAME);
        cpioOutputStream.putNextEntry(entry);
    }

    /**
     * Tests that an exception is thrown when trying to add a new entry after
     * close() has been called.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryAfterClose() throws IOException {
        cpioOutputStream.close();
        CpioArchiveEntry entry = new CpioArchiveEntry(ENTRY_NAME);
        cpioOutputStream.putNextEntry(entry);
    }

    /**
     * Tests that an exception is thrown when trying to add an entry with the
     * same name as an existing entry.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicate() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry(ENTRY_NAME);
        cpioOutputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry(ENTRY_NAME);
        cpioOutputStream.putNextEntry(entry2);
    }
}