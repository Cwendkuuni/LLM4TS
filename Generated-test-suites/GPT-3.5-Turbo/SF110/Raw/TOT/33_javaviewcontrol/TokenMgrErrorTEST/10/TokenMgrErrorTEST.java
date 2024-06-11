import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage_WithMessage_ReturnsMessage() {
        // Arrange
        TokenMgrError error = new TokenMgrError("Sample error message", TokenMgrError.LEXICAL_ERROR);

        // Act
        String message = error.getMessage();

        // Assert
        assertEquals("Sample error message", message);
    }

    @Test
    public void testGetMessage_NoMessage_ReturnsNull() {
        // Arrange
        TokenMgrError error = new TokenMgrError();

        // Act
        String message = error.getMessage();

        // Assert
        assertNull(message);
    }

    @Test
    public void testConstructor_WithMessageAndReason_SetsMessageAndReason() {
        // Arrange & Act
        TokenMgrError error = new TokenMgrError("Sample error message", TokenMgrError.LEXICAL_ERROR);

        // Assert
        assertEquals("Sample error message", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test
    public void testConstructor_NoMessageAndReason_SetsNullMessageAndZeroReason() {
        // Arrange & Act
        TokenMgrError error = new TokenMgrError();

        // Assert
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructor_WithLexicalError_ReturnsFormattedError() {
        // Arrange & Act
        TokenMgrError error = new TokenMgrError(false, 1, 2, 3, "errorAfter", 'A', TokenMgrError.LEXICAL_ERROR);

        // Assert
        String expectedMessage = "Lexical error at line 2, column 3. Encountered: \"A\" (65), after : \"errorAfter\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test
    public void testAddEscapes_WithEscapableCharacters_ReturnsEscapedString() {
        // Arrange & Act
        String escapedString = TokenMgrError.addEscapes("\t\n\r\"'\\\b\f");

        // Assert
        assertEquals("\\t\\n\\r\\\"\\'\\\\\\b\\f", escapedString);
    }

    @Test
    public void testAddEscapes_WithNonEscapableCharacters_ReturnsOriginalString() {
        // Arrange & Act
        String originalString = "abc123";
        String escapedString = TokenMgrError.addEscapes(originalString);

        // Assert
        assertEquals(originalString, escapedString);
    }
}