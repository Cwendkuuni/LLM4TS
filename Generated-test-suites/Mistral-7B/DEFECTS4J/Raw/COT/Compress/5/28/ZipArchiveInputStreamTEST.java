import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.util.ZipLong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private byte[] signature = {0x04, 0x03, 0x4b, 0x50, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d};

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream.write(signature, 0, signature.length);
        zipArchiveInputStream = new ZipArchiveInputStream(new PushbackInputStream(new ByteArrayInputStream(outputStream.toByteArray()), 10));
        zipArchiveInputStream.setUseUnicodeExtraFields(true);
        zipArchiveInputStream.setZipEncoding(ZipEncoding.UTF8);
    }

    @Test
    public void testConstructor() throws IOException {
        Assert.assertNotNull(zipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullInputStream() {
        new ZipArchiveInputStream(null);
    }

    @Test
    public void testGetNextZipEntryNullEntryWhenEndOfCentralDirectoryIsReached() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntryValidEntry() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("name", zipArchiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenStreamIsClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextEntryNullEntryWhenEndOfCentralDirectoryIsReached() throws IOException {
        zipArchiveInputStream.getNextEntry();
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryValidEntry() throws IOException {
        zipArchiveInputStream.getNextEntry();
        ArchiveEntry archiveEntry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("name", archiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWhenStreamIsClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
        int len = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(len, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadWhenStreamIsClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[1], 0, 1);
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 15, buffer.length);
    }

    @Test
    public void testReadWithLengthGreaterThanAvailableBytes() throws IOException {
        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length + 1);
        Assert.assertEquals(length, buffer.length);
    }

    @Test
    public void testReadWithNoBytesAvailable() throws IOException {
        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, 0);
    }

    @Test(expected = EOFException.class)
    public void testReadEndOfStream() throws IOException {
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 0, buffer.length);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.read(buffer, 0, buffer.length);
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        long value = 10;
        zipArchiveInputStream.skip(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeValue() throws IOException {
        zipArchiveInputStream.skip(-1);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] invalidSignature = {0x04, 0x03, 0x4b, 0x51, 0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d};
        Assert.assertFalse(ZipArchiveInputStream.matches(invalidSignature, invalidSignature.length));
    }
}