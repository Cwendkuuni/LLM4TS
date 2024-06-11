import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        for (int i = 1; i <= data.length; i++) {
            int read = inputStream.read();
            assertEquals((byte) data[i - 1], (byte) read);
        }
        assertEquals(-1, inputStream.read());
        
        stream.close();
        inputStream.close();
    }

    @Test
    public void testRead_withOffsetAndLength() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        byte[] buffer = new byte[10];
        int read = inputStream.read(buffer, 0, buffer.length);
        
        assertEquals(10, read);
        assertArrayEquals(new byte[]{0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb}, buffer);
        
        stream.close();
        inputStream.close();
    }

    @Test
    public void testSkip() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        inputStream.skip(5);
        assertEquals(5, inputStream.read());
        
        stream.close();
        inputStream.close();
    }

    @Test
    public void testAvailable() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        assertEquals(data.length, inputStream.available());
        
        inputStream.skip(5);
        assertEquals(data.length - 5, inputStream.available());
        
        stream.close();
        inputStream.close();
    }
    
    @Test
    public void testClose() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        inputStream.close();
        assertTrue(stream.closed);
    }
    
    @Test
    public void testDeflateCompressorInputStream_withParameters() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(false);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream, parameters);

        for (int i = 1; i <= data.length; i++) {
            int read = inputStream.read();
            assertEquals((byte) data[i - 1], (byte) read);
        }
        assertEquals(-1, inputStream.read());
        
        stream.close();
        inputStream.close();
    }
    
    @Test
    public void testIsZlibHeader_withHeader() throws Exception {
        byte[] data = { 0x78, 0x9c, 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb, 0xff, 0xff, 0x0f, 0xea, 0x02, 0x51, 0x67, 0x03 };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        assertFalse(inputStream.isZlibHeader(data, data.length));
        
        stream.close();
        inputStream.close();
    }
    
    @Test
    public void testIsZlibHeader_withoutHeader() throws Exception {
        byte[] data = { 0x5d, 0x55, 0x53, 0x08, 0x57, 0x14, 0x5d, 0xc6, 0xbf, 0xfb };
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(stream);
        
        assertTrue(inputStream.isZlibHeader(data, data.length));
        
        stream.close();
        inputStream.close();
    }
    
}