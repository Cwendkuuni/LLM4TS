import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setup() {
        format = new CSVFormatBuilder()
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true)
                .build();
        mockReader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testNextToken_EmptyLine() throws IOException {
        // Arrange: empty line
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(null);

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: end of stream
        assertNull(token);
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
    }

    @Test
    public void testNextToken_Whitespace() throws IOException {
        // Arrange: whitespace char
        ByteArrayInputStream in = new ByteArrayInputStream(" \n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: whitespace token
        assertNotNull(token);
        assertTrue(token.isWhitespace());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_Delimiter() throws IOException {
        // Arrange: delimiter char
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{delimiter});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: delimiter token
        assertNotNull(token);
        assertTrue(token.isDelimiter());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_Escape() throws IOException {
        // Arrange: escape character followed by a newline
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{'\\', CR, LF});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: empty token
        assertNotNull(token);
        assertTrue(token.isEndOfLine());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
        Mockito.verify(mockReader, Mockito.times(1)).read();
        Mockito.verify(mockReader, Mockito.times(1)).read();
    }

    @Test
    public void testNextToken_QuoteChar() throws IOException {
        // Arrange: quote character
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{quoteChar});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: quote char token
        assertNotNull(token);
        assertTrue(token.isQuoteChar());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_Comment() throws IOException {
        // Arrange: comment character
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{commmentStart});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: comment token
        assertNotNull(token);
        assertTrue(token.isCommentStart());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_Data() throws IOException {
        // Arrange: data with no escapes or quotes
        ByteArrayInputStream in = new ByteArrayInputStream("data".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: data token
        assertNotNull(token);
        assertEquals("data", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_DataWithEscapes() throws IOException {
        // Arrange: data with escape sequences
        ByteArrayInputStream in = new ByteArrayInputStream("data\\r\\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: empty token (as we consume the escape sequences)
        assertNotNull(token);
        assertTrue(token.isEndOfLine());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
        Mockito.verify(mockReader, Mockito.times(2)).read();
    }

    @Test
    public void testNextToken_DataWithQuotes() throws IOException {
        // Arrange: data with quotes
        ByteArrayInputStream in = new ByteArrayInputStream("\"data\".toString()".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: quoted data token
        assertNotNull(token);
        assertEquals("data", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_DataWithQuotesAndEscapes() throws IOException {
        // Arrange: data with quotes and escape sequences
        ByteArrayInputStream in = new ByteArrayInputStream("\"data\\r\\n\".toString()".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: empty token (as we consume the quote and escape sequences)
        assertNotNull(token);
        assertTrue(token.isEndOfLine());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
        Mockito.verify(mockReader, Mockito.times(3)).read();
    }

    @Test
    public void testNextToken_EmptyString() throws IOException {
        // Arrange: empty string
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: empty string token
        assertNotNull(token);
        assertTrue(token.isWhitespace());
        assertEquals("", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_IgnoreSurroundingSpaces() throws IOException {
        // Arrange: data with surrounding spaces
        ByteArrayInputStream in = new ByteArrayInputStream("data   ".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: data token
        assertNotNull(token);
        assertEquals("data", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).consume();
    }

    @Test
    public void testNextToken_IgnoreEmptyLines() throws IOException {
        // Arrange: empty line
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Mockito.when(mockReader.readLine()).thenReturn(new String(in.readAllBytes()));

        // Act: read next token
        Token token = lexer.nextToken(null);

        // Assert: null token (end of stream)
        assertNull(token);
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
    }
}