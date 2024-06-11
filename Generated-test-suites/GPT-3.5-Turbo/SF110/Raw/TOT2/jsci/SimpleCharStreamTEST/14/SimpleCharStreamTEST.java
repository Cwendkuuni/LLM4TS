import org.junit.*;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {
    
    @Test
    public void testBeginToken() {
        // Test typical usage
        SimpleCharStream.BeginToken();
        assertEquals(0, SimpleCharStream.getColumn());
        assertEquals(1, SimpleCharStream.getLine());
        
        // Test edge case - empty stream
        // It should throw an IOException
        try {
            SimpleCharStream.BeginToken();
            fail("IOException not thrown for empty stream");
        } catch (IOException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadChar() {
        // Test typical usage
        char c = 0;
        try {
            c = SimpleCharStream.readChar();
        } catch (IOException e) {
            fail("IOException thrown unexpectedly");
        }
        assertEquals(c, SimpleCharStream.buffer[SimpleCharStream.bufpos]);
        
        // Test edge case - end of stream
        // It should fill the buffer again
        for (int i = 0; i < SimpleCharStream.bufsize; i++) {
            try {
                c = SimpleCharStream.readChar();
            } catch (IOException e) {
                fail("IOException thrown unexpectedly");
            }
        }
    }
    
    @Test
    public void testGetImage() {
        // Test typical usage
        String image = SimpleCharStream.GetImage();
        assertNotNull(image);
        
        // Test edge case - empty buffer
        // It should return an empty string
        SimpleCharStream.bufpos = -1;
        assertEquals("", SimpleCharStream.GetImage());
    }
    
    // Add more test methods here for other public methods
    
}