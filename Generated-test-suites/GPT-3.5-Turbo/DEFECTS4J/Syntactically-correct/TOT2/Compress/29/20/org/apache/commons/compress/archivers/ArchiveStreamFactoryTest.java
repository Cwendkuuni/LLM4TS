import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, inputStream);
        });
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, outputStream);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        });
    }

    @Test
    public void testCreateArchiveInputStream_ArchiveFormatException() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream("fake", inputStream);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_ArchiveFormatException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveOutputStream("fake", outputStream);
        });
    }

}