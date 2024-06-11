import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        long lineNumber = lexer.getLineNumber();

        // Assert
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape_EscapeCharacterIsBackslash_ReturnsCharValue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        int result = lexer.readEscape();

        // Assert
        Assert.assertEquals('\\', result);
    }

    @Test
    public void testReadEscape_EscapeCharacterNotRecognized_ReturnsEndOfStream() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        reader.setLastChar('x');

        // Act
        int result = lexer.readEscape();

        // Assert
        Assert.assertEquals(Lexer.END_OF_STREAM, result);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EndOfStreamReached_ThrowsIOException() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        reader.setLastChar(Lexer.END_OF_STREAM);

        // Act
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_BufferWithTrailingSpaces_TrailingSpacesRemoved() {
        // Arrange
        StringBuilder buffer = new StringBuilder("test   ");
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        lexer.trimTrailingSpaces(buffer);

        // Assert
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_CIsLF_ReturnsTrue() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.readEndOfLine(Lexer.LF);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CIsCR_ReturnsTrue() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.readEndOfLine(Lexer.CR);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CIsCRFollowedByLF_ReturnsTrue() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.readEndOfLine(Lexer.CR);
        result = lexer.readEndOfLine(Lexer.LF);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespace_CIsSpace_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isWhitespace(' ');

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_CIsCR_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isStartOfLine(Lexer.CR);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_CIsEndOfStream_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        boolean result = lexer.isEndOfFile(Lexer.END_OF_STREAM);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDelimiter_CIsDelimiter_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        format.setDelimiter(',');

        // Act
        boolean result = lexer.isDelimiter(',');

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEscape_CIsEscape_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        format.setEscape('\\');

        // Act
        boolean result = lexer.isEscape('\\');

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_CIsQuoteChar_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        format.setQuoteChar('"');

        // Act
        boolean result = lexer.isQuoteChar('"');

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCommentStart_CIsCommentStart_ReturnsTrue() {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        format.setCommentStart("#");

        // Act
        boolean result = lexer.isCommentStart('#');

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testNextToken_TokenNotNull_ReturnsToken() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);
        Token token = new Token();

        // Act
        Token result = lexer.nextToken(token);

        // Assert
        Assert.assertEquals(token, result);
    }

    @Test
    public void testNextToken_TokenNull_ReturnsNewToken() throws IOException {
        // Arrange
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, reader);

        // Act
        Token result = lexer.nextToken(null);

        // Assert
        Assert.assertNotNull(result);
    }

}