import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeflateCompressorInputStreamTest {

    @Mock
    private InputStream inputStream;

    private DeflateCompressorInputStream deflateInputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        // Test with valid compressed data
        byte[] compressedData = new byte[] { 120, -100, 120, 40, 75, 45, -56, -57, -19, -21, 54, 2, 0, 0, 0, 0 };
        InputStream input = new ByteArrayInputStream(compressedData);
        deflateInputStream = new DeflateCompressorInputStream(input);
        int result = deflateInputStream.read();
        assertEquals(120, result);

        // Test with end of compressed data
        result = deflateInputStream.read();
        assertEquals(-1, result);

        // Test with empty compressed data
        byte[] emptyData = new byte[0];
        input = new ByteArrayInputStream(emptyData);
        deflateInputStream = new DeflateCompressorInputStream(input);
        result = deflateInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] inputBytes = new byte[] { 120, -100, 120, 40, 75, 45, -56, -57 };
        byte[] targetArray = new byte[5];

        // Test reading 5 bytes from compressed data
        InputStream input = new ByteArrayInputStream(inputBytes);
        deflateInputStream = new DeflateCompressorInputStream(input);
        int bytesRead = deflateInputStream.read(targetArray, 0, 5);
        assertEquals(5, bytesRead);
        verify(inputStream, times(5)).read();

        // Test reading more than available bytes from compressed data
        // Result should be the total available bytes
        bytesRead = deflateInputStream.read(targetArray, 0, 10);
        assertEquals(3, bytesRead);
        verify(inputStream, times(8)).read();

        // Test reading with offset and length
        bytesRead = deflateInputStream.read(targetArray, 2, 2);
        assertEquals(2, bytesRead);
        verify(inputStream, times(10)).read();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        deflateInputStream = new DeflateCompressorInputStream(input);
        long result = deflateInputStream.skip(10);
        assertEquals(10, result);
        verify(input, times(1)).skip(10);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream input = mock(InputStream.class);
        Mockito.when(input.available()).thenReturn(5);
        deflateInputStream = new DeflateCompressorInputStream(input);
        int result = deflateInputStream.available();
        assertEquals(5, result);
        verify(input, times(1)).available();
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = mock(InputStream.class);
        deflateInputStream = new DeflateCompressorInputStream(input);
        deflateInputStream.close();
        verify(input, times(1)).close();
    }
    
    @Test
    public void testIsZlibHeader() throws IOException {
        // Test with valid zlib header
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x78, (byte) 0x9C, 0x02, 0x04 });
        deflateInputStream = new DeflateCompressorInputStream(input, parameters);
        boolean result = deflateInputStream.isZlibHeader();
        assertEquals(true, result);
        
        // Test without zlib header
        input = new ByteArrayInputStream(new byte[] { 0x78, (byte) 0xBA, 0x02, 0x04 });
        deflateInputStream = new DeflateCompressorInputStream(input);
        result = deflateInputStream.isZlibHeader();
        assertEquals(false, result);
    }
}