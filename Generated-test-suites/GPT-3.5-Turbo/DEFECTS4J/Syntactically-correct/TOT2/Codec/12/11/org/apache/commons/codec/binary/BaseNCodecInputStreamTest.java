import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    // Test public int read()
    @Test
    public void testRead() throws IOException {
        byte[] testData = {65, 66, 67}; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertEquals(65, inputStream.read());
        assertEquals(66, inputStream.read());
        assertEquals(67, inputStream.read());
        assertEquals(-1, inputStream.read()); // EOF
    }
    
    // Test public int read(byte b[], int offset, int len)
    @Test
    public void testReadByteArrayOffsetLen() throws IOException {
        byte[] testData = {68, 69, 70}; // ASCII values for 'D', 'E', 'F'
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, true);
        
        byte[] output = new byte[6]; // Larger array than testData
        assertEquals(3, inputStream.read(output, 0, 3));
        assertArrayEquals(new byte[]{68, 69, 70, 0, 0, 0}, output);
        
        // Test exception cases
        try {
            inputStream.read(null, 0, 5);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        try {
            inputStream.read(new byte[3], -1, 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // Test public boolean markSupported()
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertFalse(inputStream.markSupported());
    }

    // Test public int available()
    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, codec, false);
        
        assertEquals(0, inputStream.available());
    }
}