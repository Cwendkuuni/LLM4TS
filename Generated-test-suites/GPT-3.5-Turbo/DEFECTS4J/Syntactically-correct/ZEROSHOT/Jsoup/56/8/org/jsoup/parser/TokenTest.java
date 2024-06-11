package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    Token.Doctype doctype;
    Token.StartTag startTag;
    Token.EndTag endTag;
    Token.Comment comment;
    Token.Character character;
    Token.EOF eof;
    
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
        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("name");
        
        String name = doctype.getName();
        
        assertEquals("name", name);
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("publicIdentifier");
        
        String publicIdentifier = doctype.getPublicIdentifier();
        
        assertEquals("publicIdentifier", publicIdentifier);
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("systemIdentifier");
        
        String systemIdentifier = doctype.getSystemIdentifier();
        
        assertEquals("systemIdentifier", systemIdentifier);
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;
        
        boolean forceQuirks = doctype.isForceQuirks();
        
        assertTrue(forceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
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
    public void testStartTagName() {
        startTag.tagName = "tag";
        
        String name = startTag.name();
        
        assertEquals("tag", name);
    }
    
    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "tag";
        
        String normalName = startTag.normalName();
        
        assertEquals("tag", normalName);
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;
        
        boolean selfClosing = startTag.isSelfClosing();
        
        assertTrue(selfClosing);
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        startTag.attributes = attributes;
        
        Attributes retrievedAttributes = startTag.getAttributes();
        
        assertEquals(attributes, retrievedAttributes);
    }
    
    @Test
    public void testStartTagAppendTagNameWithAppend() {
        startTag.tagName = "tag";
        
        startTag.appendTagName("Name");
        
        assertEquals("tagNameName", startTag.tagName);
    }
    
    @Test
    public void testStartTagAppendTagNameWithAppendChar() {
        startTag.tagName = "tag";
        
        startTag.appendTagName('N');
        
        assertEquals("tagNameN", startTag.tagName);
    }
    
    @Test
    public void testStartTagAppendAttributeNameWithAppend() {
        startTag.pendingAttributeName = "attr";
        
        startTag.appendAttributeName("Name");
        
        assertEquals("attrName", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeNameWithAppendChar() {
        startTag.pendingAttributeName = "attr";
        
        startTag.appendAttributeName('N');
        
        assertEquals("attrN", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValueWithAppend() {
        startTag.ensureAttributeValue();
        startTag.appendAttributeValue("value1");
        
        assertEquals("value1", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagAppendAttributeValueWithAppendChar() {
        startTag.ensureAttributeValue();
        startTag.appendAttributeValue('c');
        
        assertEquals("c", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagAppendAttributeValueWithAppendCharArray() {
        startTag.ensureAttributeValue();
        char[] array = {'v', 'a', 'l', 'u', 'e'};
        startTag.appendAttributeValue(array);
        
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagAppendAttributeValueWithAppendInt() {
        startTag.ensureAttributeValue();
        int[] codepoints = {118, 97, 108, 117, 101};
        startTag.appendAttributeValue(codepoints);
        
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagSetEmptyAttributeValue() {
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testStartTagNewAttributeWithHasPendingAttributeValue() {
        startTag.pendingAttributeName = "attr";
        startTag.hasPendingAttributeValue = true;
        startTag.pendingAttributeValue.append("value");
        
        startTag.newAttribute();
        
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals("", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagNewAttributeWithHasEmptyAttributeValue() {
        startTag.pendingAttributeName = "attr";
        startTag.hasEmptyAttributeValue = true;
        
        startTag.newAttribute();
        
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testStartTagNewAttributeWithoutPendingAttribute() {
        startTag.newAttribute();
        
        assertNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagFinaliseTagWithPendingAttribute() {
        startTag.pendingAttributeName = "attr";
        
        startTag.finaliseTag();
        
        assertNull(startTag.pendingAttributeName);
    }
    
    @Test
    public void testEndTagReset() {
        endTag.tagName = "tag";
        endTag.normalName = "tag";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }
    
    @Test
    public void testEndTagName() {
        endTag.tagName = "tag";
        
        String name = endTag.name();
        
        assertEquals("tag", name);
    }
    
    @Test
    public void testEndTagNormalName() {
        endTag.normalName = "tag";
        
        String normalName = endTag.normalName();
        
        assertEquals("tag", normalName);
    }
    
    @Test
    public void testEndTagToString() {
        endTag.tagName = "tag";
        
        String result = endTag.toString();
        
        assertEquals("</tag>", result);
    }
    
    @Test
    public void testCommentReset() {
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        comment.data.append("data");
        
        String data = comment.getData();
        
        assertEquals("data", data);
    }
    
    @Test
    public void testCommentToString() {
        comment.data.append("data");
        
        String result = comment.toString();
        
        assertEquals("<!--data-->", result);
    }
    
    @Test
    public void testCharacterReset() {
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCharacterData() {
        character.data("data");
        
        String data = character.getData();
        
        assertEquals("data", data);
    }
    
    @Test
    public void testCharacterToString() {
        character.data = "data";
        
        String result = character.toString();
        
        assertEquals("data", result);
    }
    
    @Test
    public void testEOFReset() {
        EOF resetEOF = eof.reset();
        
        assertSame(eof, resetEOF);
    }
}