import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream("zip", in);
            assertNotNull(archiveIn);
        } catch (ArchiveException e) {
            fail("Should not throw ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("zip", out);
            assertNotNull(archiveOut);
        } catch (ArchiveException e) {
            fail("Should not throw ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(null, in);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Should throw IllegalArgumentException, not ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiver() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(null, out);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Should throw IllegalArgumentException, not ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveInputStream archiveIn = factory.createArchiveInputStream("zip", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Should throw IllegalArgumentException, not ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("zip", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Should throw IllegalArgumentException, not ArchiveException");
        }
    }

    // Add more test cases for edge cases and error scenarios

}