import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.Token.*;

public class TokenTest {
    
    // Doctype test cases
    @Test
    public void testDoctype_reset() {
        Doctype doctype = new Doctype();
        doctype.reset();
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    // Tag test cases
    @Test
    public void testTag_reset() {
        Tag tag = new Tag() {};
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
    public void testTag_name() {
        Tag tag = new Tag() {};
        tag.name("test");
        assertEquals("test", tag.name());
        assertEquals("test", tag.normalName());
    }
    
    @Test
    public void testTag_isSelfClosing() {
        Tag tag = new Tag() {};
        assertFalse(tag.isSelfClosing());
    }
    
    // StartTag test cases
    @Test
    public void testStartTag_reset() {
        StartTag startTag = new StartTag();
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
    public void testStartTag_nameAttr() {
        StartTag startTag = new StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("test", attributes);
        assertEquals("test", startTag.name());
        assertEquals("test", startTag.normalName());
        assertSame(attributes, startTag.getAttributes());
    }
    
    // EndTag test cases
    @Test
    public void testEndTag_reset() {
        EndTag endTag = new EndTag();
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
    
    // Comment test cases
    @Test
    public void testComment_reset() {
        Comment comment = new Comment();
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    // Character test cases
    @Test
    public void testCharacter_reset() {
        Character character = new Character();
        character.reset();
        assertNull(character.getData());
    }
    
    @Test
    public void testCharacter_data() {
        Character character = new Character();
        character.data("test");
        assertEquals("test", character.getData());
    }
    
    // EOF test cases
    @Test
    public void testEOF_reset() {
        EOF eof = new EOF();
        eof.reset();
        assertSame(eof, eof.reset());
    }
    
    // Utility methods test cases
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.Doctype;
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.StartTag;
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.EndTag;
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.Comment;
        assertTrue(token.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.Character;
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = TokenType.EOF;
        assertTrue(token.isEOF());
    }
}