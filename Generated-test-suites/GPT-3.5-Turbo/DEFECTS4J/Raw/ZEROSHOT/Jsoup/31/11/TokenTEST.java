import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    // Test tokenType method
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            void testMethod() {}
        };
        
        assertEquals("Token", token.tokenType());
    }
    
    // Test Doctype getName method
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        
        assertEquals("html", doctype.getName());
    }
    
    // Test Doctype getPublicIdentifier method
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
        
        assertEquals("-//W3C//DTD HTML 4.0 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    // Test Doctype getSystemIdentifier method
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
        
        assertEquals("http://www.w3.org/TR/REC-html40/loose.dtd", doctype.getSystemIdentifier());
    }
    
    // Test Doctype isForceQuirks method
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    // Test StartTag name method
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "body";
        
        assertEquals("body", startTag.name());
    }
    
    // Test StartTag isSelfClosing method
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        
        assertTrue(startTag.isSelfClosing());
    }
    
    // Test StartTag getAttributes method
    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        startTag.attributes = attributes;
        
        assertEquals(attributes, startTag.getAttributes());
    }
    
    // Test EndTag name method
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "body";
        
        assertEquals("body", endTag.name());
    }
    
    // Test Comment getData method
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("This is a comment", comment.getData());
    }
    
    // Test Character getData method
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("a");
        
        assertEquals("a", character.getData());
    }
    
    // Test isDoctype method
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.isDoctype());
    }
    
    // Test asDoctype method
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        
        assertNotNull(doctype);
    }
    
    // Test isStartTag method
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        
        assertTrue(token.isStartTag());
    }
    
    // Test asStartTag method
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        
        assertNotNull(startTag);
    }
    
    // Test isEndTag method
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        
        assertTrue(token.isEndTag());
    }
    
    // Test asEndTag method
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        
        assertNotNull(endTag);
    }
    
    // Test isComment method
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        
        assertTrue(token.isComment());
    }
    
    // Test asComment method
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        
        assertNotNull(comment);
    }
    
    // Test isCharacter method
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("a");
        
        assertTrue(token.isCharacter());
    }
    
    // Test asCharacter method
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("a");
        Token.Character character = token.asCharacter();
        
        assertNotNull(character);
    }
    
    // Test isEOF method
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        
        assertTrue(token.isEOF());
    }
}