import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry_emptyInputStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(zipInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_validEntry() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testGetNextZipEntry_noMoreEntries() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        ZipArchiveEntry nextEntry = zipInputStream.getNextZipEntry();
        Assert.assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_closedStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        zipInputStream.close();
        zipInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_nullInputStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(null);
    }

    @Test
    public void testRead_emptyInputStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        byte[] buffer = new byte[10];
        int length = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
    }

    @Test
    public void testRead_validData() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        byte[] buffer = new byte[5];
        int length = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertNotEquals(-1, length);
        byte[] expected = new byte[]{'P', 'K', 'Z', 'I', 'P'};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test(expected = IOException.class)
    public void testRead_closedStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        zipInputStream.close();
        byte[] buffer = new byte[10];
        int length = zipInputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testRead_nullBuffer() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        int length = zipInputStream.read(null, 0, 10);
    }

    @Test(expected = IOException.class)
    public void testRead_negativeLength() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        byte[] buffer = new byte[10];
        int length = zipInputStream.read(buffer, 10, -10);
    }

    @Test
    public void testClose() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(getClass().getResourceAsStream("/test.zip"));
        zipInputStream.close();
        Assert.assertFalse(zipInputStream.isOpen());
    }
}