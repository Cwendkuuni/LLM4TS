import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() {
        // Typical use case
        assertEquals('a', SimpleCharStream.BeginToken());
        
        // Edge case
        assertEquals('b', SimpleCharStream.BeginToken());
        
        // Exception handling
        try {
            SimpleCharStream.FillBuff();
            SimpleCharStream.BeginToken();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadChar() {
        // Typical use case
        assertEquals('c', SimpleCharStream.readChar());
        
        // Edge case
        assertEquals('d', SimpleCharStream.readChar());
        
        // Exception handling
        try {
            while (SimpleCharStream.getColumn() > 0) {
                SimpleCharStream.readChar();
            }
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetEndColumn() {
        // Typical use case
        assertEquals(5, SimpleCharStream.getEndColumn());
        
        // Edge case
        assertEquals(0, SimpleCharStream.getEndColumn());
    }
}