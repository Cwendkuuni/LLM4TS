import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveIn = factory.createArchiveInputStream("zip", in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("zip", out);
        assertNotNull(archiveOut);
        assertTrue(archiveOut instanceof ZipArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiver() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithoutMarkSupport() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            public int read() {
                return 0;
            }
        };
        factory.createArchiveInputStream(in);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithZipSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x50, 0x4B, 0x03, 0x04 });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithJarSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x50, 0x4B, 0x03, 0x04 });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithArSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x21, 0x3C, 0x61, 0x72, 0x63, 0x68, 0x3E });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithCpioSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x30, 0x37, 0x30, 0x37 });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithDumpSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x3C });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithTarSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 0x75, 0x73, 0x74, 0x61, 0x72 });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof TarArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnknownSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnknownStreamSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            public int read() {
                return 0;
            }
        };
        factory.createArchiveInputStream(in);
    }
}