import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
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
        doctype.publicIdentifier.append("publicId");
        assertEquals("publicId", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemId");
        assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tagname");
        assertEquals("tagname", startTag.name());
    }

    @Test
    public void testIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attribute", "value"));
        startTag.attributes = attributes;
        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("append");
        assertEquals("append", startTag.tagName);
    }

    @Test
    public void testAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName('a');
        assertEquals("a", startTag.tagName);
    }

    @Test
    public void testAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("attribute");
        assertEquals("attribute", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName('a');
        assertEquals("a", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("value");
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue('a');
        assertEquals("a", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tagname");
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("attribute", "value"));
        assertEquals("<tagname attribute=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tagname");
        assertEquals("</tagname>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
        assertEquals("<!--comment-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("character");
        assertEquals("character", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("character");
        assertEquals("character", character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.asDoctype() instanceof Token.Doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.asStartTag() instanceof Token.StartTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.asEndTag() instanceof Token.EndTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertTrue(token.asComment() instanceof Token.Comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("character");
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("character");
        assertTrue(token.asCharacter() instanceof Token.Character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

}