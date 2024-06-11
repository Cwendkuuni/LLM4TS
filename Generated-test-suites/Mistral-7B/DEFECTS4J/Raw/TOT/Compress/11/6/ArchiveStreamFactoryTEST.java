import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Test for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from an archive name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String[] archiverNames = new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"};
        InputStream in = new ByteArrayInputStream(new byte[1024]);

        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, in);
            try {
                fail("Expected ArchiveException.");
            } catch (ArchiveException e) {
                if (!ARCHIVER_NOT_FOUND.equalsIgnoreCase(e.getMessage())) {
                    throw e;
                }
            } finally {
                assertNull(archiveInputStream);
            }
        }

        String zipArchiverName = "zip";
        InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        zipInputStream.mark(1024);
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(zipArchiverName, zipInputStream);
        assertNotNull(zipArchiveInputStream);
        zipArchiveInputStream.close();
    }

    /**
     * Test creating an archive output stream from an archive name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String[] archiverNames = new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"};
        OutputStream out = new ByteArrayOutputStream();

        for (String archiverName : archiverNames) {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, out);
            try {
                fail("Expected ArchiveException.");
            } catch (ArchiveException e) {
                if (!ARCHIVER_NOT_FOUND.equalsIgnoreCase(e.getMessage())) {
                    throw e;
                }
            } finally {
                assertNull(archiveOutputStream);
            }
        }

        String zipArchiverName = "zip";
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(zipArchiverName, zipOutputStream);
        assertNotNull(zipArchiveOutputStream);
        zipArchiveOutputStream.close();
    }

    /**
     * Test creating an archive input stream from an input stream, autodetecting the archive type.
     */
    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        String[] archiverNames = new String[]{"ar", "zip", "tar", "jar", "cpio", "dump"};
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);

        for (String archiverName : archiverNames) {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream);
            try {
                if (!ARCHIVER_NOT_FOUND.equalsIgnoreCase(archiveInputStream.getFormat().getName())) {
                    assertEquals(archiverName, archiveInputStream.getFormat().getName());
                }
                archiveInputStream.close();
            } catch (ArchiveException e) {
                fail("Unexpected exception: " + e.getMessage());
            } catch (IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    private static final String ARCHIVER_NOT_FOUND = "Archiver not found.";
}