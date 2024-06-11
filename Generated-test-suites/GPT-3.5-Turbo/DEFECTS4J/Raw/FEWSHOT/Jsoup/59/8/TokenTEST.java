package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        // test reset method
        doctype.name.append("html");
        doctype.pubSysKey = "system";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
        
        // test getters and setters
        doctype.getName().append("html");
        doctype.getPubSysKey().append("system");
        doctype.getPublicIdentifier().append("//W3C//DTD HTML 4.01//EN");
        doctype.getSystemIdentifier().append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.isForceQuirks();
        assertTrue(doctype.getName().toString().equalsIgnoreCase("html"));
        assertTrue(doctype.getPubSysKey().equalsIgnoreCase("system"));
        assertTrue(doctype.getPublicIdentifier().equalsIgnoreCase("//W3C//DTD HTML 4.01//EN"));
        assertTrue(doctype.getSystemIdentifier().equalsIgnoreCase("http://www.w3.org/TR/html4/strict.dtd"));
        
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        // test reset method
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue = new StringBuilder("container");
        startTag.pendingAttributeValueS = "container";
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = null;
        
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
        
        // test getters and setters
        startTag.name("div");
        assertEquals(startTag.normalName(), "div");
        assertTrue(startTag.name().equalsIgnoreCase("div"));
        assertTrue(startTag.isSelfClosing());
        
    }
    
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        // test reset method
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        
        // test toString method
        assertTrue(endTag.toString().equalsIgnoreCase("</div>"));
    }
    
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        
        // test reset method
        comment.data.append("This is a comment.");
        comment.bogus = true;
        
        comment.reset();
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
        
        // test getters and setters
        comment.getData().append("This is a comment.");
        assertTrue(comment.getData().equalsIgnoreCase("This is a comment."));
        
        // test toString method
        assertTrue(comment.toString().equalsIgnoreCase("<!--This is a comment.-->"));
    }
    
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        
        // test reset method
        character.data = "This is a character";
        
        character.reset();
        assertNull(character.data);
        
        // test getters and setters
        character.data("This is a character");
        assertTrue(character.getData().equalsIgnoreCase("This is a character"));
        
        // test toString method
        assertTrue(character.toString().equalsIgnoreCase("This is a character"));
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        // test reset method
        eof.reset();
        
        // test toString method
        assertTrue(eof.toString().isEmpty());
    }
}