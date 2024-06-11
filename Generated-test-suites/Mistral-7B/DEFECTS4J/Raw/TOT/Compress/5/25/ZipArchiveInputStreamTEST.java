import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ZipArchiveInputStreamTest {

    private byte[] testZipData;
    private ZipArchiveInputStream zipStream;
    private ZipFile zipFile;
    private ZipEntry zipEntry;

    @Before
    public void setup() throws IOException, DataFormatException, ZipException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(byteStream);

        ZipArchiveEntry entry = new ZipArchiveEntry("testEntry.txt");
        out.putNextEntry(entry);
        out.write("Hello World!".getBytes());
        out.finish();
        out.close();

        testZipData = byteStream.toByteArray();
        zipFile = new ZipFile(new ByteArrayInputStream(testZipData));
        zipEntry = zipFile.getEntry("testEntry.txt");
        zipStream = new ZipArchiveInputStream(zipFile.getInputStream(zipEntry));
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipStream.getNextZipEntry();
        assertEquals("testEntry.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(ZipArchiveOutputStream.DEFLATED, entry.getMethod());
        assertSame(zipEntry, entry);

        entry = zipStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryAfterClosed() throws IOException {
        zipStream.close();
        zipStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];

        int length = zipStream.read(buffer);
        assertEquals(5, length);
        assertEquals("Hello W".getBytes()[0], buffer[0]);

        length = zipStream.read(buffer, 1, buffer.length - 1);
        assertEquals(5, length);
        assertEquals("o World!".getBytes()[0], buffer[1]);

        length = zipStream.read(buffer, 6, buffer.length - 6);
        assertEquals(5, length);
        assertEquals("rld!".getBytes()[0], buffer[6]);

        length = zipStream.read(buffer, 11, buffer.length - 11);
        assertEquals(-1, length);

        zipStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];

        int length = zipStream.read(buffer);
        assertEquals(5, length);

        zipStream.closeEntry();

        length = zipStream.read(buffer);
        assertEquals(5, length);

        zipStream.closeEntry();

        length = zipStream.read(buffer);
        assertEquals(-1, length);
    }

    @Test
    public void testReadWithInvalidSignature() throws IOException {
        byte[] invalidSignature = new byte[]{"A", "B", "C", "D", "E"};
        InputStream invalidStream = new ByteArrayInputStream(invalidSignature);
        ZipArchiveInputStream invalidZipStream = new ZipArchiveInputStream(invalidStream);
        assertNull(invalidZipStream.getNextZipEntry());
        invalidZipStream.close();
    }
}