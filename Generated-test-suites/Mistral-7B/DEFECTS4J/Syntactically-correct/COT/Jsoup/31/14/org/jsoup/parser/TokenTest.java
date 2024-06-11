import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctype_basic() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier("_public");
        assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier("system://localhost/index.html");
        assertEquals("system://localhost/index.html", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_invalid_name() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("!");
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_empty_publicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier("");
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_empty_systemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier("");
    }

    @Test
    public void testTag_basic() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());
    }

    @Test
    public void testTag_name() {
        Token tag = new Token.StartTag();
        tag.name("div");
        assertEquals("div", tag.asStartTag().name());
    }

    @Test
    public void testTag_isSelfClosing() {
        Token tag = new Token.StartTag();
        tag.isSelfClosing(true);
        assertTrue(tag.asStartTag().isSelfClosing());
    }

    @Test
    public void testTag_attributes() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        assertEquals("class", tag.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testTag_finaliseTag() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.finaliseTag();
        assertNull(tag.asStartTag().pendingAttributeName);
    }

    @Test
    public void testTag_toString() {
        Token tag = new Token.StartTag("div");
        assertEquals("<div>", tag.toString());
    }

    @Test
    public void testTag_toString_withAttributes() {
        Token tag = new Token.StartTag("div").attributes().put("class", "test");
        assertEquals("<div class=\"test\">", tag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testTag_invalid_name() {
        Token tag = new Token.StartTag();
        tag.name("!");
    }

    @Test
    public void testComment_basic() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
    }

    @Test
    public void testComment_getData() {
        Token comment = new Token.Comment();
        comment.data("This is a comment.");
        assertEquals("This is a comment.", comment.asComment().getData());
    }

    @Test
    public void testComment_toString() {
        Token comment = new Token.Comment();
        comment.data("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacter_basic() {
        Token character = new Token.Character("a");
        assertEquals("Character", character.tokenType());
    }

    @Test
    public void testCharacter_getData() {
        Token character = new Token.Character("a");
        assertEquals("a", character.asCharacter().getData());
    }

    @Test
    public void testCharacter_toString() {
        Token character = new Token.Character('a');
        assertEquals("a", character.toString());
    }

    @Test
    public void testEOF_basic() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testToken_isDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testToken_isStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testToken_isEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testToken_isComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testToken_isCharacter() {
        Token token = new Token.Character("a");
        assertTrue(token.isCharacter());
    }

    @Test
    public void testToken_isEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testToken_asDoctype() {
        Token token = new Token.Doctype();
        assertSame(token, token.asDoctype());
    }

    @Test
    public void testToken_asStartTag() {
        Token token = new Token.StartTag();
        assertSame(token, token.asStartTag());
    }

    @Test
    public void testToken_asEndTag() {
        Token token = new Token.EndTag();
        assertSame(token, token.asEndTag());
    }

    @Test
    public void testToken_asComment() {
        Token token = new Token.Comment();
        assertSame(token, token.asComment());
    }

    @Test
    public void testToken_asCharacter() {
        Token token = new Token.Character("a");
        assertSame(token, token.asCharacter());
    }

    @Test
    public void testToken_asEOF() {
        Token token = new Token.EOF();
        assertSame(token, token.asEOF());
    }
}