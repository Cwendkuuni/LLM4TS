import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream("unknownFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() throws Exception {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamARWithBytes() throws Exception {
        byte[] bytes = new byte[10];
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(bytes));
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamJAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnsupportedStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        inputStream.mark(10);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        inputStream.reset();
        fail("Expected ArchiveException");
    }

    @Test
    public void testCreateArchiveOutputStreamAR() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknownFormat", outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullOutputStream() throws Exception {
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}