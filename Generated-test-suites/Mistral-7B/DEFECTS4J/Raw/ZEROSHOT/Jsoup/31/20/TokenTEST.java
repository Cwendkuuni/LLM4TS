import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());

        Token doctype2 = new Token.Doctype();
        doctype2.asDoctype().getName().append("html");
        assertEquals("Doctype[html]", doctype2.tokenType());
        assertEquals("html", doctype2.asDoctype().getName());
        assertNull(doctype2.asDoctype().getPublicIdentifier());
        assertNull(doctype2.asDoctype().getSystemIdentifier());
        assertFalse(doctype2.asDoctype().isForceQuirks());

        Token doctype3 = new Token.Doctype();
        doctype3.asDoctype().getName().append("html");
        doctype3.asDoctype().getPublicIdentifier().append("public");
        doctype3.asDoctype().setForceQuirks(true);
        assertEquals("Doctype[html][public][#force-quirks]", doctype3.tokenType());
        assertEquals("html", doctype3.asDoctype().getName());
        assertEquals("public", doctype3.asDoctype().getPublicIdentifier());
        assertTrue(doctype3.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());
        assertNull(tag.asStartTag().name());
        assertFalse(tag.isSelfClosing());
        assertNull(tag.asStartTag().getAttributes());

        Token tag2 = new Token.StartTag("p");
        assertEquals("StartTag[p]", tag2.tokenType());
        assertEquals("p", tag2.asStartTag().name());
        assertFalse(tag2.isSelfClosing());
        assertNull(tag2.asStartTag().getAttributes());

        Token tag3 = new Token.StartTag("p", new Token.Attributes());
        assertEquals("StartTag[p]", tag3.tokenType());
        assertEquals("p", tag3.asStartTag().name());
        assertFalse(tag3.isSelfClosing());
        assertNotNull(tag3.asStartTag().getAttributes());

        Token tag4 = new Token.StartTag("img", new Token.Attributes().put("src", "image.jpg"));
        assertEquals("StartTag[img][src=image.jpg]", tag4.tokenType());
        assertEquals("img", tag4.asStartTag().name());
        assertFalse(tag4.isSelfClosing());
        assertNotNull(tag4.asStartTag().getAttributes());
        assertEquals("image.jpg", tag4.asStartTag().getAttributes().get("src"));

        Token tag5 = new Token.EndTag("p");
        assertEquals("EndTag[p]", tag5.tokenType());
        assertEquals("p", tag5.asEndTag().name());
        assertFalse(tag5.isSelfClosing());
        assertNull(tag5.asEndTag().getAttributes());

        Token tag6 = new Token.EndTag("p");
        tag6.asEndTag().name("div");
        assertEquals("EndTag[div]", tag6.tokenType());
        assertEquals("div", tag6.asEndTag().name());
        assertFalse(tag6.isSelfClosing());
        assertNull(tag6.asEndTag().getAttributes());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        assertNull(comment.asComment().getData());

        Token comment2 = new Token.Comment();
        comment2.asComment().getData().append("This is a comment");
        assertEquals("Comment[This is a comment]", comment2.tokenType());
        assertEquals("This is a comment", comment2.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&nbsp;");
        assertEquals("Character[&nbsp;]", character.tokenType());
        assertEquals("&nbsp;", character.asCharacter().getData());

        Token character2 = new Token.Character('\u00A9');
        assertEquals("Character[\u00A9]", character2.tokenType());
        assertEquals("\u00A9", character2.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }
}