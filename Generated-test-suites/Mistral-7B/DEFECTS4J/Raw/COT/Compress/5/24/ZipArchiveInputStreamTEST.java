import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.apache.commons.compress.utils.ZipUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipArchiveEntry zipArchiveEntry;
    private byte[] testFileSignature = new byte[]{0x04, 0x03, 0x4b, 0x50};
    private byte[] centralDirectorySignature = new byte[]{0x02, 0x01, 0x4b, 0x50, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a};

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(testFileSignature);
        bytes.write(new byte[]{2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[]{3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[]{4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[]{2, (byte) 0xC3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[]{4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[]{4, 0, 0, 0});
        bytes.write(new byte[]{2, 0, 0, 0});
        bytes.write(new byte[]{2, (byte) 0xA0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        bytes.write(new byte[0]);
        inputStream = new ByteArrayInputStream(bytes.toByteArray());
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
    }

    @After
    public void tearDown() throws Exception {
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry_ReturnsNullWhenEndOfCentralDirectoryIsReached() throws Exception {
        // Given

        // When
        while (zipArchiveEntry != null) {
            zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        }

        // Then
        Assert.assertNull(zipArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_ThrowsIOExceptionWhenEndOfStreamIsReached() throws Exception {
        // Given
        zipArchiveInputStream.close();

        // When
        zipArchiveInputStream.getNextZipEntry();

        // Then
    }

    @Test
    public void testGetNextZipEntry_ReturnsValidEntry() throws Exception {
        // Given

        // When
        ZipArchiveEntry returnedEntry = zipArchiveInputStream.getNextZipEntry();

        // Then
        Assert.assertNotNull(returnedEntry);
        Assert.assertEquals("TestEntry.class", returnedEntry.getClass().getSimpleName());
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_ThrowsIOExceptionWhenInvalidLocalFileHeaderSignatureIsDetected() throws Exception {
        // Given
        byte[] signature = new byte[4];
        signature[0] = 0x0C;
        signature[1] = 0x0F;
        signature[2] = 0x4B;
        signature[3] = 0x50;
        InputStream invalidInput = new ByteArrayInputStream(signature);
        ZipArchiveInputStream invalidZipArchiveInputStream = new ZipArchiveInputStream(invalidInput, ZipEncoding.UTF8, true);

        // When
        invalidZipArchiveInputStream.getNextZipEntry();

        // Then
        Assert.fail("Expected IOException but did not receive one.");
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_ThrowsIOExceptionWhenInvalidCentralDirectorySignatureIsDetected() throws Exception {
        // Given
        byte[] signature = new byte[10];
        signature[0] = 0x02;
        signature[1] = 0x01;
        signature[2] = 0x4B;
        signature[3] = 0x50;
        signature[4] = 0x05;
        signature[5] = 0x06;
        signature[6] = 0x07;
        signature[7] = 0x08;
        signature[8] = 0x09;
        signature[9] = 0x0B;
        InputStream invalidInput = new ByteArrayInputStream(signature);
        ZipArchiveInputStream invalidZipArchiveInputStream = new ZipArchiveInputStream(invalidInput, ZipEncoding.UTF8, true);

        // When
        invalidZipArchiveInputStream.getNextZipEntry();

        // Then
        Assert.fail("Expected IOException but did not receive one.");
    }

    @Test
    public void testRead_ReturnsValidDataWhenReadingStoredData() throws Exception {
        // Given

        // When
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);

        // Then
        Assert.assertNotNull(bytesRead);
        Assert.assertEquals(bytesRead, zipArchiveEntry.getSize());
    }

    @Test
    public void testRead_ReturnsValidDataWhenReadingInflatedData() throws Exception {
        // Given
        byte[] testData = "Test Data".getBytes();
        byte[] testDataCompressed = ZipUtil.compress(testData, ZipEncoding.UTF8);
        byte[] buffer = new byte[testData.length];

        // When
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.setReadMode(ZipArchiveInputStream.DEFLATED);
        int bytesRead = zipArchiveInputStream.read(buffer, 0, testData.length);

        // Then
        Assert.assertNotNull(bytesRead);
        Assert.assertEquals(bytesRead, testData.length);
        Assert.assertArrayEquals(testData, buffer);
    }

    @Test(expected = IOException.class)
    public void testRead_ThrowsIOExceptionWhenEndOfStreamIsReached() throws Exception {
        // Given
        byte[] buffer = new byte[10];

        // When
        zipArchiveInputStream.close();
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);

        // Then
        Assert.fail("Expected IOException but did not receive one.");
    }

    @Test(expected = EOFException.class)
    public void testRead_ThrowsEOFExceptionWhenEndOfEntryIsReached() throws Exception {
        // Given
        byte[] buffer = new byte[10];

        // When
        zipArchiveInputStream.read(buffer, 0, buffer.length);

        // Then
        Assert.fail("Expected EOFException but did not receive one.");
    }

    @Test
    public void testSkip_SkipsValidNumberOfBytes() throws Exception {
        // Given

        // When
        long bytesToSkip = zipArchiveEntry.getSize() + 100;
        long skippedBytes = zipArchiveInputStream.skip(bytesToSkip);

        // Then
        Assert.assertEquals(bytesToSkip, skippedBytes);
    }

    @Test(expected = IOException.class)
    public void testClose_ThrowsIOExceptionWhenInputStreamIsClosed() throws Exception {
        // Given
        zipArchiveInputStream.close();

        // When
        zipArchiveInputStream.close();

        // Then
        Assert.fail("Expected IOException but did not receive one.");
    }

    @Test
    public void testMatches_ReturnsTrueWhenSignatureMatchesLocalFileHeaderSignature() {
        // Given

        // When
        boolean result = ZipArchiveInputStream.matches(testFileSignature, testFileSignature.length);

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_ReturnsTrueWhenSignatureMatchesEndOfCentralDirectorySignature() {
        // Given

        // When
        boolean result = ZipArchiveInputStream.matches(centralDirectorySignature, centralDirectorySignature.length);

        // Then
        Assert.assertTrue(result);
    }

}