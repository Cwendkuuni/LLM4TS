import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry_NullInputStream_ReturnsNull() throws IOException {
        ZipEntry zipEntry = zipInputStream.getNextZipEntry();
        Assert.assertNull(zipEntry);
    }

    @Test
    public void testRead_InvalidBuffer_ThrowsArrayIndexOutOfBoundsException() throws IOException {
        byte[] buffer = new byte[5];
        int start = 6;
        int length = 5;

        try {
            zipInputStream.read(buffer, start, length);
            Assert.fail("ArrayIndexOutOfBoundsException expected");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Pass
        }
    }

    @Test
    public void testClose_ValidInputStream_ClosesStream() throws IOException {
        zipInputStream.close();

        try {
            zipInputStream.read();
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testSkip_InvalidValue_ThrowsIllegalArgumentException() throws IOException {
        long value = -1;

        try {
            zipInputStream.skip(value);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testMatches_InvalidSignatureLength_ReturnsFalse() {
        byte[] signature = new byte[3];
        int length = 3;

        boolean result = ZipArchiveInputStream.matches(signature, length);

        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_ValidLFHSignature_ReturnsTrue() {
        byte[] signature = new byte[4];
        int length = 4;
        signature[0] = 0x50;
        signature[1] = 0x4b;
        signature[2] = 0x03;
        signature[3] = 0x04;

        boolean result = ZipArchiveInputStream.matches(signature, length);

        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_ValidEOCDSignature_ReturnsTrue() {
        byte[] signature = new byte[4];
        int length = 4;
        signature[0] = 0x50;
        signature[1] = 0x4b;
        signature[2] = 0x05;
        signature[3] = 0x06;

        boolean result = ZipArchiveInputStream.matches(signature, length);

        Assert.assertTrue(result);
    }

}