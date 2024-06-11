import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.setForceQuirks(true);
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagTokenReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", null);
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("container");
        startTag.newAttribute();
        startTag.setEmptyAttributeValue();
        
        startTag.reset();
        
        assertEquals("", startTag.name());
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.normalName());
    }
    
    @Test
    public void testCommentTokenReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterTokenReset() {
        Token.Character character = new Token.Character();
        character.data("A");
        
        character.reset();
        
        assertNull(character.getData());
    }
    
    @Test
    public void testEOFTokenReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertNotNull(eof);
    }
}