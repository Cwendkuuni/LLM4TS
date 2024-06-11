import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName = "html";
        doctypeToken.asDoctype().pubSysKey = "DTD HTML";
        doctypeToken.asDoctype().publicIdentifier = "public \"-//W3C//DTD HTML 4.01//EN\"";
        doctypeToken.asDoctype().systemIdentifier = "/HTML.dtd";
        doctypeToken.asDoctype().forceQuirks = true;

        Token resetToken = doctypeToken.reset();
        assertEquals(TokenType.Doctype, resetToken.type);
        assertNull(resetToken.getName());
        assertNull(resetToken.asDoctype().pubSysKey);
        assertNull(resetToken.asDoctype().publicIdentifier);
        assertNull(resetToken.asDoctype().systemIdentifier);
        assertFalse(resetToken.asDoctype().isForceQuirks());

        Token expectedDoctypeToken = new Token.Doctype();
        expectedDoctypeToken.asDoctype().getName = "html";
        expectedDoctypeToken.asDoctype().pubSysKey = "DTD HTML";
        expectedDoctypeToken.asDoctype().publicIdentifier = "public \"-//W3C//DTD HTML 4.01//EN\"";
        expectedDoctypeToken.asDoctype().systemIdentifier = "/HTML.dtd";
        expectedDoctypeToken.asDoctype().forceQuirks = true;

        assertEquals(expectedDoctypeToken, doctypeToken);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("html");
        startTagToken.asStartTag().attributes = new Attributes();
        startTagToken.asStartTag().attributes.put(new Attribute("class", "mainPage"));

        Token resetToken = startTagToken.reset();
        assertEquals(TokenType.StartTag, resetToken.type);
        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
        assertNull(resetToken.pendingAttributeName);
        assertNull(resetToken.pendingAttributeValue);
        assertNull(resetToken.pendingAttributeValueS);
        assertFalse(resetToken.hasEmptyAttributeValue);
        assertFalse(resetToken.hasPendingAttributeValue);
        assertFalse(resetToken.selfClosing);
        assertNull(resetToken.attributes);

        Token expectedStartTagToken = new Token.StartTag();
        expectedStartTagToken.asStartTag().name("html");
        expectedStartTagToken.asStartTag().attributes = new Attributes();
        expectedStartTagToken.asStartTag().attributes.put(new Attribute("class", "mainPage"));

        assertEquals(expectedStartTagToken, startTagToken);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("html");

        Token resetToken = endTagToken.reset();
        assertEquals(TokenType.EndTag, resetToken.type);
        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
        assertNull(resetToken.pendingAttributeName);
        assertNull(resetToken.pendingAttributeValue);
        assertNull(resetToken.pendingAttributeValueS);
        assertFalse(resetToken.hasEmptyAttributeValue);
        assertFalse(resetToken.hasPendingAttributeValue);
        assertFalse(resetToken.selfClosing);
        assertNull(resetToken.attributes);

        Token expectedEndTagToken = new Token.EndTag();
        expectedEndTagToken.name("html");

        assertEquals(expectedEndTagToken, endTagToken);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().data = "-- This is a comment";

        Token resetToken = commentToken.reset();
        assertEquals(TokenType.Comment, resetToken.type);
        assertNull(resetToken.data);
        assertFalse(resetToken.bogus);

        Token expectedCommentToken = new Token.Comment();
        expectedCommentToken.asComment().data = "-- This is a comment";

        assertEquals(expectedCommentToken, commentToken);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.type = null;
        invalidToken.reset();
    }
}