import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testTokenResetDoctype() {
        Token token = new Token.Doctype();
        token.asDoctype().name("html");
        token.asDoctype().systemIdentifier("test.html");
        token.asDoctype().forceQuirks(true);

        Token resetToken = token.reset();

        assertEquals("Doctype", resetToken.tokenType());
        assertNull(resetToken.asDoctype().getName());
        assertNull(resetToken.asDoctype().getPublicIdentifier());
        assertNull(resetToken.asDoctype().getSystemIdentifier());
        assertFalse(resetToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testTokenResetTag() {
        Token token = new Token.StartTag();
        token.name("div");
        token.asStartTag().attributes().put("class", "test");

        Token resetToken = token.reset();

        assertEquals("StartTag", resetToken.tokenType());
        assertNull(resetToken.name());
        assertNull(resetToken.asStartTag().getAttributes());
    }

    @org.junit.Test
    public void testTokenResetComment() {
        Token token = new Token.Comment();
        token.asComment().data("This is a comment.");

        Token resetToken = token.reset();

        assertEquals("Comment", resetToken.tokenType());
        assertNull(resetToken.asComment().getData());
    }

    @org.junit.Test
    public void testTokenResetCharacter() {
        Token token = new Token.Character();
        token.asCharacter().data("A");

        Token resetToken = token.reset();

        assertEquals("Character", resetToken.tokenType());
        assertNull(resetToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testTokenResetEOF() {
        Token token = new Token.EOF();

        Token resetToken = token.reset();

        assertEquals("EOF", resetToken.tokenType());
        assertSame(token, resetToken);
    }

    @org.junit.Test
    public void testTokenTypeDoctype() {
        Token token = new Token.Doctype();

        assertTrue(token.isDoctype());
    }

    @org.junit.Test
    public void testTokenTypeStartTag() {
        Token token = new Token.StartTag();

        assertTrue(token.isStartTag());
    }

    @org.junit.Test
    public void testTokenTypeEndTag() {
        Token token = new Token.EndTag();

        assertTrue(token.isEndTag());
    }

    @org.junit.Test
    public void testTokenTypeComment() {
        Token token = new Token.Comment();

        assertTrue(token.isComment());
    }

    @org.junit.Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();

        assertTrue(token.isCharacter());
    }

    @org.junit.Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();

        assertTrue(token.isEOF());
    }
}