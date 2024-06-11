package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue = new StringBuilder("testId");
        tag.pendingAttributeValueS = "testId";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        
        tag.newAttribute();
        
        assertNotNull(tag.attributes);
        assertNull(tag.pendingAttributeName);
        assertTrue(tag.pendingAttributeValue.toString().isEmpty());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "data";
        tag.pendingAttributeValue.append("testData");
        
        tag.finaliseTag();
        
        assertNull(tag.pendingAttributeName);
        assertTrue(tag.attributes.containsKey("data"));
        assertEquals("testData", tag.attributes.get("data"));
    }

    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "span";
        
        String name = tag.name();
        
        assertEquals("span", name);
    }

    @Test
    public void testTagGetNormalName() {
        Token.Tag tag = new Token.StartTag();
        tag.normalName = "span";
        
        String normalName = tag.normalName();
        
        assertEquals("span", normalName);
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendTagName("span");
        
        assertEquals("span", tag.tagName);
        assertEquals("span", tag.normalName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendTagName('d');
        
        assertEquals("d", tag.tagName);
        assertEquals("d", tag.normalName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeName("id");
        
        assertEquals("id", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeName('d');
        
        assertEquals("d", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeValue("testValue");
        
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.pendingAttributeValue.toString().contains("testValue"));
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeValue('d');
        
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.pendingAttributeValue.toString().contains("d"));
    }

    @Test
    public void testTagAppendAttributeValueCharArray() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeValue(new char[]{'d', 'i', 'v'});
        
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.pendingAttributeValue.toString().contains("div"));
    }

    @Test
    public void testTagAppendAttributeValueIntArray() {
        Token.Tag tag = new Token.StartTag();
        
        tag.appendAttributeValue(new int[]{100, 105, 118});
        
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.pendingAttributeValue.toString().contains("div"));
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        
        tag.setEmptyAttributeValue();
        
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("test");
        
        character.reset();
        
        assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        
        character.data("test");
        
        assertEquals("test", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertNotNull(eof);
    }
}