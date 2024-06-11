package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.hasEmptyAttributeValue = true;
        tag.attributes = new Attributes();
        Attribute attribute = new Attribute("class", "test");
        tag.attributes.put(attribute);
        
        tag.reset();
        
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertEquals(null, tag.attributes);
    }
    
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        
        startTag.nameAttr("span", new Attributes());
        
        assertEquals("span", startTag.name());
        assertEquals("span", startTag.normalName());
        
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "test"));
        startTag.attributes.put(new Attribute("id", "123"));
        
        assertEquals("<span class=\"test\" id=\"123\">", startTag.toString());
    }
    
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test comment");
        
        comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Test character";
        
        character.reset();
        
        assertEquals(null, character.getData());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertEquals(eof, eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        
        assertTrue(token.isEOF());
    }
}