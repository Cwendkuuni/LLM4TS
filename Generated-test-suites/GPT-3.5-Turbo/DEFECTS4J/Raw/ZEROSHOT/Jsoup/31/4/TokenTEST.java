import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.EOF();
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
    public void testStartTagTagName() {
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
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("span");
        assertEquals("span", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("id");
        assertEquals("id", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("myValue");
        assertEquals("myValue", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
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
        Token token = new Token.Character("A");
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("A");
        Token.Character character = token.asCharacter();
        assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

}