import org.junit.*;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() {
        // Test normal use case
        char expectedChar = 'a';
        SimpleCharStream.tokenBegin = 5;
        SimpleCharStream.bufpos = 10;
        char actualChar = SimpleCharStream.BeginToken();
        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testUpdateLineColumn() {
        // Test with '\r' character
        char testChar = '\r';
        SimpleCharStream.column = 5;
        SimpleCharStream.prevCharIsLF = false;
        SimpleCharStream.prevCharIsCR = false;
        SimpleCharStream.line = 3;
        SimpleCharStream.bufpos = 10;
        SimpleCharStream.UpdateLineColumn(testChar);
        assertEquals(6, SimpleCharStream.column);
        assertEquals(4, SimpleCharStream.line);
        assertTrue(SimpleCharStream.prevCharIsCR);
    }

    // Edge case testing for other public methods should go here

    // Exception handling scenarios for public methods should go here

}