import org.junit.Test;
import static org.junit.Assert.*;

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
        
        token = new Token.Character();
        assertEquals("Character", token.tokenType());
        
        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("example");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        Token token = doctype.reset();
        assertTrue(token instanceof Token.Doctype);
        doctype = (Token.Doctype) token;
        
        assertEquals("", doctype.name.toString());
        assertEquals(null, doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "example";
        startTag.normalName = "example";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        Token token = startTag.reset();
        assertTrue(token instanceof Token.StartTag);
        startTag = (Token.StartTag) token;
        
        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertEquals(null, startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "example";
        endTag.normalName = "example";
        
        Token token = endTag.reset();
        assertTrue(token instanceof Token.EndTag);
        endTag = (Token.EndTag) token;
        
        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("example");
        comment.bogus = true;
        
        Token token = comment.reset();
        assertTrue(token instanceof Token.Comment);
        comment = (Token.Comment) token;
        
        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "example";
        
        Token token = character.reset();
        assertTrue(token instanceof Token.Character);
        character = (Token.Character) token;
        
        assertEquals(null, character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token token = eof.reset();
        assertTrue(token instanceof Token.EOF);
    }
    
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.tagName = "example";
        startTag.attributes = attributes;
        startTag.normalName = "example";
        
        Token.StartTag newStartTag = startTag.nameAttr("newExample", attributes);
        assertEquals("newExample", newStartTag.tagName);
        assertEquals(attributes, newStartTag.attributes);
        assertEquals("newexample", newStartTag.normalName);
    }
    
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "example";
        
        assertEquals("<example>", startTag.toString());
        
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        startTag.attributes = attributes;
        assertEquals("<example attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "example";
        
        assertEquals("</example>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("example");
        
        assertEquals("example", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "example";
        
        assertEquals("example", character.getData());
    }
}