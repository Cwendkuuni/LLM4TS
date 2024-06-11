import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setup() {
        byte[] input = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @After
    public void teardown() throws IOException {
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testReadClosedStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[0]);
    }

    @Test
    public void testReadEmptyStream() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadInvalidBuffer() throws IOException {
        zipArchiveInputStream.read(new byte[10], -1, 10);
    }

    @Test
    public void testReadAfterFinished() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        Assert.assertTrue(zipArchiveInputStream.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeValue() throws IOException {
        zipArchiveInputStream.skip(-1);
    }

    @Test
    public void testMatchesLFHSignature() {
        byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x04};
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesEOCDSignature() {
        byte[] signature = new byte[]{0x50, 0x4B, 0x05, 0x06};
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x00};
        Assert.assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] buffer = new byte[4];
        zipArchiveInputStream.readFully(data);
        zipArchiveInputStream.read(buffer);
        Assert.assertArrayEquals(data, buffer);
    }

    @Test(expected = EOFException.class)
    public void testReadFullyEOF() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        zipArchiveInputStream.readFully(data);
    }

    // Add your additional test cases for edge cases and exception handling scenarios here

}

