import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test cases for Token.reset()
    @Test
    public void testResetDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
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
    public void testResetTagToken() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "a";
        tag.normalName = "a";
        tag.pendingAttributeName = "href";
        tag.pendingAttributeValue.append("https://www.example.com");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "button"));
        
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
    public void testResetCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testResetCharacterToken() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    // Additional test cases for Token.reset()
    
    @Test
    public void testResetDoctypeTokenWithEmptyName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("");
        doctype.pubSysKey = "pub";
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
    public void testResetTagTokenWithEmptyTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "";
        tag.normalName = "";
        tag.pendingAttributeName = "href";
        tag.pendingAttributeValue.append("https://www.example.com");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "button"));
        
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
    
    // Test cases for Token.toString()
    @Test
    public void testDoctypeTokenToString() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;
        
        String expected = "<!DOCTYPE html PUB \"pubId\" \"sysId\">";
        String actual = doctype.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTagTokenToString() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "a";
        tag.normalName = "a";
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("href", "https://www.example.com"));
        tag.attributes.put(new Attribute("class", "button"));
        
        String expected = "<a href=\"https://www.example.com\" class=\"button\">";
        String actual = tag.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCommentTokenToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCharacterTokenToString() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        String expected = "This is a character";
        String actual = character.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEOFTokenToString() {
        Token.EOF eof = new Token.EOF();
        
        String expected = "";
        String actual = eof.toString();
        
        assertEquals(expected, actual);
    }
    
}