import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
        };

        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();

        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();

        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();

        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myDiv");
        startTag.attributes.put("class", "container");

        assertEquals("<div id=\"myDiv\" class=\"container\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "p";
        assertEquals("p", endTag.name());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";

        assertEquals("</p>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Hello World");

        assertEquals("Hello World", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("Hello World");

        assertEquals("Hello World", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("Hello World");

        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("Hello World");

        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

}