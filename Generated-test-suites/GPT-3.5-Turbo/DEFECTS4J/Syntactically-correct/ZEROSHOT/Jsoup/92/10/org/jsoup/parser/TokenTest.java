import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Before
    public void setUp() {
        // Setup code here
    }
    
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.pubSysKey = "testPubSysKey";
        doctype.publicIdentifier.append("testPublicIdentifier");
        doctype.systemIdentifier.append("testSystemIdentifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "testTagName";
        tag.normalName = "testNormalName";
        tag.pendingAttributeName = "testPendingAttributeName";
        tag.pendingAttributeValue.append("testPendingAttributeValue");
        tag.pendingAttributeValueS = "testPendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }
    
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("testName", new Attributes());
        
        assertEquals("testName", startTag.tagName);
        assertEquals("testName", startTag.normalName);
    }
    
    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("testTag", new Attributes());
        
        assertEquals("<testTag>", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTag";
        
        assertEquals("<testTag>", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "testTag";
        
        assertEquals("</testTag>", endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "testData";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("testData");
        
        assertEquals("<![CDATA[testData]]>", cdata.toString());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token result = eof.reset();
        
        assertEquals(eof, result);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.asDoctype() instanceof Token.Doctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        
        assertTrue(token.asStartTag() instanceof Token.StartTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        
        assertTrue(token.asEndTag() instanceof Token.EndTag);
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        
        assertTrue(token.asComment() instanceof Token.Comment);
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsCData() {
        Token token = new Token.CData("testData");
        
        assertTrue(token.isCData());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        
        assertTrue(token.asCharacter() instanceof Token.Character);
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        
        assertTrue(token.isEOF());
    }
}