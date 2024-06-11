import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
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

    private ByteArrayOutputStream outputStream;
    private ZipArchiveOutputStream zipOutputStream;
    private ByteArrayInputStream inputStream;
    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        zipOutputStream = new ZipArchiveOutputStream(outputStream, ZipEncoding.UTF8, true);
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutputStream.write("Hello World!".getBytes());
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (zipInputStream != null) {
            zipInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
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
        zipInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenEndOfCentralDirectoryReached()
            throws IOException {
        ZipArchiveInputStream testFile = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[0]));
        testFile.getNextZipEntry();
        testFile.getNextZipEntry();
        testFile.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        int readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertGreaterEquals(readBytes, 0);
        Assert.assertEquals("Hello World!".getBytes().length, readBytes);
    }

    @Test(expected = IOException.class)
    public void testReadWhenClosed() throws IOException {
        zipInputStream.close();
        zipInputStream.read(new byte[1][0]);
    }

    @Test(expected = EOFException.class)
    public void testReadWhenEndOfStreamReached() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        int readBytes = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, zipInputStream.read(new byte[1][0]));
    }

    @Test(expected = DataFormatException.class)
    public void testReadWhenCorruptedDataIsPresent() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(
                outputStream, ZipEncoding.UTF8, true);
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutputStream.write("Hello World!".getBytes());
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test2.txt"));
        zipOutputStream.writeInt(0xdeadbeef); // corrupt signature
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        outputStream.close();
        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                inputStream);
        zipInputStream.read(new byte[1][0]);
    }

    @Test(expected = ZipException.class)
    public void testReadWhenUnsupportedEncodingIsUsed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(
                outputStream, "ISO-8859-1", true); // unsupported encoding
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutputStream.write("Hello World!".getBytes());
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        outputStream.close();
        InputStream inputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                inputStream);
        zipInputStream.read(new byte[1][0]);
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        Assert.assertFalse(zipInputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseWhenAlreadyClosed() throws IOException {
        zipInputStream.close();
        zipInputStream.close();
    }
}