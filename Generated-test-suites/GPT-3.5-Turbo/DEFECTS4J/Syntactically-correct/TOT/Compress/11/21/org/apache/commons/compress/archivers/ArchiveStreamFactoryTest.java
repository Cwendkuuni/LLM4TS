import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithAr() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithCpio() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithDump() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithJar() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithTar() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithZip() throws ArchiveException {
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateArchiveOutputStreamWithAr() throws ArchiveException {
        ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertNotNull(output);
    }

    @Test
    public void testCreateArchiveOutputStreamWithCpio() throws ArchiveException {
        ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertNotNull(output);
    }

    @Test
    public void testCreateArchiveOutputStreamWithJar() throws ArchiveException {
        ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertNotNull(output);
    }

    @Test
    public void testCreateArchiveOutputStreamWithTar() throws ArchiveException {
        ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertNotNull(output);
    }

    @Test
    public void testCreateArchiveOutputStreamWithZip() throws ArchiveException {
        ArchiveOutputStream output = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectZip() throws ArchiveException, IOException {
        byte[] bytes = {0x50, 0x4b, 0x03, 0x04};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectJar() throws ArchiveException, IOException {
        byte[] bytes = {0x50, 0x4b, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x08, 0x00};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectAr() throws ArchiveException, IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectCpio() throws ArchiveException, IOException {
        byte[] bytes = {0x30, 0x37, 0x30, 0x37, 0x30, 0x30, 0x30, 0x30};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectDump() throws ArchiveException, IOException {
        byte[] bytes = {0x30, 0x30, 0x00, 0x00, 0x01, 0x75, 0x70, 0x1f, 0x98, 0x82, 0x88, 0x2d, 0xdf, 0x01, 0x00, 0x00};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectTar() throws ArchiveException, IOException {
        byte[] bytes = {0x75, 0x73, 0x74, 0x61, 0x72, 0x00, 0x30, 0x30};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(input);
        in.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutodetectWithNullInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutodetectWithInputStreamNotSupportingMark() throws ArchiveException {
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
        };
        new ArchiveStreamFactory().createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutodetectWithUnknownSignature() throws ArchiveException, IOException {
        byte[] bytes = {0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        new ArchiveStreamFactory().createArchiveInputStream(in);
        in.close();
    }
}