import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstruction() {
        InputStream inputStream = new FileInputStream(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNotNull(zipArchiveInputStream);
    }

    @Test
    public void testGetNextZipEntry_NormalCase() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_ClosedStream() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        zipArchiveInputStream.close();
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntry_NullEntry() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        zipArchiveInputStream.closeEntry();
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_ValidEntry() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        String name = zipArchiveEntry.getName();
        Assert.assertNotNull(name);
        zipArchiveInputStream.closeEntry();
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        zipArchiveInputStream.close();
        byte[] buffer = new byte[1];
        int read = zipArchiveInputStream.read(buffer);
    }

    @Test
    public void testRead_EmptyBuffer() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        byte[] buffer = new byte[1];
        int read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testRead_FilledBuffer() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        byte[] buffer = new byte[1];
        int read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, read);

        zipArchiveInputStream.getNextZipEntry();
        read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{'A'}, buffer);
    }

    @Test
    public void testRead_FilledBuffer_WithData() throws IOException {
        byte[] input = "Hello World!".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(input);
        CRC32 crc = new CRC32();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(
                new FileOutputStream(new File("test.zip")));
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        zipArchiveOutputStream.putNextEntry(entry);
        zipArchiveOutputStream.write(input, 0, input.length);
        zipArchiveOutputStream.closeEntry();
        zipArchiveOutputStream.finish();
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(new File("test.zip")));
        byte[] buffer = new byte[input.length];
        int read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(input.length, read);
        Assert.assertArrayEquals(input, buffer);
        crc.update(input);
        long calculatedCrc = crc.getValue();
        long actualCrc = ZipLong.getValue(zipArchiveInputStream.getNextEntry()
                .getCrc());
        Assert.assertEquals(calculatedCrc, actualCrc);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testRead_InvalidDataDescriptor() throws IOException {
        byte[] invalidDataDescriptor = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1};
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(
                invalidDataDescriptor);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                testInputStream);
        byte[] buffer = new byte[1];
        int read = zipArchiveInputStream.read(buffer);
    }

    @Test
    public void testRead_EOF() throws IOException {
        byte[] input = "Hello World!".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(input);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                testInputStream);
        byte[] buffer = new byte[input.length];
        int read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(input.length, read);
        Assert.assertArrayEquals(input, buffer);
        read = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, read);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_CentralDirectoryReached() throws IOException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                zipFile.getInputStream(zipFile.getEntry("test.txt")));
        ZipArchiveEntry zipArchiveEntry;
        while ((zipArchiveEntry = zipArchiveInputStream.getNextZipEntry()) != null) {
            // Do something with the entry
        }
        zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
    }
}