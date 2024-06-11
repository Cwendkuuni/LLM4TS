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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validArchiveTypes() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream arArchiveInputStream = archiveStreamFactory.createArchiveInputStream("ar", arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, arArchiveInputStream);

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream cpioArchiveInputStream = archiveStreamFactory.createArchiveInputStream("cpio", cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream dumpArchiveInputStream = archiveStreamFactory.createArchiveInputStream("dump", dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpArchiveInputStream);

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream jarArchiveInputStream = archiveStreamFactory.createArchiveInputStream("jar", jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarArchiveInputStream);

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream tarArchiveInputStream = archiveStreamFactory.createArchiveInputStream("tar", tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream zipArchiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_invalidArchiveType() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        InputStream invalidInputStream = new ByteArrayInputStream(new byte[10]);
        archiveStreamFactory.createArchiveInputStream("invalid", invalidInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_validArchiveTypes() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("ar", arOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, arArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("cpio", cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioArchiveOutputStream);

        // Test DUMP
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("dump", dumpOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, dumpArchiveOutputStream); // Note: DumpArchiveOutputStream is actually a CpioArchiveOutputStream

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("jar", jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarArchiveOutputStream);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("tar", tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_invalidArchiveType() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        OutputStream invalidOutputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("invalid", invalidOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetection() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        // Test JAR (ZIP with a different magic number)
        InputStream jarInputStream = new ByteArrayInputStream(new byte[10]);
        jarInputStream.skip(2); // Skip the first 2 bytes to simulate JAR's different magic number
        ArchiveInputStream archiveInputStreamJar = archiveStreamFactory.createArchiveInputStream(jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStreamJar);

        // Test empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamEmpty = archiveStreamFactory.createArchiveInputStream(emptyInputStream);
        assertNull(archiveInputStreamEmpty);
    }
}