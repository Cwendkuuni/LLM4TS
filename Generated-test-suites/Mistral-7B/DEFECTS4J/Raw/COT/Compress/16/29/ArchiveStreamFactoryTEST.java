import org.apache.commons.compress.ArchiveException;
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
import org.junit.Assert;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidParameters() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        // Test AR
        ArchiveInputStream arInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(arInputStream);
        arInputStream.close();

        // Test CPIO
        ArchiveInputStream cpioInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(cpioInputStream);
        cpioInputStream.close();

        // Test DUMP
        ArchiveInputStream dumpInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(dumpInputStream);
        dumpInputStream.close();

        // Test JAR
        ArchiveInputStream jarInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(jarInputStream);
        jarInputStream.close();

        // Test TAR
        ArchiveInputStream tarInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(tarInputStream);
        tarInputStream.close();

        // Test ZIP
        ArchiveInputStream zipInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        Assert.assertNotNull(zipInputStream);
        zipInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        String unknownArchiver = "unknown";
        try {
            ArchiveInputStream archiveInputStream = archiveFactory.createArchiveInputStream(unknownArchiver, new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        String nullArchiver = null;
        try {
            ArchiveInputStream archiveInputStream = archiveFactory.createArchiveInputStream(nullArchiver, new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        String archiver = ArchiveStreamFactory.JAR;
        InputStream nullInputStream = null;
        try {
            ArchiveInputStream archiveInputStream = archiveFactory.createArchiveInputStream(archiver, nullInputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidParameters() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        // Test AR
        ArchiveOutputStream arOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(arOutputStream);
        arOutputStream.close();

        // Test CPIO
        ArchiveOutputStream cpioOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(cpioOutputStream);
        cpioOutputStream.close();

        // Test DUMP
        ArchiveOutputStream dumpOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(dumpOutputStream);
        dumpOutputStream.close();

        // Test JAR
        ArchiveOutputStream jarOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(jarOutputStream);
        jarOutputStream.close();

        // Test TAR
        ArchiveOutputStream tarOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(tarOutputStream);
        tarOutputStream.close();

        // Test ZIP
        ArchiveOutputStream zipOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream(1024));
        Assert.assertNotNull(zipOutputStream);
        zipOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        String unknownArchiver = "unknown";
        try {
            ArchiveOutputStream archiveOutputStream = archiveFactory.createArchiveOutputStream(unknownArchiver, new ByteArrayOutputStream(1024));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveFactory archiveFactory = new ArchiveStreamFactory();

        String archiver = ArchiveStreamFactory.JAR;
        OutputStream nullOutputStream = null;
        try {
            ArchiveOutputStream archiveOutputStream = archiveFactory.createArchiveOutputStream(archiver, nullOutputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}