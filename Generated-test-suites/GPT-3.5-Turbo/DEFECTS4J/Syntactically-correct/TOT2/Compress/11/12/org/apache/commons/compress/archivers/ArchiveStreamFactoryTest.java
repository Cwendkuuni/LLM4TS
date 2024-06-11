import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException for null input stream");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, System.out);
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("tar", null);
            fail("Expected IllegalArgumentException for null output stream");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnsupportedMark() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException for unsupported mark");
        } catch (IllegalArgumentException e) {
            // Pass
        } catch (ArchiveException ae) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException for unknown archiver name");
        } catch (ArchiveException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("unknown", System.out);
            fail("Expected ArchiveException for unknown archiver name");
        } catch (ArchiveException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] zipSignature = new byte[] {80, 75, 3, 4}; // ZIP signature
        ByteArrayInputStream in = new ByteArrayInputStream(zipSignature);
        try {
            factory.createArchiveInputStream(in);
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException during auto detection of ZIP format");
        }
    }

}