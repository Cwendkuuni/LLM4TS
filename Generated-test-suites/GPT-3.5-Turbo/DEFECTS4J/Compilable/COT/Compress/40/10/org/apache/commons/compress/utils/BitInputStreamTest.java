package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;
    
    @Before
    public void setup() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @Test
    public void testReadBits() throws IOException {
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(1, result);
        result = bitInputStream.readBits(8);
        Assert.assertEquals(2, result);
        result = bitInputStream.readBits(8);
        Assert.assertEquals(3, result);
    }
    
    @Test
    public void testReadBitsNegative() throws IOException {
        long result = bitInputStream.readBits(-1);
        Assert.assertEquals(-1, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsOutOfRange() throws IOException {
        long result = bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        // Validate if the bit cache is cleared
    }
    
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        // Validate if the InputStream is closed
    }
}