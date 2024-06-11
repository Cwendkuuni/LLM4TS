import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

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
        
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetMethods() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        assertEquals("name", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        
        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "attribName";
        tag.pendingAttributeValue.append("attribValue");
        tag.pendingAttributeValueS = "attribValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        
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
        Attributes attributes = new Attributes();
        
        attributes.put(new Attribute("attribute", "value"));
        
        startTag.nameAttr("tag", attributes);
        
        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
    }
    
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.nameAttr("tag", null);
        
        assertEquals("<tag>", startTag.toString());
        
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attribute", "value"));
        startTag.attributes = attributes;
        
        assertEquals("<tag attribute=\"value\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        
        endTag.name("tag");
        
        assertEquals("</tag>", endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        
        character.data("data");
        
        assertEquals("data", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        assertNull(eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isDoctype());
        
        token.type = Token.TokenType.Doctype;
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        Token token = doctype;
        
        assertEquals(doctype, token.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isStartTag());
        
        token.type = Token.TokenType.StartTag;
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        Token token = startTag;
        
        assertEquals(startTag, token.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isEndTag());
        
        token.type = Token.TokenType.EndTag;
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        Token token = endTag;
        
        assertEquals(endTag, token.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isComment());
        
        token.type = Token.TokenType.Comment;
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        Token token = comment;
        
        assertEquals(comment, token.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isCharacter());
        
        token.type = Token.TokenType.Character;
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        
        Token token = character;
        
        assertEquals(character, token.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertFalse(token.isEOF());
        
        token.type = Token.TokenType.EOF;
        
        assertTrue(token.isEOF());
    }

}