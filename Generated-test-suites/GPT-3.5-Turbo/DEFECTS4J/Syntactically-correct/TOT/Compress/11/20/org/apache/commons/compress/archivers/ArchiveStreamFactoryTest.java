import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void createArchiveInputStream_withArArchiver_returnsArArchiveInputStream() throws ArchiveException {
        ArchiveInputStreamMock arArchiveInputStreamMock = mock(ArArchiveInputStream.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, mockInputStream)).thenReturn(arArchiveInputStreamMock);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, mockInputStream);

        assertEquals(arArchiveInputStreamMock, result);
    }

    @Test
    public void createArchiveOutputStream_withArArchiver_returnsArArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStreamMock arArchiveOutputStreamMock = mock(ArArchiveOutputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, mockOutputStream)).thenReturn(arArchiveOutputStreamMock);

        ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, mockOutputStream);

        assertEquals(arArchiveOutputStreamMock, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_withNullArchiverName_throwsIllegalArgumentException() throws ArchiveException {
        InputStream mockInputStream = mock(InputStream.class);
        archiveStreamFactory.createArchiveInputStream(null, mockInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveOutputStream_withNullArchiverName_throwsIllegalArgumentException() throws ArchiveException {
        OutputStream mockOutputStream = mock(OutputStream.class);
        archiveStreamFactory.createArchiveOutputStream(null, mockOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_withNullInputStream_throwsIllegalArgumentException() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveOutputStream_withNullOutputStream_throwsIllegalArgumentException() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void createArchiveInputStream_withUnknownArchiverName_throwsArchiveException() throws ArchiveException {
        InputStream mockInputStream = mock(InputStream.class);
        archiveStreamFactory.createArchiveInputStream("unknown", mockInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void createArchiveOutputStream_withUnknownArchiverName_throwsArchiveException() throws ArchiveException {
        OutputStream mockOutputStream = mock(OutputStream.class);
        archiveStreamFactory.createArchiveOutputStream("unknown", mockOutputStream);
    }

    @Test
    public void createArchiveInputStream_withInputStream_returnsArchiveInputStream() throws ArchiveException {
        ArchiveInputStreamMock archiveInputStreamMock = mock(ArchiveInputStream.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(archiveStreamFactory.createArchiveInputStream(mockInputStream)).thenReturn(archiveInputStreamMock);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(archiveInputStreamMock, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_withNullInputStream_throwsIllegalArgumentException() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_withUnsupportedMark_throwsIllegalArgumentException() throws ArchiveException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(false);

        archiveStreamFactory.createArchiveInputStream(mockInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void createArchiveInputStream_withNoArchiverFoundForStreamSignature_throwsArchiveException() throws ArchiveException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);

        archiveStreamFactory.createArchiveInputStream(mockInputStream);
    }

    @Test
    public void createArchiveInputStream_withZipSignature_returnsZipArchiveInputStream() throws ArchiveException, IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(4);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(ZipArchiveInputStream.class, result.getClass());
    }

    @Test
    public void createArchiveInputStream_withJarSignature_returnsJarArchiveInputStream() throws ArchiveException, IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(4);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(10);
        when(mockInputStream.read(any(byte[].class))).thenReturn(11);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(JarArchiveInputStream.class, result.getClass());
    }

    @Test
    public void createArchiveInputStream_withArSignature_returnsArArchiveInputStream() throws ArchiveException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(4);
        when(mockInputStream.read(any(byte[].class))).thenReturn(1);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(5);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(ArArchiveInputStream.class, result.getClass());
    }

    @Test
    public void createArchiveInputStream_withCpioSignature_returnsCpioArchiveInputStream() throws ArchiveException, IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(4);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(7);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(CpioArchiveInputStream.class, result.getClass());
    }

    @Test
    public void createArchiveInputStream_withDumpSignature_returnsDumpArchiveInputStream() throws ArchiveException, IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(32);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(DumpArchiveInputStream.class, result.getClass());
    }

    @Test
    public void createArchiveInputStream_withTarSignature_returnsTarArchiveInputStream() throws ArchiveException, IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.read(any(byte[].class))).thenReturn(512);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);
        when(mockInputStream.read(any(byte[].class))).thenReturn(0);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(mockInputStream);

        assertEquals(TarArchiveInputStream.class, result.getClass());
    }
}