import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        assertEquals("div", tag.name());
    }

    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("div");
        assertEquals("div", tag.tagName);
    }

    @Test
    public void testAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("id");
        assertEquals("id", tag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("main");
        assertNotNull(tag.pendingAttributeValue);
        assertEquals("main", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterConstructor() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        assertNotNull(doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        assertNotNull(startTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        assertNotNull(endTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        assertNotNull(comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("data");
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("data");
        Token.Character character = token.asCharacter();
        assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}