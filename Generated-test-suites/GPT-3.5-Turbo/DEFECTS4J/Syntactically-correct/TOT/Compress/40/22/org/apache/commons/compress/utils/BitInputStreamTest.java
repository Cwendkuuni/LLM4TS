
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() throws IOException {
        byte[] bytes = {0x4A, 0x80, 0x00, (byte)0xFF};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testReadBits_typical() throws IOException {
        assertEquals(0x4A8000, bitInputStream.readBits(24));
    }
    
    @Test
    public void testReadBits_limit() throws IOException {
        assertEquals(0xFF, bitInputStream.readBits(8));
    }
    
    @Test
    public void testReadBits_edge() throws IOException {
        assertEquals(0x4A8000FF, bitInputStream.readBits(32));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testReadBits_wrongCount() throws IOException {
        bitInputStream.readBits(64); // should throw an exception
    }
    
    @Test
    public void testReadBits_error() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(32));
    }
}
