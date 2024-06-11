import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctypeConstructor() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
    }
}