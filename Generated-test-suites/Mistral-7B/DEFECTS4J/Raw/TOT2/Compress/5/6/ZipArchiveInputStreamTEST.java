import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextEntry() throws IOException, ZipException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNotNull(entry.getSize());
        Assert.assertEquals(-1, entry.getSize()); // DEFLATE algorithm is used
        Assert.assertNull(entry.getExtra());
        Assert.assertEquals("", entry.getComment());

        zipArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testRead() throws IOException, ZipException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(10, length);
        String s = new String(buffer);
        Assert.assertEquals("Hello World!", s);

        zipArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testGetNextEntryNull() throws IOException, ZipException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadEOF() throws IOException, ZipException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(10, length);
        String s = new String(buffer);
        Assert.assertEquals("Hello World!", s);

        length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, length);

        zipArchiveInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadNullBuffer() throws IOException, ZipException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        int length = zipArchiveInputStream.read(null);
    }

    @Test
    public void testReadError() throws IOException, ZipException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test2.txt")); // empty entry
        zipArchiveOutputStream.finish();
        zipArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);

        byte[] buffer = new byte[10];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(10, length);
        String s = new String(buffer);
        Assert.assertEquals("Hello World!", s);

        length = zipArchiveInputStream.read(buffer);
        Assert.assertEquals(-1, length);

        ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream errorZipArchiveOutputStream = new ZipArchiveOutputStream(errorOutputStream);
        errorZipArchiveOutputStream.putNextEntry(new ZipEntry("test2.txt"));
        try {
            errorZipArchiveOutputStream.write("Error!"
                    .getBytes()); // Should throw DataFormatException
        } catch (DataFormatException e) {
            // Continue
        } finally {
            errorZipArchiveOutputStream.close();
        }

        ByteArrayInputStream errorInputStream = new ByteArrayInputStream(errorOutputStream.toByteArray());
        ZipArchiveInputStream errorZipArchiveInputStream = new ZipArchiveInputStream(errorInputStream);

        try {
            length = errorZipArchiveInputStream.read(buffer);
            Assert.fail(); // Should throw DataFormatException
        } catch (DataFormatException e) {
            // Continue
        } finally {
            errorZipArchiveInputStream.close();
            errorInputStream.close();
            outputStream.close();
            inputStream.close();
        }
    }
}