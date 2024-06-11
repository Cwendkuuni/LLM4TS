
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        
        // Act
        long lineNumber = lexer.getLineNumber();
        
        // Assert
        assertThat(lineNumber).isEqualTo(0);
    }

    @Test
    public void testReadEscape_validEscapeSequence() throws IOException {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\\n"));
        lexer.setIn(in);
        
        // Act
        int escapedCharacter = lexer.readEscape();
        
        // Assert
        assertThat(escapedCharacter).isEqualTo('\n');
    }

    @Test(expected = IOException.class)
    public void testReadEscape_invalidEscapeSequence() throws IOException {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\\m"));
        lexer.setIn(in);
        
        // Act
        lexer.readEscape();
        
        // Expect IOException
    }

    @Test
    public void testTrimTrailingSpaces_noTrailingSpaces() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        StringBuilder buffer = new StringBuilder("abc");
        
        // Act
        lexer.trimTrailingSpaces(buffer);
        
        // Assert
        assertThat(buffer.toString()).isEqualTo("abc");
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\n"));
        lexer.setIn(in);
        
        // Act
        boolean endOfLine = lexer.readEndOfLine();
        
        // Assert
        assertThat(endOfLine).isTrue();
    }

    @Test
    public void testIsStartOfLine_startOfLine() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = UNDEFINED;
        
        // Act
        boolean startOfLine = lexer.isStartOfLine(c);
        
        // Assert
        assertThat(startOfLine).isTrue();
    }

    @Test
    public void testIsEndOfFile_endOfFile() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = END_OF_STREAM;
        
        // Act
        boolean endOfFile = lexer.isEndOfFile(c);
        
        // Assert
        assertThat(endOfFile).isTrue();
    }

    @Test
    public void testIsDelimiter_delimiter() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = ',';
        
        // Act
        boolean isDelimiter = lexer.isDelimiter(c);
        
        // Assert
        assertThat(isDelimiter).isTrue();
    }

    @Test
    public void testIsEscape_escapeCharacter() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = '\\';
        
        // Act
        boolean isEscape = lexer.isEscape(c);
        
        // Assert
        assertThat(isEscape).isTrue();
    }

    @Test
    public void testIsQuoteChar_quoteCharacter() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = '\"';
        
        // Act
        boolean isQuoteChar = lexer.isQuoteChar(c);
        
        // Assert
        assertThat(isQuoteChar).isTrue();
    }

    @Test
    public void testIsCommentStart_commentStartCharacter() {
        // Arrange
        Lexer lexer = new Lexer(null, null);
        int c = '#';
        
        // Act
        boolean isCommentStart = lexer.isCommentStart(c);
        
        // Assert
        assertThat(isCommentStart).isTrue();
    }
}
