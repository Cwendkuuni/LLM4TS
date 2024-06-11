import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("doctype");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        doctype = doctype.reset();
        
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        tag.tagName = "tag";
        tag.normalName = "tag";
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "value";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag = tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag = startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        
        endTag.tagName = "tag";
        endTag.normalName = "tag";
        endTag.pendingAttributeName = "attribute";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        
        endTag = endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("data");
        comment.bogus = true;
        
        comment = comment.reset();
        
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "data";
        
        character = character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof = eof.reset();
        
        assertNotNull(eof);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isDoctype());
        
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isStartTag());
        
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isEndTag());
        
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isComment());
        
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isCharacter());
        
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isEOF());
        
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
}