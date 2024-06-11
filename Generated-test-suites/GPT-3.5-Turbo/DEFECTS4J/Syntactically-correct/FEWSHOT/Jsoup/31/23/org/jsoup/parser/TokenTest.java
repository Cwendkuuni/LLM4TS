package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
        assertEquals("", doctype.getName());
        
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getPublicIdentifier());
        
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.1//EN");
        assertEquals("-//W3C//DTD XHTML 1.1//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getSystemIdentifier());
        
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd");
        assertEquals("http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", doctype.getSystemIdentifier());
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
        tag.pendingAttributeName = "name";
        tag.pendingAttributeValue = new StringBuilder("value");
        
        assertNull(tag.attributes);
        
        tag.newAttribute();
        assertEquals(1, tag.attributes.size());
        assertTrue(tag.attributes.hasKey("name"));
        assertEquals("value", tag.attributes.get("name"));
        
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "name";
        tag.pendingAttributeValue = new StringBuilder("value");
        
        tag.finaliseTag();
        assertEquals(1, tag.attributes.size());
        assertTrue(tag.attributes.hasKey("name"));
        assertEquals("value", tag.attributes.get("name"));
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.tagName);
        
        tag.tagName = "div";
        assertEquals("div", tag.name());
    }
    
    @Test
    public void testTagNameSetter() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.tagName);
        
        tag.name("div");
        assertEquals("div", tag.tagName);
    }
    
    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
        
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.getAttributes());
        
        Attributes attributes = new Attributes();
        tag.attributes = attributes;
        assertEquals(attributes, tag.getAttributes());
    }
    
    @Test
    public void testAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.tagName);
        
        tag.appendTagName("div");
        assertEquals("div", tag.tagName);
        
        tag.appendTagName('>');
        assertEquals("div>", tag.tagName);
    }
    
    @Test
    public void testAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.pendingAttributeName);
        
        tag.appendAttributeName("class");
        assertEquals("class", tag.pendingAttributeName);
        
        tag.appendAttributeName('=');
        assertEquals("class=", tag.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        assertNull(tag.pendingAttributeValue);
        
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
        
        tag.appendAttributeValue('=');
        assertEquals("value=", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testToStringStartTagNoAttributes() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        
        assertEquals("<div>", tag.toString());
    }
    
    @Test
    public void testToStringStartTagWithAttributes() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "a";
        tag.attributes = new Attributes();
        tag.attributes.put("href", "https://www.example.com");
        tag.attributes.put("target", "_blank");
        
        assertEquals("<a href=\"https://www.example.com\" target=\"_blank\">", tag.toString());
    }
    
    @Test
    public void testToStringEndTag() {
        Token.Tag tag = new Token.EndTag();
        tag.tagName = "div";
        
        assertEquals("</div>", tag.toString());
    }
    
    @Test
    public void testGetData() {
        Token.Comment comment = new Token.Comment();
        assertEquals("", comment.getData());
        
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testToStringComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testGetDataCharacter() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }
    
    @Test
    public void testToStringCharacter() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.toString());
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        
        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        
        assertEquals(doctype, token);
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
        
        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        
        assertEquals(startTag, token);
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
        
        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        
        assertEquals(endTag, token);
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
        
        Token.Character character = new Token.Character("");
        assertFalse(character.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        
        assertEquals(comment, token);
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("");
        assertTrue(character.isCharacter());
        
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("");
        Token.Character character = token.asCharacter();
        
        assertEquals(character, token);
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
        
        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isEOF());
    }
    
}