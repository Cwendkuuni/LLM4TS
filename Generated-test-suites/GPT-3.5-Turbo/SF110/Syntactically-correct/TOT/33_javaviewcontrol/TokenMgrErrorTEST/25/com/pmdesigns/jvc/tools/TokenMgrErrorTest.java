
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    private TokenMgrError tokenMgrError;

    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }

    @Test
    public void getMessage_ShouldReturnSuperClassMessage_WhenSuperClassHasMessage() {
        // Arrange
        String message = "Test Message";
        tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);

        // Act
        String result = tokenMgrError.getMessage();

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void getMessage_ShouldReturnNull_WhenSuperClassHasNoMessage() {
        // Arrange

        // Act
        String result = tokenMgrError.getMessage();

        // Assert
        assertNull(result);
    }

    @Test
    public void constructor_ShouldSetErrorCode_WhenReasonIsProvided() {
        // Arrange
        String message = "Test Message";
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;

        // Act
        tokenMgrError = new TokenMgrError(message, reason);

        // Assert
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void LexicalError_ShouldReturnErrorMessage_WhenParametersAreProvided() {
        // Arrange
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "Test";
        char curChar = 'a';

        String expectedErrorMessage = "Lexical error at line 5, column 10.  Encountered: <EOF>  after : \"Test\"";

        // Act
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);

        // Assert
        assertEquals(expectedErrorMessage, result);
    }

}
