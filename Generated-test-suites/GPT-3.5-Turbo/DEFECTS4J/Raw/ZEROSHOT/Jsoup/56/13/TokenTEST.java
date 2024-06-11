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
        
        String expected = "Token";
        String actual = token.tokenType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        String expectedName = "";
        String expectedPublicIdentifier = "";
        String expectedSystemIdentifier = "";
        boolean expectedForceQuirks = false;
        
        String actualName = doctype.getName();
        String actualPublicIdentifier = doctype.getPublicIdentifier();
        String actualSystemIdentifier = doctype.getSystemIdentifier();
        boolean actualForceQuirks = doctype.isForceQuirks();
        
        assertEquals(expectedName, actualName);
        assertEquals(expectedPublicIdentifier, actualPublicIdentifier);
        assertEquals(expectedSystemIdentifier, actualSystemIdentifier);
        assertEquals(expectedForceQuirks, actualForceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("main");
        startTag.pendingAttributeValueS = "main";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new org.jsoup.nodes.Attributes();
        startTag.attributes.put(new org.jsoup.nodes.Attribute("id", "main"));
        
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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
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
        character.data = "A";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertNotNull(eof);
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
        
        boolean actual = token.isDoctype();
        
        assertTrue(actual);
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        
        Token.Doctype actual = token.asDoctype();
        
        assertNotNull(actual);
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
        
        boolean actual = token.isStartTag();
        
        assertTrue(actual);
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        
        Token.StartTag actual = token.asStartTag();
        
        assertNotNull(actual);
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
        
        boolean actual = token.isEndTag();
        
        assertTrue(actual);
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        
        Token.EndTag actual = token.asEndTag();
        
        assertNotNull(actual);
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
        
        boolean actual = token.isComment();
        
        assertTrue(actual);
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        
        Token.Comment actual = token.asComment();
        
        assertNotNull(actual);
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
        
        boolean actual = token.isCharacter();
        
        assertTrue(actual);
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        
        Token.Character actual = token.asCharacter();
        
        assertNotNull(actual);
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
        
        boolean actual = token.isEOF();
        
        assertTrue(actual);
    }
}