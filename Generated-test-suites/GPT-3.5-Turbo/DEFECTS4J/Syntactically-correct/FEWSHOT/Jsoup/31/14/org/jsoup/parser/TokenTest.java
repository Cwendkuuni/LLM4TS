package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.junit.Test;

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
    public void testStartTagConstructorNoAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.name);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagConstructorWithName() {
        Token.StartTag startTag = new Token.StartTag("div");
        assertEquals("div", startTag.name);
        assertFalse(startTag.selfClosing);
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testStartTagConstructorWithNameAndAttributes() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        assertEquals("div", startTag.name);
        assertFalse(startTag.selfClosing);
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
    }

    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.finaliseTag();
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNameSet() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.tagName);
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
    public void testStartTagAppendTagNameString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = null;
        startTag.appendTagName("div");
        assertEquals("div", startTag.tagName);
        
        startTag.tagName = "d";
        startTag.appendTagName("iv");
        assertEquals("div", startTag.tagName);
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = null;
        startTag.appendTagName('d');
        assertEquals("d", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeNameString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = null;
        startTag.appendAttributeName("id");
        assertEquals("id", startTag.pendingAttributeName);
        
        startTag.pendingAttributeName = "i";
        startTag.appendAttributeName("d");
        assertEquals("id", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = null;
        startTag.appendAttributeName('i');
        assertEquals("i", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValueString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = null;
        startTag.appendAttributeValue("value");
        assertEquals("value", startTag.pendingAttributeValue.toString());
        
        startTag.pendingAttributeValue = new StringBuilder("val");
        startTag.appendAttributeValue("ue");
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = null;
        startTag.appendAttributeValue('v');
        assertEquals("v", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testEndTagConstructorNoName() {
        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.tagName);
    }

    @Test
    public void testEndTagConstructorWithName() {
        Token.EndTag endTag = new Token.EndTag("div");
        assertEquals("div", endTag.tagName);
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }

    @Test
    public void testEndTagNameSet() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("div", endTag.tagName);
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
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        token = new Token.StartTag();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertSame(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        token = new Token.Comment();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertSame(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        token = new Token.Character("A");
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertSame(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        token = new Token.Doctype();
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertSame(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("A");
        assertTrue(token.isCharacter());

        token = new Token.EndTag();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("A");
        Token token = character;
        assertSame(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        token = new Token.StartTag();
        assertFalse(token.isEOF());
    }
}