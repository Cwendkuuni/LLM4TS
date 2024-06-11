import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream testStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        String encoding = ZipEncoding.UTF8.getName();
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(getTestData());
        testStream = new ZipArchiveInputStream(testInput, encoding, true);
    }

    @After
    public void tearDown() throws IOException {
        testStream.close();
        outputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = testStream.getNextZipEntry();
        assertNull(entry);
        entry = testStream.getNextZipEntry();
        assertEquals("TestEntry.jar!/TestEntry.class", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_ThrowsIOException_WhenFileEndReached()
            throws IOException {
        testStream.getNextZipEntry();
        testStream.getNextZipEntry();
        testStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int read = testStream.read(buffer, 0, buffer.length);
        assertEquals(read, 2);
        assertEquals(buffer[0], (byte) 0x50);
        assertEquals(buffer[1], (byte) 0x4b);
        assertEquals(buffer[2], (byte) 0x03);
        assertEquals(buffer[3], (byte) 0x4b);
        assertEquals(buffer[4], (byte) 0x50);

        read = testStream.read(buffer, 0, buffer.length);
        assertEquals(read, 1);
        assertEquals(buffer[0], (byte) 0x04);

        testStream.close();

        read = testStream.read(buffer, 0, buffer.length);
        assertEquals(read, -1);
    }

    @Test(expected = IOException.class)
    public void testRead_ThrowsIOException_WhenClosed() throws IOException {
        testStream.close();
        testStream.read(new byte[1], 0, 1);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = testStream.skip(5);
        assertEquals(skipped, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_ThrowsIllegalArgumentException_WhenNegativeValue()
            throws IOException {
        testStream.skip(-1);
    }

    @Test(expected = ZipException.class)
    public void testSkip_ThrowsZipException_WhenEndOfCentralDirectoryReached()
            throws IOException {
        testStream.getNextZipEntry();
        testStream.getNextZipEntry();
        testStream.getNextZipEntry();
        testStream.skip(Long.MAX_VALUE);
    }

    @Test
    public void testCloseEntry() throws IOException {
        ZipArchiveEntry entry = testStream.getNextZipEntry();
        testStream.closeEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseEntry_ThrowsIOException_WhenClosed() throws IOException {
        testStream.closeEntry();
    }

    @Test
    private byte[] getTestData() throws IOException {
        byte[] signature = new byte[]{0x04, 0x03, 0x4b, 0x50, 0x04, 0x03, 0x4b, 0x50};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(signature, 0, signature.length);
        output.write(new byte[]{0x00, 0x02, (byte) 0xed, 0x00}); // version made by
        output.write(new byte[]{0x02, 0x00}); // general purpose bit flag
        output.write(new byte[]{0x08, 0x00}); // compression method
        output.write(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}); // last mod file time
        output.write(new byte[]{0x00, 0x00, 0x00, 0x00}); // last mod file date
        output.write(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}); // crc-32
        output.write(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}); // compressed size
        output.write(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}); // uncompressed size
        output.write(new byte[]{0x06, 0x00}); // file name length
        output.write(new byte[]{0x00, 0x08}); // extra field length

        byte[] fileName = "TestEntry.jar!/TestEntry.class".getBytes();
        output.write(fileName, 0, fileName.length);

        byte[] extraData = new byte[0];
        output.write(extraData, 0, extraData.length);

        output.write(new byte[]{0x00, 0x00, 0x00, 0x00}); // file comment length

        output.write(new byte[]{0x00, 0x00, 0x00, 0x00}); // disk number start
        output.write(new byte[]{0x00, 0x00}); // internal file attribute
        output.write(new byte[]{0x00, 0x00}); // external file attribute
        output.write(new byte[]{0x00, 0x00}); // local header size
        output.write(new byte[]{0x00, 0x00}); // data descriptor size

        output.write(new byte[]{0x00, 0x00, 0x00, 0x00}); // CD disk number start
        output.write(new byte[]{0x00, 0x00}); // CD internal file attribute
        output.write(new byte[]{0x00, 0x00}); // CD external file attribute
        output.write(new byte[]{0x00, 0x00}); // CD local header size
        output.write(new byte[]{0x00, 0x00}); // CD data descriptor size

        return output.toByteArray();
    }
}