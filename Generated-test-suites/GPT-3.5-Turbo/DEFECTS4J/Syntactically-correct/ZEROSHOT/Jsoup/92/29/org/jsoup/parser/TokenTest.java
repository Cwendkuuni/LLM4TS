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
        
        assertEquals(TokenType.EOF, token.type);
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.name.append("doctype");
        doctype.systemIdentifier.append("system");
        doctype.publicIdentifier.append("public");
        doctype.pubSysKey = "key";
        
        doctype.reset();
        
        assertFalse(doctype.forceQuirks);
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals(null, doctype.getPubSysKey());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.selfClosing = true;
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;

        startTag.reset();

        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertFalse(startTag.selfClosing);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals(null, startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "TAG";
        
        endTag.reset();
        
        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
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
        
        assertEquals(null, character.data);
    }
    
    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");
        
        assertEquals("<![CDATA[data]]>", cData.toString());
    }
    
    @Test
    public void testEofReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertEquals(TokenType.EOF, eof.type);
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
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertEquals(null, doctype.asDoctype());
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
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertEquals(null, startTag.asStartTag());
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
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertEquals(null, endTag.asEndTag());
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
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertEquals(null, comment.asComment());
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
    }
    
    @Test
    public void testIsCDATA() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertFalse(token.isCData());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        
        assertEquals(null, character.asCharacter());
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
    }
}