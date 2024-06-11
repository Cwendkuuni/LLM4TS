import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//W3C/DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        
        assertEquals("html", doctype.getName());
        assertEquals("//W3C/DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
    }
    
    @Test
    public void testAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new org.jsoup.nodes.Attributes();
        assertNotNull(startTag.getAttributes());
        assertEquals(0, startTag.getAttributes().size());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("strong");
        assertEquals("</strong>", endTag.toString());
    }
    
    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character("abc");
        assertEquals("abc", character.getData());
    }
    
    @Test
    public void testTokenTypeCheck() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
    }

}