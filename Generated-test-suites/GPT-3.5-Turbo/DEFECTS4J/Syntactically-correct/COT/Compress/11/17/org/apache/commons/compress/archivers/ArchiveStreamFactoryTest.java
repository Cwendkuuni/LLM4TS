
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream expected = new ArArchiveInputStream(in);
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);

        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testCreateArchiveInputStreamARNullArchiverName() {
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamARNullInputStream() {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = null;

        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamZIP() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream expected = new ZipArchiveInputStream(in);
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);

        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    // Add more test cases for the remaining public methods in the ArchiveStreamFactory class...

}

