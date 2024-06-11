import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithValidLocalFileHeader() throws IOException {
        String expectedName = "test.txt";
        ZipEncoding encoding = ZipEncoding.UTF8;
        byte[] header = createLocalFileHeader(expectedName, ZipEncodingHelper.UTF8, true, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180);
        InputStream inputStream = new ByteArrayInputStream(header);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals(expectedName, zipArchiveEntry.getName());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryWithInvalidLocalFileHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithCentralDirectorySignature() throws IOException {
        String expectedName = "test.txt";
        byte[] centralDirectory = createCentralDirectorySignature();
        InputStream inputStream = new ByteArrayInputStream(centralDirectory);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        byte[] data = new byte[100];
        byte[] input = new byte[50];
        System.arraycopy(data, 0, input, 0, 50);
        InputStream inputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        int length = zipArchiveInputStream.read(data, 0, data.length);
        Assert.assertEquals(-1, length);
    }

    @Test
    public void testReadWithValidData() throws IOException, ZipException, DataFormatException {
        byte[] compressedData = new byte[100];
        byte[] input = new byte[50];
        System.arraycopy(compressedData, 0, input, 0, 50);
        InputStream inputStream = new ByteArrayInputStream(input);
        Inflater inflater = new Inflater(true);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertArrayEquals(new byte[]{compressedData[0], compressedData[1]}, buffer);
        int totalLength = 0;
        while (length > 0) {
            length = zipArchiveInputStream.read(buffer, 0, buffer.length);
            totalLength += length;
        }
        Assert.assertEquals(compressedData.length, totalLength);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidData() throws IOException {
        byte[] input = new byte[50];
        InputStream inputStream = new ByteArrayInputStream(input);
        Inflater inflater = new Inflater(true);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, false);
        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
        zipArchiveInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[100];
        InputStream inputStream = new ByteArrayInputStream(data);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        long length = zipArchiveInputStream.skip(30L);
        Assert.assertEquals(30L, length);
        zipArchiveInputStream.close();
    }

    private byte[] createLocalFileHeader(String name, ZipEncoding encoding,
                                         boolean extraFieldPresent, long crc, long compressedSize, long uncompressedSize,
                                         int time, int date, short versionMadeBy, short versionNeeded, short generalPurposeFlag) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipShort.writeValue(outputStream, ZipArchiveOutputStream.LFH_SIG);
        ZipShort.writeValue(outputStream, versionNeeded);
        outputStream.write(ZipFile.getPlatformBits(encoding));
        outputStream.write(generalPurposeFlag);
        ZipShort.writeValue(outputStream, versionMadeBy);
        ZipShort.writeValue(outputStream, methodToShort(ZipFile.STORED));
        ZipLong.writeValue(outputStream, time);
        ZipLong.writeValue(outputStream, date);
        ZipLong.writeValue(outputStream, crc);
        ZipLong.writeValue(outputStream, compressedSize);
        ZipLong.writeValue(outputStream, uncompressedSize);
        ZipShort.writeValue(outputStream, (short) (name.length() + 6));
        ZipShort.writeValue(outputStream, 0);
        outputStream.write(name.getBytes(encoding.name()));
        if (extraFieldPresent) {
            int extraFieldLength = 6;
            outputStream.write(new byte[extraFieldLength]);
        }
        return outputStream.toByteArray();
    }

    private byte[] createCentralDirectorySignature() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipLong.writeValue(outputStream, ZipLong.CDIS_SIG);
        return outputStream.toByteArray();
    }

    private short methodToShort(int method) {
        switch (method) {
            case ZipFile.STORED:
                return ZipShort.STORED;
            case ZipFile.DEFLATED:
                return ZipShort.DEFLATED;
            case ZipFile.ENCRYPTED:
                return ZipShort.ENCRYPTED;
            case ZipFile.BZIP2:
                return ZipShort.BZIP2;
            default:
                throw new IllegalArgumentException("Unknown compression method: " + method);
        }
    }
}