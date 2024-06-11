import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;
    
    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character("");
        eof = new Token.EOF();
    }
    
    @Test
    public void testTokenType() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        assertNull(startTag.getAttributes());
    }
    
    @Test
    public void testEndTagName() {
        endTag.tagName = "div";
        assertEquals("div", endTag.name());
    }
    
    @Test
    public void testCommentGetData() {
        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        assertEquals("", character.getData());
        
        Token.Character charWithData = new Token.Character("Hello World");
        assertEquals("Hello World", charWithData.getData());
    }
    
    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype1 = doctype.asDoctype();
        assertNotNull(doctype1);
    }
    
    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag1 = startTag.asStartTag();
        assertNotNull(startTag1);
    }
    
    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag1 = endTag.asEndTag();
        assertNotNull(endTag1);
    }
    
    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment1 = comment.asComment();
        assertNotNull(comment1);
    }
    
    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character1 = character.asCharacter();
        assertNotNull(character1);
    }
    
    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }
    
    @Test
    public void testTokenTypeEnum() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
    
    @Test
    public void testStartTagToString() {
        startTag.tagName = "div";
        startTag.attributes.put(new Attribute("id", "myDiv"));
        assertEquals("<div id=\"myDiv\">", startTag.toString());
        
        startTag.attributes.put(new Attribute("class", "myClass"));
        assertEquals("<div id=\"myDiv\" class=\"myClass\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentToString() {
        comment.data.append("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }
    
    @Test
    public void testCharacterToString() {
        assertEquals("", character.toString());
        
        Token.Character charWithData = new Token.Character("Hello World");
        assertEquals("Hello World", charWithData.toString());
    }
}