import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws Exception {
        byte[] byteArray = {0b01010100, 0b11110000};
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
    }

    @Test
    public void testReadBits() throws IOException {
        long result = bitInputStream.readBits(8);
        assertEquals(84, result);

        result = bitInputStream.readBits(4);
        assertEquals(10, result);
        
        result = bitInputStream.readBits(4);
        assertEquals(15, result);
        
        result = bitInputStream.readBits(4);
        assertEquals(0, result);
        
        result = bitInputStream.readBits(4);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsExceedCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(84, result);
    }
}