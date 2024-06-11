
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        
        Token.Comment comment = new Token.Comment();
        assertEquals(Token.TokenType.Comment, comment.type);
        
        Token.Character character = new Token.Character();
        assertEquals(Token.TokenType.Character, character.type);
        
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        assertEquals("name", doctype.getName());
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
        
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        
        assertNull(startTag.attributes);
        
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        
        startTag.newAttribute();
        
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        Attribute attribute = startTag.attributes.iterator().next();
        assertEquals("attr", attribute.getKey());
        assertEquals("value", attribute.getValue());
        
        startTag.pendingAttributeName = "empty";
        startTag.hasEmptyAttributeValue = true;
        
        startTag.newAttribute();
        
        assertEquals(2, startTag.attributes.size());
        Attribute emptyAttribute = null;
        for (Attribute attr : startTag.attributes) {
            if (attr.getKey().equals("empty")) {
                emptyAttribute = attr;
                break;
            }
        }
        assertNotNull(emptyAttribute);
        assertEquals("", emptyAttribute.getValue());
        
        startTag.pendingAttributeName = null;
        startTag.pendingAttributeValue.append("value2");
        
        startTag.newAttribute();
        
        assertEquals(3, startTag.attributes.size());
        Attribute booleanAttribute = null;
        for (Attribute attr : startTag.attributes) {
            if (attr.getKey().equals("boolean")) {
                booleanAttribute = attr;
                break;
            }
        }
        assertNotNull(booleanAttribute);
        assertTrue(booleanAttribute instanceof BooleanAttribute);
        assertEquals("value2", booleanAttribute.getValue());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        
        startTag.name("tag");
        
        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertFalse(startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.getAttributes());
        
        startTag.attributes = new Attributes();
        assertNotNull(startTag.getAttributes());
    }
    
    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("tag");
        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
        
        startTag.appendTagName('A');
        assertEquals("tagA", startTag.tagName);
        assertEquals("taga", startTag.normalName);
    }
    
    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("attr");
        assertEquals("attr", startTag.pendingAttributeName);
        
        startTag.appendAttributeName('A');
        assertEquals("attrA", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        startTag.appendAttributeValue("value");
        assertEquals("value", startTag.pendingAttributeValue.toString());
        
        startTag.ensureAttributeValue();
        startTag.appendAttributeValue('A');
        assertEquals("valueA", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagSetEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.hasEmptyAttributeValue);
        
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "tag";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }
}
