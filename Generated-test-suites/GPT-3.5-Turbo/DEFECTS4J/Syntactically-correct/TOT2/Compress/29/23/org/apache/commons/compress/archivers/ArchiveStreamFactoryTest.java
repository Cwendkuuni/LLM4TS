import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;
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
import org.junit.BeforeClass;

public class ArchiveStreamFactoryTest {
    
    private static ArchiveStreamFactory archiveStreamFactory;

    @BeforeClass
    public static void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    // Test case for createArchiveInputStream - AR
    @Test
    public void testCreateArchiveInputStreamAR() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArArchiveInputStream ais = (ArArchiveInputStream) archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Test case for createArchiveInputStream - ARJ
    @Test
    public void testCreateArchiveInputStreamARJ() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArjArchiveInputStream ais = (ArjArchiveInputStream) archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ARJ, in);
            assertNotNull(ais);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected here");
        }
    }

    // Additional test cases for createArchiveInputStream and createArchiveOutputStream can be added for other formats

}