
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
    public void getMessage_WithNullMessage_ReturnsNull() {
        assertNull(tokenMgrError.getMessage());
    }

    @Test
    public void getMessage_WithMessage_ReturnsMessage() {
        String message = "Test Message";
        TokenMgrError tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, tokenMgrError.getMessage());
    }
}
