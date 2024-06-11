import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character("test");
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Name");
        assertEquals("Test Name", doctype.getName());
    }

    // Add more test methods for other public methods

    // Add edge cases and exception handling test cases

}