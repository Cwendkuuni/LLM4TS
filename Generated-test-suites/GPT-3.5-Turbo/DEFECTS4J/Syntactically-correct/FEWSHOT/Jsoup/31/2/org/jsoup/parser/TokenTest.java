package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testToken() {
        Token.Doctype doctype = new Token.Doctype();
        
        // Test getName
        assertEquals("", doctype.getName());
        
        // Test getPublicIdentifier
        assertEquals("", doctype.getPublicIdentifier());
        
        // Test getSystemIdentifier
        assertEquals("", doctype.getSystemIdentifier());
        
        // Test isForceQuirks
        assertFalse(doctype.isForceQuirks());
        
        // Test tokenType
        assertEquals("Doctype", doctype.tokenType());
        
        Token.StartTag startTag = new Token.StartTag();
        
        // Test name
        assertNull(startTag.name());
        
        // Test name
        startTag.name("test");
        assertEquals("test", startTag.name());
        
        // Test isSelfClosing
        assertFalse(startTag.isSelfClosing());
        
        // Test getAttributes
        assertNull(startTag.getAttributes());
        
        // Test appendTagName
        startTag.appendTagName("test");
        assertEquals("test", startTag.name());
        
        // Test appendTagName
        startTag.appendTagName('t');
        assertEquals("testt", startTag.name());
        
        // Test appendAttributeName
        startTag.appendAttributeName("name");
        assertNull(startTag.pendingAttributeName);
        
        // Test appendAttributeName
        startTag.newAttribute();
        startTag.appendAttributeName("test");
        assertNull(startTag.pendingAttributeName);
        
        // Test appendAttributeName
        startTag.appendAttributeName('n');
        assertEquals("n", startTag.pendingAttributeName);
        
        // Test appendAttributeValue
        startTag.appendAttributeValue("value");
        assertNull(startTag.pendingAttributeValue);
        
        // Test appendAttributeValue
        startTag.newAttribute();
        startTag.appendAttributeValue("test");
        assertNull(startTag.pendingAttributeValue);
        
        // Test appendAttributeValue
        startTag.appendAttributeValue('v');
        assertEquals("v", startTag.pendingAttributeValue.toString());
        
        // Test finaliseTag
        startTag.pendingAttributeName = "test";
        startTag.finaliseTag();
        assertEquals("test", startTag.name());
        
        // Test toString
        assertEquals("<test>", startTag.toString());
        
        Token.EndTag endTag = new Token.EndTag();
        
        // Test toString
        assertEquals("</>", endTag.toString());
        
        Token.Comment comment = new Token.Comment();
        
        // Test getData
        assertEquals("", comment.getData());
        
        // Test toString
        assertEquals("<!-- -->", comment.toString());
        
        Token.Character character = new Token.Character("test");
        
        // Test getData
        assertEquals("test", character.getData());
        
        // Test toString
        assertEquals("test", character.toString());
        
        Token.EOF eof = new Token.EOF();
        
        // Test isEOF
        assertTrue(eof.isEOF());
    }
}