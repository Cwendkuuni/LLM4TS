import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.reset();

        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");

        assertEquals("html", doctypeToken.getName());

        doctypeToken.setForceQuirks(true);

        assertTrue(doctypeToken.isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        assertEquals("StartTag", startTagToken.tokenType());

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);

        startTagToken.newAttribute();

        assertNotNull(startTagToken.attributes);

        startTagToken.name("div");

        assertEquals("div", startTagToken.name());

        startTagToken.name("div").name("class").value("test");

        assertEquals("div", startTagToken.name());
        assertEquals("class", startTagToken.pendingAttributeName);
        assertEquals("test", startTagToken.pendingAttributeValue.toString());

        startTagToken.newAttribute();

        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);

        startTagToken.selfClosing(true);

        assertTrue(startTagToken.isSelfClosing());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        assertEquals("EndTag", endTagToken.tokenType());

        endTagToken.reset();

        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);

        endTagToken.name("div");

        assertEquals("div", endTagToken.name());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        assertEquals("Comment", commentToken.tokenType());

        commentToken.reset();

        assertNull(commentToken.getData());
        assertFalse(commentToken.isBogus());

        commentToken.data("This is a comment.");

        assertEquals("This is a comment.", commentToken.getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        assertEquals("Character", characterToken.tokenType());

        characterToken.reset();

        assertNull(characterToken.getData());

        characterToken.data("A");

        assertEquals("A", characterToken.getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType());

        eofToken.reset();

        assertTrue(eofToken.isEOF());
    }
}