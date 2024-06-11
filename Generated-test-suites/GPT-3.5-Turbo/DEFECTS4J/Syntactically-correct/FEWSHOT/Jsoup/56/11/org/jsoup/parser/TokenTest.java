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
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.appendAttributeValue("unique");
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        tag.attributes.put("id", "unique");
        
        Token.Tag resetTag = tag.reset();
        
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";
        
        String expected = "</p>";
        String result = endTag.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Some character data";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertSame(resetEOF, eof);
    }
}