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
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR archive
        InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertEquals("AR archive", ais.getClass().getName());
        ais.close();

        // Test ZIP archive
        InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream zais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertEquals("ZIP archive", zais.getClass().getName());
        zais.close();

        // Test invalid archiver
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream iais = null;
        try {
            iais = new ArchiveStreamFactory().createArchiveInputStream("invalid", invalidInputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(iais);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archive
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertEquals("AR archive", aos.getClass().getName());
        aos.close();

        // Test ZIP archive
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertEquals("ZIP archive", zos.getClass().getName());
        zos.close();

        // Test invalid archiver
        OutputStream invalidOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream iaos = null;
        try {
            iaos = new ArchiveStreamFactory().createArchiveOutputStream("invalid", invalidOutputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(iaos);
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test AR archive
        byte[] arData = new byte[1024];
        InputStream arInputStream = new ByteArrayInputStream(arData);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        assertEquals("AR archive", ais.getClass().getName());
        ais.close();

        // Test ZIP archive
        byte[] zipData = new byte[1024];
        InputStream zipInputStream = new ByteArrayInputStream(zipData);
        ArchiveInputStream zais = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        assertEquals("ZIP archive", zais.getClass().getName());
        zais.close();

        // Test invalid archive
        byte[] invalidData = new byte[1024];
        InputStream invalidInputStream = new ByteArrayInputStream(invalidData);
        ArchiveInputStream iais = null;
        try {
            iais = new ArchiveStreamFactory().createArchiveInputStream(invalidInputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(iais);
    }
}