package org.jsoup.parser;
 
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("container");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = false;
        tag.attributes = new Attributes();
        tag.attributes.put("class", "wrapper");
        
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
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment.");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Some character data";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertNotNull(resetEOF);
    }
}