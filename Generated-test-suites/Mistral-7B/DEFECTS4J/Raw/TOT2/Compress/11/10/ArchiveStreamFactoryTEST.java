import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromName() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass().getSuperclass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        String archiverName = "tar";
        byte[] archiveSignature = new byte[512];
        InputStream inputStream = new ByteArrayInputStream(archiveSignature);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertEquals(ArchiveInputStream.class, archiveInputStream.getClass());
        assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass().getSuperclass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromName() throws Exception {
        String archiverName = "jar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass().getSuperclass());
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromBytes() throws Exception {
        String archiverName = "ar";
        byte[] archiveHeader = new byte[12];
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(ArchiveOutputStream.class, archiveOutputStream.getClass());
        assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass().getSuperclass());
        archiveOutputStream.putNextEntry(new ArArchiveEntry("test.txt"));
        archiveOutputStream.write("Test data".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, new ByteArrayInputStream(outputStream.toByteArray()));
        ArArchiveEntry archiveEntry = (ArArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals("Test data", new String(archiveInputStream.readAllBytes()));
        archiveInputStream.close();
    }

    @Test
    public void testUnknownArchiverName() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testNullArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream));
    }

    @Test
    public void testNullInputStream() throws Exception {
        String archiverName = "zip";
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, null));
    }

    @Test
    public void testNullOutputStream() throws Exception {
        String archiverName = "jar";
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, null));
    }

    @Test
    public void testCreateInvalidArchiveInputStream() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertThrows(ArchiveException.class, () -> archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullEntry() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertThrows(IllegalArgumentException.class, () -> archiveOutputStream.putNextEntry(null));
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }
}