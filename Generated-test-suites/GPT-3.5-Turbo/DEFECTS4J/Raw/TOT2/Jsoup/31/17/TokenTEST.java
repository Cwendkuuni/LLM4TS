import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character("test");
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getName());
    }

    // Additional test cases can be added for the remaining methods

}