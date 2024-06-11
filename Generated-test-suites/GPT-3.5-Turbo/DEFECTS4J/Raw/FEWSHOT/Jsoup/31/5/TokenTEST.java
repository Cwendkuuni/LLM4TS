package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue = new StringBuilder("myId");
        startTag.finaliseTag();
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        Attribute attribute = startTag.attributes.get("id");
        assertEquals("myId", attribute.getValue());
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
        Attributes attributes = new Attributes();
        startTag.attributes = attributes;
        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagNameWithString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "d";
        startTag.appendTagName("iv");
        assertEquals("div", startTag.tagName);
    }

    @Test
    public void testStartTagAppendTagNameWithChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "d";
        startTag.appendTagName('i');
        assertEquals("di", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeNameWithString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "i";
        startTag.appendAttributeName("d");
        assertEquals("id", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameWithChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "i";
        startTag.appendAttributeName('d');
        assertEquals("id", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValueWithString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder("my");
        startTag.appendAttributeValue("Id");
        assertEquals("myId", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueWithChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder("my");
        startTag.appendAttributeValue('I');
        assertEquals("myI", startTag.pendingAttributeValue.toString());
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
        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Character data");
        assertEquals("Character data", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("Character data");
        assertEquals("Character data", character.toString());
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
        Token.Character character = new Token.Character("Character data");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("Character data");
        Token token = character;
        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}