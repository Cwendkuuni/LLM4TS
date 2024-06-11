import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    // Test createArchiveInputStream with known archiverName and valid InputStream
    @Test
    public void testCreateArchiveInputStream_validInput() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Test createArchiveInputStream with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    // Test createArchiveInputStream with null InputStream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    // Test createArchiveOutputStream with known archiverName and valid OutputStream
    @Test
    public void testCreateArchiveOutputStream_validOutput() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Test createArchiveOutputStream with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    // Test createArchiveOutputStream with null OutputStream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    // Test createArchiveInputStream auto-detection with valid ZIP InputStream
    @Test
    public void testCreateArchiveInputStream_autoDetect_ZIP() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            zos.putNextEntry(new ZipEntry("test.zip"));
            zos.closeEntry();
            zos.close();
            InputStream in = new ByteArrayInputStream(baos.toByteArray());
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Test createArchiveInputStream auto-detection with invalid signature
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_autoDetect_invalidSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }

    // Test createArchiveInputStream auto-detection with valid TAR InputStream
    @Test
    public void testCreateArchiveInputStream_autoDetect_TAR() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream out = new TarArchiveOutputStream(baos);
            out.close();
            InputStream in = new ByteArrayInputStream(baos.toByteArray());
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Test createArchiveInputStream auto-detection with null stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_autoDetect_nullStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

}