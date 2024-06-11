import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidName() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInput() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_InvalidName() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutput() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ZIP_InvalidName() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_NullInput() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = null;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_ZIP_InvalidName() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullOutput() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = null;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownFormat_NullInput() throws Exception {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiveName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiveName_NullInput() throws Exception {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_Empty() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR_Empty_NullOutput() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_Empty_ValidOutput() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream(1024);
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }
}