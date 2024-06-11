import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;

public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new ConcreteLexer();
        assertEquals('\ufffe', lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testReadEscape() {
        Lexer lexer = new ConcreteLexer();
        // Test different escape characters
        assertEquals(Lexer.CR, lexer.readEscape('r'));
        assertEquals(Lexer.LF, lexer.readEscape('n'));
        assertEquals(Lexer.TAB, lexer.readEscape('t'));
        assertEquals(Lexer.BACKSPACE, lexer.readEscape('b'));
        assertEquals(Lexer.FF, lexer.readEscape('f'));
        // Test invalid escape char
        assertEquals(Lexer.END_OF_STREAM, lexer.readEscape('x'));
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new ConcreteLexer();
        StringBuilder buffer = new StringBuilder("  test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("  test", buffer.toString());
    }

    // Add more test methods for other functionalities of the Lexer class

}