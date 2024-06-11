import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private Lexer lexer;

    @Before
    public void setUp() throws IOException {
        final CSVFormat format = new CSVFormat();
        this.mockReader = Mockito.mock(ExtendedBufferedReader.class);
        this.lexer = new Lexer(format, this.mockReader);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        // Given: an empty line
        final String input = "\n";
        Mockito.when(this.mockReader.readLine()).thenReturn(input);

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: end of stream is returned
        assertNull(token);
        Mockito.verify(this.mockReader).readLine();
    }

    @Test
    public void testNextToken_whitespace() throws IOException {
        // Given: a whitespace character
        final String input = " \t";
        Mockito.when(this.mockReader.read()).thenReturn(input.charAt(0));

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: whitespace token is returned
        assertTrue(token.getType() == Token.Type.WHITESPACE);
        Mockito.verify(this.mockReader).read();
    }

    @Test
    public void testNextToken_delimiter() throws IOException {
        // Given: a delimiter character
        final String input = ",\n";
        Mockito.when(this.mockReader.read()).thenReturn(',').thenReturn(LF);

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: delimiter token is returned
        assertTrue(token.getType() == Token.Type.DELIMITER);
        Mockito.verify(this.mockReader).read();
        Mockito.verify(this.mockReader).read();
    }

    @Test
    public void testNextToken_escapeSequence() throws IOException {
        // Given: an escape sequence
        final String input = "\\r\n";
        Mockito.when(this.mockReader.read()).thenReturn('\\').thenReturn('r').thenReturn(CR).thenReturn(LF);

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: line terminator token is returned
        assertTrue(token.getType() == Token.Type.LINE_TERMINATOR);
        Mockito.verify(this.mockReader).read();
        Mockito.verify(this.mockReader).read();
        Mockito.verify(this.mockReader).read();
        Mockito.verify(this.mockReader).read();
    }

    @Test
    public void testNextToken_commentStart() throws IOException {
        // Given: a comment start character
        final String input = "%\n";
        Mockito.when(this.mockReader.read()).thenReturn('%');

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: comment start token is returned
        assertTrue(token.getType() == Token.Type.COMMENTSTART);
        Mockito.verify(this.mockReader).read();
    }

    @Test
    public void testNextToken_quotedString() throws IOException {
        // Given: a quoted string
        final String input = "\"Hello, World!\"\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(this.mockReader.readLine()).thenReturn(input);
        Mockito.when(this.mockReader.getCharacterStream()).thenReturn(new InputStreamReader(inputStream));

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: string token is returned
        assertEquals("Hello, World!", token.getValue());
        Mockito.verify(this.mockReader).readLine();
        Mockito.verify(this.mockReader).getCharacterStream();
    }

    @Test
    public void testNextToken_unquotedString() throws IOException {
        // Given: an unquoted string
        final String input = "Hello, World!\n";
        Mockito.when(this.mockReader.readLine()).thenReturn(input);

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: string token is returned
        assertEquals("Hello, World", token.getValue());
        Mockito.verify(this.mockReader).readLine();
    }

    @Test
    public void testNextToken_normalCharacter() throws IOException {
        // Given: a normal character
        final String input = "A\n";
        Mockito.when(this.mockReader.read()).thenReturn('A').thenReturn(LF);

        // When: reading next token
        Token token = this.lexer.nextToken(null);

        // Then: character token is returned
        assertEquals("A", token.getValue());
        Mockito.verify(this.mockReader).read();
        Mockito.verify(this.mockReader).read();
    }

    @Test(expected = IOException.class)
    public void testNextToken_readError() throws IOException {
        // Given: an error reading from the input stream
        final String input = "Error!";
        Mockito.when(this.mockReader.readLine()).thenThrow(new IOException(input));

        // When: reading next token
        this.lexer.nextToken(null);

        // Then: an IOException is thrown
    }
}