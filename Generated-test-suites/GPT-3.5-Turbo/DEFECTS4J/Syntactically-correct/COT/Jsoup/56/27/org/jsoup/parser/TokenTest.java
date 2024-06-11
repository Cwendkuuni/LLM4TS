import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        String expected = "Token";
        String actual = token.tokenType();
        
        assertEquals(expected, actual);
    }
}