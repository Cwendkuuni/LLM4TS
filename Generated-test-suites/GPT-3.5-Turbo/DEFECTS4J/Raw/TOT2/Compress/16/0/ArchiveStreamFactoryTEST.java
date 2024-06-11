import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private InputStream mockInputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
        mockInputStream = mock(InputStream.class);
        mockOutputStream = mock(OutputStream.class);
    }

    @Test
    public void testCreateArchiveInputStream() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        try {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, mockInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, mockOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNoMarkSupported() throws ArchiveException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(false);
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectZip() {
        byte[] magicChars = new byte[]{0x50, 0x4B, 0x03, 0x04};
        InputStream inputStream = new ByteArrayInputStream(magicChars);
        try {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectJar() {
        byte[] magicChars = new byte[]{0x50, 0x4B, 0x03, 0x04};
        InputStream inputStream = new ByteArrayInputStream(magicChars);
        try {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
            assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
        } catch (ArchiveException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectUnknownArchive() throws ArchiveException {
        byte[] magicChars = new byte[]{0x00, 0x00, 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(magicChars);
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectIOException() throws ArchiveException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        when(inputStream.read()).thenThrow(new IOException());
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

}