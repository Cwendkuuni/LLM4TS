import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/strict.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testResetWithStringBuilderNotNull() {
        StringBuilder sb = new StringBuilder();
        sb.append("test");
        
        Token.reset(sb);
        
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testResetWithStringBuilderNull() {
        StringBuilder sb = null;
        
        Token.reset(sb);
        
        assertNull(sb);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("main");
        startTag.selfClosing = true;
        
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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        
        startTag.tagName = "div";
        
        startTag.nameAttr("h1", attributes);
        
        assertEquals("h1", startTag.tagName);
        assertSame(attributes, startTag.attributes);
        assertEquals("h1", startTag.normalName);
    }
    
    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        attributes.put("id", "main");
        startTag.tagName = "div";
        startTag.attributes = attributes;
        
        String expectedOutput = "<div class=\"container\" id=\"main\">";
        
        assertEquals(expectedOutput, startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        
        String expectedOutput = "<div>";
        
        assertEquals(expectedOutput, startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        String expectedOutput = "</div>";
        
        assertEquals(expectedOutput, endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "This is a character";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        
        character.data("This is a character");
        
        assertEquals("This is a character", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        assertSame(eof, eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertSame(doctype, doctype.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertSame(startTag, startTag.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertSame(endTag, endTag.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertSame(comment, comment.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        
        assertSame(character, character.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
    
    @Test
    public void testTokenType() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}