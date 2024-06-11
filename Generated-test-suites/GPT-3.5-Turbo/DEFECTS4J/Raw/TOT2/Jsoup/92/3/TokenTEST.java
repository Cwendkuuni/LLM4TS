import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "Key";
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");

        assertEquals("div", tag.tagName);
        assertEquals("div", tag.normalName);
    }

    @Test
    public void testAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("id");

        assertEquals("id", tag.pendingAttributeName);
    }

    // Additional test cases for other methods should be included here

}