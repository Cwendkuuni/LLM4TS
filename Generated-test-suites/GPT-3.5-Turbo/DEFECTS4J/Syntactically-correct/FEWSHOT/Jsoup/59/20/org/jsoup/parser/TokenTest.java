package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//id");
        doctype.systemIdentifier.append("//system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }
    
    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myDiv");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Some characters";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertNull(eof.reset());
    }
    
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myDiv");
        
        assertEquals("<div id=\"myDiv\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "Some characters";
        
        assertEquals("Some characters", character.toString());
    }
}