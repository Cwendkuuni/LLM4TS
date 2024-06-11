import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = new byte[]{65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream input = new BaseNCodecInputStream(testInput, baseNCodec, false);
        
        int result = input.read();
        
        assertEquals(65, result); // 'A' should be read
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] testData = new byte[]{65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream input = new BaseNCodecInputStream(testInput, baseNCodec, false);
        
        byte[] buffer = new byte[2];
        int result = input.read(buffer, 0, 2);
        
        assertEquals(2, result); // 2 bytes should be read
        assertEquals(65, buffer[0]); // 'A' should be at index 0
        assertEquals(66, buffer[1]); // 'B' should be at index 1
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);
        
        assertFalse(input.markSupported()); // Mark is not supported
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSkipLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);

        input.skip(-1); // Should throw IllegalArgumentException
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream testInput = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream input = new BaseNCodecInputStream(testInput, baseNCodec, false);
        
        int result = input.available();
        
        assertEquals(0, result); // No data available
    }
}