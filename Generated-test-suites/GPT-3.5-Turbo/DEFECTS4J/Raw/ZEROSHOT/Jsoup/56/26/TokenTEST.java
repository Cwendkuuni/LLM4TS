import org.junit.Test;
import static org.junit.Assert.*;

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
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;
        
        assertEquals("HTML", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
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
        character.data = "Some text";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertEquals(eof, eof.reset());
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
        
        token.type = TokenType.Doctype;
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertNull(token.asDoctype());
        
        token.type = TokenType.Doctype;
        
        assertNotNull(token.asDoctype());
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
        
        token.type = TokenType.StartTag;
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertNull(token.asStartTag());
        
        token.type = TokenType.StartTag;
        
        assertNotNull(token.asStartTag());
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
        
        token.type = TokenType.EndTag;
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertNull(token.asEndTag());
        
        token.type = TokenType.EndTag;
        
        assertNotNull(token.asEndTag());
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
        
        token.type = TokenType.Comment;
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertNull(token.asComment());
        
        token.type = TokenType.Comment;
        
        assertNotNull(token.asComment());
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
        
        token.type = TokenType.Character;
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertNull(token.asCharacter());
        
        token.type = TokenType.Character;
        
        assertNotNull(token.asCharacter());
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
        
        token.type = TokenType.EOF;
        
        assertTrue(token.isEOF());
    }

}