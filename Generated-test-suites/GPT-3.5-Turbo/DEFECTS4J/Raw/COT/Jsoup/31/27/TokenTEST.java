
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        
        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
        
        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());
        
        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        
        token = new Token.Character("data");
        assertEquals("Character", token.tokenType());
        
        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("example");
        assertEquals("example", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "example";
        assertEquals("example", startTag.name());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "example";
        assertEquals("example", endTag.name());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("example");
        assertEquals("example", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}
