import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class TokenTest {
    
    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;
    
    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character("data");
        eofToken = new Token.EOF();
    }
    
    @Test
    public void testTokenType_Doctype() {
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
    }
    
    @Test
    public void testTokenType_StartTag() {
        assertEquals(Token.TokenType.StartTag, startTagToken.type);
    }
    
    @Test
    public void testTokenType_EndTag() {
        assertEquals(Token.TokenType.EndTag, endTagToken.type);
    }
    
    @Test
    public void testTokenType_Comment() {
        assertEquals(Token.TokenType.Comment, commentToken.type);
    }
    
    @Test
    public void testTokenType_Character() {
        assertEquals(Token.TokenType.Character, characterToken.type);
    }
    
    @Test
    public void testTokenType_EOF() {
        assertEquals(Token.TokenType.EOF, eofToken.type);
    }
    
    @Test
    public void testGetType_Doctype() {
        assertEquals("Doctype", doctypeToken.tokenType());
    }
    
    @Test
    public void testGetType_StartTag() {
        assertEquals("StartTag", startTagToken.tokenType());
    }
    
    @Test
    public void testGetType_EndTag() {
        assertEquals("EndTag", endTagToken.tokenType());
    }
    
    @Test
    public void testGetType_Comment() {
        assertEquals("Comment", commentToken.tokenType());
    }
    
    @Test
    public void testGetType_Character() {
        assertEquals("Character", characterToken.tokenType());
    }
    
    @Test
    public void testGetType_EOF() {
        assertEquals("EOF", eofToken.tokenType());
    }
    
    @Test
    public void testDoctype_getName_empty() {
        assertEquals("", doctypeToken.getName());
    }
    
    @Test
    public void testDoctype_getPublicIdentifier_empty() {
        assertEquals("", doctypeToken.getPublicIdentifier());
    }
    
    @Test
    public void testDoctype_getSystemIdentifier_empty() {
        assertEquals("", doctypeToken.getSystemIdentifier());
    }
    
    @Test
    public void testDoctype_isForceQuirks_false() {
        assertFalse(doctypeToken.isForceQuirks());
    }
    
    @Test
    public void testTagName() {
        startTagToken.name("tag");
        assertEquals("tag", startTagToken.name());
    }
    
    @Test
    public void testIsSelfClosing_false() {
        assertFalse(startTagToken.isSelfClosing());
    }
    
    @Test
    public void testGetAttributes_null() {
        assertNull(startTagToken.getAttributes());
    }
    
    @Test
    public void testAppendTagName_empty() {
        startTagToken.appendTagName("");
        assertEquals("", startTagToken.tagName);
    }
    
    @Test
    public void testAppendTagName_notEmpty() {
        startTagToken.appendTagName("t");
        assertEquals("t", startTagToken.tagName);
        
        startTagToken.appendTagName("a");
        assertEquals("ta", startTagToken.tagName);
    }
    
    @Test
    public void testAppendTagName_char() {
        startTagToken.appendTagName('t');
        assertEquals("t", startTagToken.tagName);
        
        startTagToken.appendTagName('a');
        assertEquals("ta", startTagToken.tagName);
    }
    
    @Test
    public void testAppendAttributeName_empty() {
        startTagToken.appendAttributeName("");
        assertEquals("", startTagToken.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeName_notEmpty() {
        startTagToken.appendAttributeName("a");
        assertEquals("a", startTagToken.pendingAttributeName);
        
        startTagToken.appendAttributeName("t");
        assertEquals("at", startTagToken.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeName_char() {
        startTagToken.appendAttributeName('a');
        assertEquals("a", startTagToken.pendingAttributeName);
        
        startTagToken.appendAttributeName('t');
        assertEquals("at", startTagToken.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeValue_empty() {
        startTagToken.appendAttributeValue("");
        assertEquals("", startTagToken.pendingAttributeValue.toString());
    }
    
    @Test
    public void testAppendAttributeValue_notEmpty() {
        startTagToken.appendAttributeValue("a");
        assertEquals("a", startTagToken.pendingAttributeValue.toString());
        
        startTagToken.appendAttributeValue("bcd");
        assertEquals("abcd", startTagToken.pendingAttributeValue.toString());
    }
    
    @Test
    public void testAppendAttributeValue_char() {
        startTagToken.appendAttributeValue('a');
        assertEquals("a", startTagToken.pendingAttributeValue.toString());
        
        startTagToken.appendAttributeValue('b');
        assertEquals("ab", startTagToken.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTag_toString_noAttributes() {
        startTagToken.tagName = "tag";
        assertEquals("<tag>", startTagToken.toString());
    }
    
    @Test
    public void testStartTag_toString_withAttributes() {
        startTagToken.tagName = "tag";
        startTagToken.attributes = new Attributes();
        startTagToken.attributes.put(new Attribute("attr1", "value1"));
        startTagToken.attributes.put(new Attribute("attr2", "value2"));
        
        assertEquals("<tag attr1=\"value1\" attr2=\"value2\">", startTagToken.toString());
    }
    
    @Test
    public void testEndTag_toString() {
        endTagToken.tagName = "tag";
        assertEquals("</tag>", endTagToken.toString());
    }
    
    @Test
    public void testComment_getData_empty() {
        assertEquals("", commentToken.getData());
    }
    
    @Test
    public void testComment_getData_notEmpty() {
        commentToken.data.append("test");
        assertEquals("test", commentToken.getData());
    }
    
    @Test
    public void testCharacter_getData() {
        assertEquals("data", characterToken.getData());
    }
    
    @Test
    public void testEOF_type() {
        assertEquals(Token.TokenType.EOF, eofToken.type);
    }
    
    @Test
    public void testIsDoctype() {
        assertTrue(doctypeToken.isDoctype());
        assertFalse(startTagToken.isDoctype());
        assertFalse(endTagToken.isDoctype());
        assertFalse(commentToken.isDoctype());
        assertFalse(characterToken.isDoctype());
        assertFalse(eofToken.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        assertEquals(doctypeToken, doctypeToken.asDoctype());
        assertNull(startTagToken.asDoctype());
        assertNull(endTagToken.asDoctype());
        assertNull(commentToken.asDoctype());
        assertNull(characterToken.asDoctype());
        assertNull(eofToken.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        assertFalse(doctypeToken.isStartTag());
        assertTrue(startTagToken.isStartTag());
        assertFalse(endTagToken.isStartTag());
        assertFalse(commentToken.isStartTag());
        assertFalse(characterToken.isStartTag());
        assertFalse(eofToken.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        assertNull(doctypeToken.asStartTag());
        assertEquals(startTagToken, startTagToken.asStartTag());
        assertNull(endTagToken.asStartTag());
        assertNull(commentToken.asStartTag());
        assertNull(characterToken.asStartTag());
        assertNull(eofToken.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        assertFalse(doctypeToken.isEndTag());
        assertFalse(startTagToken.isEndTag());
        assertTrue(endTagToken.isEndTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(eofToken.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        assertNull(doctypeToken.asEndTag());
        assertNull(startTagToken.asEndTag());
        assertEquals(endTagToken, endTagToken.asEndTag());
        assertNull(commentToken.asEndTag());
        assertNull(characterToken.asEndTag());
        assertNull(eofToken.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        assertFalse(doctypeToken.isComment());
        assertFalse(startTagToken.isComment());
        assertFalse(endTagToken.isComment());
        assertTrue(commentToken.isComment());
        assertFalse(characterToken.isComment());
        assertFalse(eofToken.isComment());
    }
    
    @Test
    public void testAsComment() {
        assertNull(doctypeToken.asComment());
        assertNull(startTagToken.asComment());
        assertNull(endTagToken.asComment());
        assertEquals(commentToken, commentToken.asComment());
        assertNull(characterToken.asComment());
        assertNull(eofToken.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        assertFalse(doctypeToken.isCharacter());
        assertFalse(startTagToken.isCharacter());
        assertFalse(endTagToken.isCharacter());
        assertFalse(commentToken.isCharacter());
        assertTrue(characterToken.isCharacter());
        assertFalse(eofToken.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        assertNull(doctypeToken.asCharacter());
        assertNull(startTagToken.asCharacter());
        assertNull(endTagToken.asCharacter());
        assertNull(commentToken.asCharacter());
        assertEquals(characterToken, characterToken.asCharacter());
        assertNull(eofToken.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        assertFalse(doctypeToken.isEOF());
        assertFalse(startTagToken.isEOF());
        assertFalse(endTagToken.isEOF());
        assertFalse(commentToken.isEOF());
        assertFalse(characterToken.isEOF());
        assertTrue(eofToken.isEOF());
    }
}