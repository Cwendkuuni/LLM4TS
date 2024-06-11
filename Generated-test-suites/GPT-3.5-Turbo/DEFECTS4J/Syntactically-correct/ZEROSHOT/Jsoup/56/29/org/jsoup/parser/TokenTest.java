package org.jsoup.parser;

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
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token reset() {
                return null;
            }
        };
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("myId");
        tag.pendingAttributeValueS = "myId";
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
        assertFalse(tag.isSelfClosing());
        assertNull(tag.attributes);
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        
        tag.tagName = "div";
        tag.normalName = "div";
        
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");
        
        startTag.reset();
        
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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        
        startTag.nameAttr("div", attributes);
        
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertEquals(attributes, startTag.getAttributes());
    }
    
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");
        
        String expected = "<div class=\"container\">";
        
        assertEquals(expected, startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        String expected = "</div>";
        
        assertEquals(expected, endTag.toString());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        assertEquals("This is a character", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertSame(eof, null);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Doctype;
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        
        Token.Doctype doctype = token.asDoctype();
        
        assertEquals(Token.Doctype.class, doctype.getClass());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.StartTag;
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        
        Token.StartTag startTag = token.asStartTag();
        
        assertEquals(Token.StartTag.class, startTag.getClass());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EndTag;
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        
        Token.EndTag endTag = token.asEndTag();
        
        assertEquals(Token.EndTag.class, endTag.getClass());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Comment;
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        
        Token.Comment comment = token.asComment();
        
        assertEquals(Token.Comment.class, comment.getClass());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Character;
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        
        Token.Character character = token.asCharacter();
        
        assertEquals(Token.Character.class, character.getClass());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EOF;
        
        assertTrue(token.isEOF());
    }
    
    @Test
    public void testTokenTypeenumValues() {
        Token.TokenType[] tokenTypes = Token.TokenType.values();
        Token.TokenType[] expected = {Token.TokenType.Doctype, Token.TokenType.StartTag, Token.TokenType.EndTag, Token.TokenType.Comment, Token.TokenType.Character, Token.TokenType.EOF};
        
        assertArrayEquals(expected, tokenTypes);
    }
}