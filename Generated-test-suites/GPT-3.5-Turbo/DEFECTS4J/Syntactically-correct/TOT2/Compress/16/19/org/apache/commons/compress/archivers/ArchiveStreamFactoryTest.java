import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveInputStream(null, in);
        });
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveInputStream("zip", null);
        });
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> {
            archiveStreamFactory.createArchiveInputStream("unknown", in);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        OutputStream out = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveOutputStream(null, out);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveOutputStream("zip", null);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName_ThrowsArchiveException() {
        OutputStream out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> {
            archiveStreamFactory.createArchiveOutputStream("unknown", out);
        });
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_NullStream_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveInputStream(null);
        });
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_StreamNotSupportingMark_ThrowsIllegalArgumentException() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> {
            archiveStreamFactory.createArchiveInputStream(in);
        });
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_UnknownArchiveType_ThrowsArchiveException() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> {
            archiveStreamFactory.createArchiveInputStream(in);
        });
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZipArchiveInputStream() throws IOException {
        byte[] data = new byte[] { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_JarArchiveInputStream() throws IOException {
        byte[] data = new byte[] { 0x50, 0x4b, 0x03, 0x04 };
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ArArchiveInputStream() throws IOException {
        byte[] data = new byte[] { 033, 016, 004 };
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_CpioArchiveInputStream() throws IOException {
        byte[] data = new byte[] { 37, 71, 03 };
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_DumpArchiveInputStream() throws IOException {
        byte[] data = new byte[] { 0x6c, 0x33, 0x4d, 0x6e };
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_TarArchiveInputStream() throws IOException {
        byte[] data = new byte[512];
        InputStream in = new ByteArrayInputStream(data);
        assertNotNull(archiveStreamFactory.createArchiveInputStream(in));
    }
}