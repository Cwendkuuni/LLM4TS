import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        // Given
        Token doctypeToken = new Token.Doctype();

        // When
        String type = doctypeToken.tokenType();

        // Then
        assertEquals("Doctype", type);
    }

    // Edge cases and exception handling scenarios can be tested separately in other test methods
}