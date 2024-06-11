import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);

        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream("UNKNOWN", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);

        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("UNKNOWN", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException, IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(true);
        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(null);
        Mockito.when(inputStream.reset()).thenReturn(null);
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn(ArchiveStreamFactory.ZIP.getBytes().length).thenReturn(-1);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);

        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithInputStreamNotSupportingMark() throws ArchiveException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(false);

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnrecognizedSignature() throws ArchiveException, IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(true);
        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(null);
        Mockito.when(inputStream.reset()).thenReturn(null);
        // Set unrecognized signature
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn("UNRECOGNIZED".getBytes().length).thenReturn(-1);

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithDumpSignatureButNotDumpArchive() throws ArchiveException, IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(true);
        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(null);
        Mockito.when(inputStream.reset()).thenReturn(null);
        // Set dump signature
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn("DUMP_SIGNATURE".getBytes().length).thenReturn(-1);

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithTarSignatureButNotTARArchive() throws ArchiveException, IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(true);
        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(null);
        Mockito.when(inputStream.reset()).thenReturn(null);
        // Set TAR signature
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn("TAR_SIGNATURE".getBytes().length).thenReturn(-1);
        // Set TAR signature not matching TARIdentifier
        Mockito.when(inputStream.getNextEntry()).thenThrow(new IOException());

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithTarSignatureAndTARArchive() throws ArchiveException, IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.markSupported()).thenReturn(true);
        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(null);
        Mockito.when(inputStream.reset()).thenReturn(null);
        // Set TAR signature
        Mockito.when(inputStream.read(Mockito.any(byte[].class))).thenReturn("TAR_SIGNATURE".getBytes().length).thenReturn(-1);
        // Set valid TAR archive
        Mockito.when(inputStream.getNextEntry()).thenReturn(Mockito.mock(TarArchiveEntry.class));

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);

        assertNotNull(archiveInputStream);
    }
}