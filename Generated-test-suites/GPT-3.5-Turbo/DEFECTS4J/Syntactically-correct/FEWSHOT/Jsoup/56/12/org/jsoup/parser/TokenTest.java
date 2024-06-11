package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;
    
    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }
    
    @Test
    public void testDoctypeReset() {
        doctype.name.append("test");
        doctype.publicIdentifier.append("pub");
        doctype.systemIdentifier.append("sys");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
        startTag.name = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.name);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        endTag.name = "test";
        endTag.normalName = "test";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.name);
        assertNull(resetEndTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        comment.data.append("test");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        character.data = "test";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("test");
        
        assertEquals("test", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("pub");
        
        assertEquals("pub", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("sys");
        
        assertEquals("sys", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
        
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "test";
        startTag.normalName = "test";
        
        assertEquals("test", startTag.name());
    }
    
    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "test";
        
        assertEquals("test", startTag.normalName());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr", "value"));
        startTag.attributes = attributes;
        
        assertEquals(attributes, startTag.getAttributes());
    }
    
    @Test
    public void testStartTagAppendTagNameString() {
        startTag.tagName = "test";
        startTag.normalName = "test";
        
        startTag.appendTagName("123");
        
        assertEquals("test123", startTag.tagName);
        assertEquals("test123", startTag.normalName);
    }
    
    @Test
    public void testStartTagAppendTagNameChar() {
        startTag.tagName = "test";
        startTag.normalName = "test";
        
        startTag.appendTagName('4');
        
        assertEquals("test4", startTag.tagName);
        assertEquals("test4", startTag.normalName);
    }
    
    @Test
    public void testStartTagAppendAttributeNameString() {
        startTag.pendingAttributeName = "attr";
        
        startTag.appendAttributeName("123");
        
        assertEquals("attr123", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeNameChar() {
        startTag.pendingAttributeName = "attr";
        
        startTag.appendAttributeName('4');
        
        assertEquals("attr4", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValueString() {
        startTag.ensureAttributeValue();
        
        startTag.appendAttributeValue("value");
        
        assertEquals("value", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }
    
    @Test
    public void testStartTagAppendAttributeValueChar() {
        startTag.ensureAttributeValue();
        
        startTag.appendAttributeValue('v');
        
        assertEquals("v", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }
    
    @Test
    public void testStartTagAppendAttributeValueCharArray() {
        startTag.ensureAttributeValue();
        
        startTag.appendAttributeValue(new char[]{'v', 'a', 'l', 'u', 'e'});
        
        assertEquals("value", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }
    
    @Test
    public void testStartTagAppendAttributeValueInt() {
        startTag.ensureAttributeValue();
        
        startTag.appendAttributeValue(new int[]{118, 97, 108, 117, 101});
        
        assertEquals("value", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }
    
    @Test
    public void testStartTagSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testEndTagToString() {
        endTag.tagName = "test";
        
        assertEquals("</test>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        comment.data.append("test");
        
        assertEquals("test", comment.getData());
    }
    
    @Test
    public void testCommentToString() {
        comment.data.append("test");
        
        assertEquals("<!--test-->", comment.toString());
    }
    
    @Test
    public void testCharacterGetData() {
        character.data = "test";
        
        assertEquals("test", character.getData());
    }
    
    @Test
    public void testCharacterToString() {
        character.data = "test";
        
        assertEquals("test", character.toString());
    }
}