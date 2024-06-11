import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals('H', deflateInputStream.read());
        assertEquals('e', deflateInputStream.read());
        assertEquals('l', deflateInputStream.read());
        assertEquals('l', deflateInputStream.read());
        assertEquals('o', deflateInputStream.read());
        assertEquals(-1, deflateInputStream.read());
        deflateInputStream.close();
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        assertEquals(5, deflateInputStream.read(buffer, 0, 5));
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
        deflateInputStream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(3, deflateInputStream.skip(3));
        assertEquals('l', deflateInputStream.read());
        deflateInputStream.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {72, 101, 108, 108, 111}; // "Hello"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(5, deflateInputStream.available());
        deflateInputStream.read(new byte[3]);
        assertEquals(2, deflateInputStream.available());
        deflateInputStream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        assertFalse(deflateInputStream.isClosed());
        deflateInputStream.close();
        assertTrue(deflateInputStream.isClosed());
    }
    
    @Test
    public void testDeflateParameters() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        assertTrue(deflateInputStream.isHeaderless());
    }
}