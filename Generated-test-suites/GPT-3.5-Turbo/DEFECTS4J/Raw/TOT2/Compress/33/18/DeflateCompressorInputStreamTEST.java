import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        int result = stream.read();
        assertEquals(65, result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        byte[] buffer = new byte[3];
        int result = stream.read(buffer, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{65, 66, 67}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        long result = stream.skip(2);
        assertEquals(2, result);
        assertEquals(67, stream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] testData = {65, 66, 67, 68, 69};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        int result = stream.available();
        assertEquals(5, result);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream input = mock(ByteArrayInputStream.class);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        stream.close();
        verify(input).close();
    }

    @Test
    public void testDeflateParameters() throws IOException {
        byte[] testData = {120, 121, 121, 122, 122};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateParameters params = new DeflateParameters();
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input, params);

        int result = stream.read();
        assertEquals(120, result);
    }
    
    @Test
    public void testZlibHeaderFalse() throws IOException {
        byte[] testData = {120, 121, 121, 122, 122};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        DeflateParameters params = new DeflateParameters();
        params.setWithZlibHeader(false);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input, params);

        int result = stream.read();
        assertEquals(120, result);
    }

}