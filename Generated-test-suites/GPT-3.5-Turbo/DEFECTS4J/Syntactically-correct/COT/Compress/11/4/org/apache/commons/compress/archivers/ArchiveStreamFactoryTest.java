
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(archiveInputStream);
            in.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new FileOutputStream("test.txt");
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(archiveOutputStream);
            out.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromFile() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new FileInputStream("test.txt");
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            assertNotNull(archiveInputStream);
            in.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
