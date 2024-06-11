import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        byte[] byteArray = {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.readBits(10);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCachedSize);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCountGreaterThan63() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
        assertEquals(0x67452301L, bitInputStream.readBits(32));
        assertEquals(0xEFCDAB89L, bitInputStream.readBits(32));
        assertEquals(0x00000067L, bitInputStream.readBits(8));
        assertEquals(0x0089ABCDL, bitInputStream.readBits(32));
        assertEquals(0x000000EFL, bitInputStream.readBits(8));
        assertEquals(-1L, bitInputStream.readBits(1));
        assertEquals(-1L, bitInputStream.readBits(8));
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {(byte) 0xEF, (byte) 0xCD, (byte) 0xAB, (byte) 0x89,
                                                                                   0x67, 0x45, 0x23, 0x01}),
                                           ByteOrder.BIG_ENDIAN);
        assertEquals(0xEFCDAB8967452301L, bitInputStream.readBits(64));
        assertEquals(0x0000000000000067L, bitInputStream.readBits(32));
        assertEquals(0x000000000089ABCDL, bitInputStream.readBits(40));
        assertEquals(0x00000000000000EFL, bitInputStream.readBits(8));
        assertEquals(-1L, bitInputStream.readBits(1));
        assertEquals(-1L, bitInputStream.readBits(8));
    }
}