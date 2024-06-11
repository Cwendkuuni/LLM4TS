
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.pendingAttributeName = "name";
        startTag.newAttribute();
        assertEquals("", startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        
        Attributes attributes = startTag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("name", attributes.get("name"));
        
        startTag.pendingAttributeName = null;
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.newAttribute();
        assertEquals("", startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        
        attributes = startTag.getAttributes();
        Attribute attribute = attributes.get("value");
        assertNotNull(attribute);
        assertEquals("value", attribute.getValue());
        
        startTag.pendingAttributeName = "name2";
        startTag.newAttribute();
        assertEquals("", startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        
        attributes = startTag.getAttributes();
        assertNull(attributes.get("name2"));
    }
    
    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.pendingAttributeName = "name";
        startTag.finaliseTag();
        assertEquals("", startTag.pendingAttributeName);
        
        Attributes attributes = startTag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("name", attributes.get("name"));
        
        startTag.pendingAttributeName = null;
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.finaliseTag();
        assertEquals("", startTag.pendingAttributeName);
        
        attributes = startTag.getAttributes();
        Attribute attribute = attributes.get("value");
        assertNotNull(attribute);
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.tagName);
        
        startTag.name("div");
        assertEquals("div", startTag.tagName);
        
        startTag.name("p");
        assertEquals("p", startTag.tagName);
    }
    
 
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.tagName);
        
        startTag.appendTagName("div");
        assertEquals("div", startTag.tagName);
        
        startTag.appendTagName("p");
        assertEquals("divp", startTag.tagName);
    }
    
    @Test
    public void testStartTagAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.tagName);
        
        startTag.appendTagName('d');
        assertEquals("d", startTag.tagName);
        
        startTag.appendTagName('i');
        assertEquals("di", startTag.tagName);
    }
    
    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeName);
        
        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);
        
        startTag.appendAttributeName("id");
        assertEquals("classid", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeName);
        
        startTag.appendAttributeName('c');
        assertEquals("c", startTag.pendingAttributeName);
        
        startTag.appendAttributeName('l');
        assertEquals("cl", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeValue);
        
        startTag.appendAttributeValue("value");
        assertEquals("value", startTag.pendingAttributeValue.toString());
        
        startTag.appendAttributeValue("123");
        assertEquals("value123", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeValue);
        
        startTag.appendAttributeValue('v');
        assertEquals("v", startTag.pendingAttributeValue.toString());
        
        startTag.appendAttributeValue('a');
        assertEquals("va", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.tagName);
        
        endTag.name("div");
        assertEquals("div", endTag.tagName);
        
        endTag.name("p");
        assertEquals("p", endTag.tagName);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assertNull(comment.getData());
        
        comment.data.append("test");
        assertEquals("test", comment.getData());
        
        comment.data.append("123");
        assertEquals("test123", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
        
        character = new Token.Character("");
        assertEquals("", character.getData());
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        
        token = new Token.StartTag();
        assertFalse(token.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        
        token = new Token.EndTag();
        assertFalse(token.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
        
        token = new Token.Comment();
        assertFalse(token.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        
        token = new Token.Character("");
        assertFalse(token.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("");
        assertTrue(token.isCharacter());
        
        token = new Token.EOF();
        assertFalse(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        
        token = new Token.Doctype();
        assertFalse(token.isEOF());
    }
}
