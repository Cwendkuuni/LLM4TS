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
        character = new Token.Character("Test Character");
        eof = new Token.EOF();
    }
    
    @Test
    public void testTokenDoctype() {
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertFalse(doctype.isForceQuirks());
        
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        assertEquals("HTML", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testTokenStartTag() {
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertFalse(startTag.isSelfClosing());
        
        startTag.name("div");
        startTag.newAttribute();
        
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("test-id");
        
        startTag.finaliseTag();
        
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNotNull(startTag.getAttributes());
        assertEquals("test-id", startTag.getAttributes().get("id"));
    }
    
    @Test
    public void testTokenEndTag() {
        assertEquals(Token.TokenType.EndTag, endTag.type);
        
        endTag.name("div");
        
        assertEquals("div", endTag.name());
    }
    
    @Test
    public void testTokenComment() {
        assertEquals(Token.TokenType.Comment, comment.type);
        
        comment.data.append("This is a test comment");
        
        assertEquals("This is a test comment", comment.getData());
    }
    
    @Test
    public void testTokenCharacter() {
        assertEquals(Token.TokenType.Character, character.type);
        
        assertEquals("Test Character", character.getData());
    }
    
    @Test
    public void testTokenEOF() {
        assertEquals(Token.TokenType.EOF, eof.type);
    }
}