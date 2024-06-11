import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {

    @Test
    public void testDoctype_basic() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
    }

    @Test
    public void testDoctype_getName_basic() {
        Token doctypeToken = new Token.Doctype();
        Token doctypeTokenWithName = new Token.Doctype().name("html");
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier_basic() {
        Token doctypeToken = new Token.Doctype();
        Token doctypeTokenWithPublicIdentifier = new Token.Doctype().name("html").publicIdentifier("_public");
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("_public", doctypeTokenWithPublicIdentifier.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier_basic() {
        Token doctypeToken = new Token.Doctype();
        Token doctypeTokenWithSystemIdentifier = new Token.Doctype().name("html").systemIdentifier("_system");
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("_system", doctypeTokenWithSystemIdentifier.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks_basic() {
        Token doctypeToken = new Token.Doctype();
        Token doctypeTokenWithForceQuirks = new Token.Doctype().forceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals(false, doctypeToken.asDoctype().isForceQuirks());
        assertEquals(true, doctypeTokenWithForceQuirks.asDoctype().isForceQuirks());
    }

    // Add test cases for other methods and edge cases in the class here

    // StartTag tests

    @Test
    public void testStartTag_basic() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());
    }

    @Test
    public void testStartTag_name_basic() {
        Token startTagToken = new Token.StartTag().name("div");
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.asStartTag().name());
    }

    @Test
    public void testStartTag_selfClosing_basic() {
        Token startTagToken = new Token.StartTag().name("img").selfClosing(true);
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals(true, startTagToken.asStartTag().isSelfClosing());
    }

    @Test
    public void testStartTag_getAttributes_basic() {
        Token startTagToken = new Token.StartTag().name("input").attributes(new Token.Attributes().put("type", "text"));
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("input", startTagToken.asStartTag().name());
        assertEquals("text", startTagToken.asStartTag().getAttributes().get("type").toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTag_newAttribute_withNullName() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().newAttribute(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTag_newAttribute_withEmptyName() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().newAttribute("");
    }

    // EndTag tests

    @Test
    public void testEndTag_basic() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());
    }

    @Test
    public void testEndTag_name_basic() {
        Token endTagToken = new Token.EndTag().name("div");
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("div", endTagToken.asEndTag().name());
    }

    @Test
    public void testEndTag_toString_basic() {
        Token endTagToken = new Token.EndTag().name("p");
        assertEquals("</p>", endTagToken.toString());
    }

    // Comment tests

    @Test
    public void testComment_basic() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
    }

    @Test
    public void testComment_getData_basic() {
        Token commentToken = new Token.Comment("This is a comment");
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment", commentToken.asComment().getData());
    }

    @Test
    public void testComment_toString_basic() {
        Token commentToken = new Token.Comment("<!-- This is a comment -->");
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("<!--This is a comment -->", commentToken.toString());
    }

    // Character tests

    @Test
    public void testCharacter_basic() {
        Token characterToken = new Token.Character('A');
        assertEquals("Character", characterToken.tokenType());
        assertEquals('A', characterToken.asCharacter().getData().charAt(0));
    }

    @Test
    public void testCharacter_toString_basic() {
        Token characterToken = new Token.Character('&');
        assertEquals("Character", characterToken.tokenType());
        assertEquals("&", characterToken.toString());
    }

    // EOF tests

    @Test
    public void testEOF_basic() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
    }
}