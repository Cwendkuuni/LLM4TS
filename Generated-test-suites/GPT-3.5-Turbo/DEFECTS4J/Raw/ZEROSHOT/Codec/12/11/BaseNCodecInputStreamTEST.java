import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setUp() {
        baseNCodecInputStream = new BaseNCodecInputStream(null, null, false);
    }
    
    @Test
    public void testRead() {
        try {
            int result = baseNCodecInputStream.read();
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testReadWithByteArrayAndOffsetAndLen() {
        try {
            byte[] b = new byte[10];
            int result = baseNCodecInputStream.read(b, 0, 10);
            assertEquals(0, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] b = new byte[10];
        baseNCodecInputStream.read(b, -1, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] b = new byte[10];
        baseNCodecInputStream.read(b, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] b = new byte[10];
        baseNCodecInputStream.read(b, 11, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] b = new byte[10];
        baseNCodecInputStream.read(b, 5, 6);
    }
    
    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testSkip() {
        try {
            baseNCodecInputStream.skip(512);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test
    public void testAvailable() {
        int result = baseNCodecInputStream.available();
        assertEquals(1, result);
    }
    
}