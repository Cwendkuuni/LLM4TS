import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeMethods() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        
        assertEquals("html", doctype.getName());
        assertEquals("//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagMethods() {
        Token.Tag tag = new Token.StartTag();
        tag.name("a");
        tag.appendAttributeName("href");
        tag.appendAttributeValue("http://example.com");
        tag.newAttribute();
        
        assertEquals("a", tag.name());
        assertTrue(tag.isSelfClosing());
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("div");
        assertEquals("<div>", startTag.toString());
        
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("container");
        startTag.newAttribute();
        
        assertEquals("<div class=\"container\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Hello, World!");
        assertEquals("Hello, World!", character.getData());
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
        Token token = new Token.Character("test");
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}