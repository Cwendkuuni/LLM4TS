import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeSetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.getName().equals("html");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier().equals("");
        doctype.setPublicIdentifier("PUBLIC");
        doctype.getPublicIdentifier().equals("PUBLIC");
        doctype.reset();
        doctype.getPublicIdentifier().equals("");
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirksFalseAndGetIsForceQuirksShouldReturnFalse() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeForceQuirksTrueAndGetIsForceQuirksShouldReturnTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = null;
        tag.newAttribute();
        tag.attributes = null;
        tag.reset();
        tag.tagName = null;
        tag.attributes = null;
    }

    @Test
    public void testTagSetName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.tagName = "div";
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        tag.attributes.put(new Attribute("class", "myclass"));
        tag.hasPendingAttributeValue = true;
        tag.appendAttributeValue(" hello");
        tag.getAttributes().get("class").getValue().equals("myclass hello");
        tag.reset();
        tag.tagName = null;
        tag.attributes = null;
    }

    @Test
    public void testTagSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.isSelfClosing = true;
        assertTrue(tag.isSelfClosing());
        tag.reset();
        tag.isSelfClosing = false;
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "myclass"));
        assertEquals("<div class=\"myclass\"></div>", startTag.toString());
        startTag.reset();
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());
        endTag.reset();
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.equals("");
        comment.reset();
        comment.data.equals("");
    }

    @Test
    public void testCommentSetData() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");
        comment.getData().equals("This is a comment");
        comment.reset();
        comment.getData().equals("");
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
        comment.reset();
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = null;
        character.reset();
        character.data = null;
    }

    @Test
    public void testCharacterSetData() {
        Token.Character character = new Token.Character();
        character.data("!");
        character.getData().equals("!");
        character.reset();
        character.data = null;
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data("!");
        assertEquals("!", character.toString());
        character.reset();
    }

    @Test
    public void testTokenIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        token = new Token.StartTag();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testTokenAsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token instanceof Token.Doctype);
        token = new Token.StartTag();
        assertFalse(token instanceof Token.Doctype);
    }

    @Test
    public void testTokenIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        token = new Token.EndTag();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testTokenAsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token instanceof Token.StartTag);
        token = new Token.EndTag();
        assertFalse(token instanceof Token.StartTag);
    }

    @Test
    public void testTokenIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
        token = new Token.StartTag();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testTokenAsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token instanceof Token.EndTag);
        token = new Token.StartTag();
        assertFalse(token instanceof Token.EndTag);
    }

    @Test
    public void testTokenIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        token = new Token.Character();
        assertFalse(token.isComment());
    }

    @Test
    public void testTokenAsComment() {
        Token token = new Token.Comment();
        assertTrue(token instanceof Token.Comment);
        token = new Token.Character();
        assertFalse(token instanceof Token.Comment);
    }

    @Test
    public void testTokenIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
        token = new Token.Comment();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testTokenAsCharacter() {
        Token token = new Token.Character();
        assertTrue(token instanceof Token.Character);
        token = new Token.Comment();
        assertFalse(token instanceof Token.Character);
    }

    @Test
    public void testTokenIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenAsEOF() {
        Token token = new Token.EOF();
        assertTrue(token instanceof Token.EOF);
    }
}