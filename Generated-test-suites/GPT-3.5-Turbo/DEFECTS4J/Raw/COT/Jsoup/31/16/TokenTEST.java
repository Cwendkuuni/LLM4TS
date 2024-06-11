import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token() {
        };
        
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        
        assertEquals("testName", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("testPublic");
        
        assertEquals("testPublic", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("testSystem");
        
        assertEquals("testSystem", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagIsSelfClosingFalse() {
        Token.Tag tag = new Token.Tag() {
        };
        
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.pendingAttributeName = "testAttribute";
        
        tag.newAttribute();
        
        assertNull(tag.pendingAttributeName);
        assertNotNull(tag.attributes);
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.pendingAttributeName = "testAttribute";
        
        tag.finaliseTag();
        
        assertNull(tag.pendingAttributeName);
    }
    
    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.tagName = "testTagName";
        
        assertEquals("testTagName", tag.name());
    }
    
    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.tagName = "testTagName";
        
        tag.appendTagName("Append");
        
        assertEquals("testTagNameAppend", tag.tagName);
    }
    
    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.pendingAttributeName = "testAttributeName";
        
        tag.appendAttributeName("Append");
        
        assertEquals("testAttributeNameAppend", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag() {
        };
        
        tag.pendingAttributeValue = new StringBuilder("testAttributeValue");
        
        tag.appendAttributeValue("Append");
        
        assertEquals("testAttributeValueAppend", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttrs() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testStartTag";
        
        assertEquals("<testStartTag>", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithAttrs() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testStartTag";
        startTag.attributes = new Attributes();
        Attribute attribute = new Attribute("testAttribute", "testValue");
        startTag.attributes.put(attribute);
        
        assertEquals("<testStartTag testAttribute=\"testValue\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "testEndTag";
        
        assertEquals("</testEndTag>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        
        assertEquals("testData", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("testData");
        
        assertEquals("testData", character.getData());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eofToken = new Token.EOF();
        
        assertTrue(eofToken.isEOF());
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertEquals(doctype, doctype.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertEquals(startTag, startTag.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertEquals(endTag, endTag.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertEquals(comment, comment.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("testData");
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("testData");
        
        assertEquals(character, character.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eofToken = new Token.EOF();
        
        assertTrue(eofToken.isEOF());
    }
}