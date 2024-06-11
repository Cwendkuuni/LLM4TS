import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {};
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
        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
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
        startTag.attributes.put("class", "container");
        assertEquals("<div class=\"container\">", startTag.toString());
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
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
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
        Token.Character character = new Token.Character("Some data");
        assertEquals("Some data", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("Some data");
        assertEquals("Some data", character.toString());
    }

    @Test
    public void testEOFisEOF() {
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
        Token token = doctype;
        assertEquals(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertEquals(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertEquals(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertEquals(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("Some data");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("Some data");
        Token token = character;
        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}