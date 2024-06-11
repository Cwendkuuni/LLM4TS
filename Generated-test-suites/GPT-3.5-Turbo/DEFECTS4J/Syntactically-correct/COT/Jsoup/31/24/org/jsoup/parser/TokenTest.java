
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        
        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
        
        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());
        
        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        
        token = new Token.Character("");
        assertEquals("Character", token.tokenType());
        
        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
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
        doctype.publicIdentifier.append("public id");
        assertEquals("public id", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system id");
        assertEquals("system id", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        assertNotNull(tag.getAttributes());
        assertEquals(1, tag.getAttributes().size());

        tag.newAttribute();
        assertEquals(2, tag.getAttributes().size());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.finaliseTag();
        assertNotNull(tag.getAttributes());
        assertEquals(0, tag.getAttributes().size());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
    }

    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("di");
        assertEquals("di", tag.name());
        tag.appendTagName("v");
        assertEquals("div", tag.name());
    }

    @Test
    public void testAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("id");
        assertEquals("id", tag.pendingAttributeName);
        tag.appendAttributeName("=");
        assertEquals("id=", tag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("home");
        assertEquals("home", tag.pendingAttributeValue.toString());
        tag.appendAttributeValue("page");
        assertEquals("homepage", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("<div>", startTag.toString());

        startTag = new Token.StartTag("img");
        assertEquals("<img>", startTag.toString());

        Attributes attributes = new Attributes();
        attributes.put("src", "image.jpg");
        attributes.put("alt", "image");
        startTag = new Token.StartTag("img", attributes);
        assertEquals("<img src=\"image.jpg\" alt=\"image\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());

        endTag = new Token.EndTag("img");
        assertEquals("</img>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("abc");
        assertEquals("abc", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("");
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }

}
