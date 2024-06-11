import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(10);
        zipOut.putNextEntry(zipEntry);
        zipOut.write("Hello World!".getBytes());
        zipOut.closeEntry();
        zipOut.finish();
        zipOut.close();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10, entry.getSize());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextEntryWithClosedStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithNullEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithEOF() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(10);
        zipOut.putNextEntry(zipEntry);
        zipOut.write("Hello World!".getBytes());
        zipOut.closeEntry();
        zipOut.finish();
        zipOut.close();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(10, entry.getSize());

        // Read all bytes from the entry
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer, 0, buffer.length);

        // Get next entry should return null
        Assert.assertNull(zipArchiveInputStream.getNextEntry());

        zipArchiveInputStream.close();
    }

    @Test
    public void testReadWithStoredEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(10);
        zipEntry.setMethod(ZipEntry.STORED);
        zipOut.putNextEntry(zipEntry);
        zipOut.write("Hello World!".getBytes());
        zipOut.closeEntry();
        zipOut.finish();
        zipOut.close();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, bytesRead);
        Assert.assertArrayEquals("Hello World!".getBytes(), buffer);

        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);

        zipArchiveInputStream.close();
    }

    @Test
    public void testReadWithDeflateEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(-1);
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOut.putNextEntry(zipEntry);
        zipOut.write("Hello World!".getBytes());
        zipOut.closeEntry();
        zipOut.finish();
        zipOut.close();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ArchiveEntry entry = zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, bytesRead);
        Assert.assertArrayEquals("Hello World!".getBytes(), buffer);

        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, bytesRead);
        Assert.assertArrayEquals("Hello World!".getBytes(), buffer);

        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);

        zipArchiveInputStream.close();
    }

    @Test
    public void testReadWithDeflateEntryWithEmptyInputStream() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0][]));
        Assert.assertNull(zipArchiveInputStream.getNextEntry());
        zipArchiveInputStream.close();
    }

    @Test(expected = DataFormatException.class)
    public void testReadWithInvalidEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(out);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(10);
        zipOut.putNextEntry(zipEntry);
        zipOut.write("Hello World!".getBytes());
        zipOut.closeEntry();

        // Change the signature of the Local File Header
        byte[] buffer = out.toByteArray();
        buffer[0] = (byte) 0x02;
        buffer[1] = (byte) 0x03;
        buffer[2] = (byte) 0x4b;
        buffer[3] = (byte) 0x50;

        InputStream inputStream = new ByteArrayInputStream(buffer);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextEntry();

        byte[] testBuffer = new byte[10];
        zipArchiveInputStream.read(testBuffer, 0, 10);
    }
}