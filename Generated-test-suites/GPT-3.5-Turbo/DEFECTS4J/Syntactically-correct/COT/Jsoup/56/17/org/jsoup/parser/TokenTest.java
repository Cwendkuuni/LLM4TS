
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "string value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
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
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "TAG";
        endTag.pendingAttributeName = "attribute";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "string value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }
    
    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testTokenTypeDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testTokenTypeStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testTokenTypeEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testTokenTypeComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }
    
    @Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
    
}
