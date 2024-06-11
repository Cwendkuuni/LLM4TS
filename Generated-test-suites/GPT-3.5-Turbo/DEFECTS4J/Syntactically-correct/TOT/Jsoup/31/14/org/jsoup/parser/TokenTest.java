import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("test");
        assertEquals("test", tag.name());
    }
    
    @Test
    public void testSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testAttributes() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeName("attr1");
        tag.appendAttributeValue("value1");
        tag.newAttribute();
        tag.appendAttributeName("attr2");
        tag.appendAttributeValue("value2");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
    }
    
    @Test
    public void testAppendingTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("test");
        assertEquals("test", tag.tagName);
    }
    
    @Test
    public void testAppendingAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("attr");
        assertEquals("attr", tag.pendingAttributeName);
    }
    
    @Test
    public void testAppendingAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagToString() {
        Token.Tag tag = new Token.StartTag();
        tag.name("test");
        tag.newAttribute();
        tag.appendAttributeName("attr1");
        tag.appendAttributeValue("value1");
        tag.newAttribute();
        tag.appendAttributeName("attr2");
        tag.appendAttributeValue("value2");
        String expected = "<test attr1=\"value1\" attr2=\"value2\">";
        assertEquals(expected, tag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.Tag tag = new Token.EndTag();
        tag.name("test");
        String expected = "</test>";
        assertEquals(expected, tag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("A");
        assertEquals("A", character.getData());
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
        Token token = new Token.Character("A");
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}