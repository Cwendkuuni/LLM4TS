import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeCreation() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
    }

    // Test other methods of Doctype as needed
}