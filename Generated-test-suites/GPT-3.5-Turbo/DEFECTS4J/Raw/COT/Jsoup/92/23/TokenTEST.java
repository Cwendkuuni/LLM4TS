
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
    }
    
    @After
    public void tearDown() {
        token = null;
    }
    
    @Test
    public void testTokenType() {
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
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
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "testId");
        assertEquals("<div id=\"testId\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assertEquals("</div>", endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.reset();
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.reset();
        assertNull(character.data);
    }
    
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("This is a CDATA section");
        assertEquals("<![CDATA[This is a CDATA section]]>", cdata.toString());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertEquals(eof, eof.reset());
    }
}