import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Name");
        doctype.pubSysKey = "Test PubSysKey";
        doctype.publicIdentifier.append("Test Public Identifier");
        doctype.systemIdentifier.append("Test System Identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.selfClosing = true;

        assertEquals("test", startTag.name());
        assertTrue(startTag.isSelfClosing());
    }

    // Additional tests for other methods in Token class can be added here

}