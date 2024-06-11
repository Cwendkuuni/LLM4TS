import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new ConcreteLexer();
        char result = lexer.mapNullToDisabled(null);
        assertEquals('\ufffe', result);
        
        char result2 = lexer.mapNullToDisabled('c');
        assertEquals('c', result2);
    }

    @Test
    public void testReadEscape() {
        // Test normal escape characters
        Lexer lexer = new ConcreteLexer();
        // Assume backslash is the escape character
        in.setLastChar('\\');
        in.setNextChar('n');
        int result = lexer.readEscape();
        assertEquals('\n', result);
        
        in.setLastChar('\\');
        in.setNextChar('t');
        int result2 = lexer.readEscape();
        assertEquals('\t', result2);
        
        // Test handling invalid escape characters
        in.setLastChar('\\');
        in.setNextChar('x');
        assertThrows(IOException.class, () -> {
            lexer.readEscape();
        });
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new ConcreteLexer();
        StringBuilder buffer = new StringBuilder("  test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("  test", buffer.toString());
        
        buffer.setLength(0);
        buffer.append("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    // Add more test cases for other methods in Lexer class

}