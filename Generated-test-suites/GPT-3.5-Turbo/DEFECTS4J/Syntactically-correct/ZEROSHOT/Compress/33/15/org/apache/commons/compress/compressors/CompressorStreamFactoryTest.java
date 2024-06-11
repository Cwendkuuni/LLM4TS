import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CompressorStreamFactoryTest {
    @Mock
    private InputStream mockInputStream;
    @Mock
    private OutputStream mockOutputStream;

    private CompressorStreamFactory compressorStreamFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        compressorStreamFactory = new CompressorStreamFactory();
    }

    @Test
    public void testCreateCompressorInputStream_validInputStream() throws Exception {
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(mockInputStream);
        assertNotNull(compressorInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_nullInputStream() throws Exception {
        compressorStreamFactory.createCompressorInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_inputStreamDoesNotSupportMark() throws Exception {
        when(mockInputStream.markSupported()).thenReturn(false);
        compressorStreamFactory.createCompressorInputStream(mockInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_compressorFound() throws Exception {
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.markSupported()).thenReturn(true);
        InputStream in = new ByteArrayInputStream("test".getBytes());
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(in);
        assertNotNull(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_compressorNotFound() throws Exception {
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.markSupported()).thenReturn(true);
        when(mockInputStream.markSupported()).thenReturn(true);
        InputStream in = new ByteArrayInputStream("test".getBytes());
        compressorStreamFactory.createCompressorInputStream(in);
    }

    @Test
    public void testCreateCompressorInputStream_validNameAndInputStream() throws Exception {
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", mockInputStream);
        assertNotNull(compressorInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_nullNameAndInputStream() throws Exception {
        compressorStreamFactory.createCompressorInputStream(null, null);
    }

    @Test
    public void testCreateCompressorInputStream_compressorNameFound() throws Exception {
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", mockInputStream);
        assertNotNull(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_compressorNameNotFound() throws Exception {
        compressorStreamFactory.createCompressorInputStream("invalid", mockInputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_validNameAndOutputStream() throws Exception {
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gzip", mockOutputStream);
        assertNotNull(compressorOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_nullNameAndOutputStream() throws Exception {
        compressorStreamFactory.createCompressorOutputStream(null, null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_compressorNameNotFound() throws Exception {
        compressorStreamFactory.createCompressorOutputStream("invalid", mockOutputStream);
    }
}