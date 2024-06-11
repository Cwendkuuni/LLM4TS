import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Test
    public void testReadBits() throws IOException {
        byte[] byteArray = {0b01010101, 0b10101010};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        long result = bitInputStream.readBits(8);
        assertEquals(85, result);

        result = bitInputStream.readBits(4);
        assertEquals(5, result);

        result = bitInputStream.readBits(4);
        assertEquals(10, result);

        result = bitInputStream.readBits(8);
        assertEquals(-1, result);
        
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        byte[] byteArray = {0b01010101, 0b10101010};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        
        long result = bitInputStream.readBits(4);
        assertEquals(5, result);
        
        bitInputStream.clearBitCache();
        
        result = bitInputStream.readBits(8);
        assertEquals(170, result);
        
        bitInputStream.close();
    }
}