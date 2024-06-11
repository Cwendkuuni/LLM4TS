import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.apache.commons.compress.utils.ZipShort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() throws IOException {
        byte[] testData = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50,
                (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x0b, (byte) 0x01, (byte) 0x08,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x04,
                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x04, (byte) 0x01, (byte) 0x00,
                (byte) 0x03, (byte) 0x02, (byte) 0x6b, (byte) 0x65, (byte) 0x74, (byte) 0x79,
                (byte) 0x20, (byte) 0x74, (byte) 0x79, (byte) 0x70, (byte) 0x65, (byte) 0x31,
                (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x04,
                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02,
                (byte) 0x00, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
                (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b,
                (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};

        inputStream = new ByteArrayInputStream(testData);
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testConstructor() throws IOException {
        ZipArchiveInputStream testZip = new ZipArchiveInputStream(null);
        Assert.assertNull(testZip);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());

        entry = zipInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenClosed() throws IOException {
        zipInputStream.close();
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenHitCentralDirectory() throws IOException {
        byte[] centralDirectorySignature = new byte[]{0x02, 0x01, 0x4b, 0x50, 0x03, 0x04, 0x14, 0x00};
        inputStream = new ByteArrayInputStream(new byte[]{});
        inputStream = new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4b, 0x50}
                .concat(new byte[]{0x02, 0x01, 0x4b, 0x50}.concat(centralDirectorySignature)));
        zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        zipInputStream.getNextZipEntry();
        zipInputStream.getNextZipEntry();
        Assert.assertNull(zipInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer);
        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01}, buffer);

        bytesRead = zipInputStream.read(buffer);
        Assert.assertEquals(7, bytesRead);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07}, buffer);

        bytesRead = zipInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadWhenClosed() throws IOException {
        zipInputStream.close();
        byte[] buffer = new byte[10];
        zipInputStream.read(buffer);
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseWhenAlreadyClosed() throws IOException {
        zipInputStream.close();
        zipInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testSkipWhenEndOfStreamReached() throws IOException {
        long value = Long.MAX_VALUE;
        zipInputStream.skip(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWhenNegativeValue() throws IOException {
        long value = Long.MIN_VALUE;
        zipInputStream.skip(value);
    }

    @Test
    public void testMatchesSignature() throws IOException {
        Assert.assertTrue(ZipArchiveInputStream.matches(new byte[]{0x04, 0x03, 0x4b, 0x50}, 32));
        Assert.assertTrue(ZipArchiveInputStream.matches(new byte[]{0x02, 0x01, 0x4b, 0x50, 0x02, 0x01, 0x4b, 0x50, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65}, 20));
    }

    @Test(expected = IOException.class)
    public void testReadFullyWhenEndOfStreamReached() throws IOException {
        zipInputStream.readFully(new byte[1]);
        byte[] b = new byte[1];
        zipInputStream.readFully(b);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] b = new byte[10];
        zipInputStream.readFully(b);
        Assert.assertEquals(5, b.length);
        Assert.assertArrayEquals(new byte[]{(byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01}, b);

        b = new byte[7];
        zipInputStream.readFully(b);
        Assert.assertEquals(7, b.length);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07}, b);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWhenClosed() throws IOException {
        zipInputStream.close();
        byte[] b = new byte[1];
        zipInputStream.readFully(b);
    }

    @Test(expected = DataFormatException.class)
    public void testInflateWithInvalidFormat() throws IOException {
        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4b, 0x50, 0x02, 0x01, 0x4b, 0x50, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65}), ZipEncoding.UTF8, true);
        zipInputStream.getNextZipEntry();
        zipInputStream.inflate();
    }

    @Test(expected = ZipException.class)
    public void testInflateWithInvalidSignature() throws IOException {
        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4b, 0x51, 0x02, 0x01, 0x4b, 0x50, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65}), ZipEncoding.UTF8, true);
        zipInputStream.getNextZipEntry();
        zipInputStream.inflate();
    }

    @Test(expected = IOException.class)
    public void testInflateWithEOFException() throws IOException {
        zipInputStream.close();
        zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x4b, 0x50, 0x02, 0x01, 0x4b, 0x50, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65, 0x04, 0x69, 0x6e, 0x64, 0x6f, 0x6e, 0x65, 0x00}), ZipEncoding.UTF8, true);
        zipInputStream.getNextZipEntry();
        zipInputStream.inflate();
    }
}