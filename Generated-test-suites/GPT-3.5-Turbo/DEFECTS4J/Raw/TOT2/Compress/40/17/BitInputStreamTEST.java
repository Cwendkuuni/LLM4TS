import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    @Test
    public void testClearBitCache() {
        byte[] testData = {0b10101010, 0b01010101};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(4); // read 4 bits
        bitInputStream.clearBitCache();
        
        assertEquals(0, bitInputStream.readBits(1)); // read the first bit after cache clearance
    }
    
    @Test
    public void testReadBits() throws IOException {
        byte[] testData = {0b10101010, 0b01010101};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        
        assertEquals(0b1010, bitInputStream.readBits(4)); // read first 4 bits
        
        assertEquals(0b101, bitInputStream.readBits(3)); // read next 3 bits
        
        assertEquals(0b11010, bitInputStream.readBits(5)); // read remaining 5 bits
        
        assertEquals(-1, bitInputStream.readBits(8)); // trying to read more bits than available
        
        assertThrows(IllegalArgumentException.class, () -> {
            bitInputStream.readBits(-1); // trying to read a negative number of bits
        });
    }
}