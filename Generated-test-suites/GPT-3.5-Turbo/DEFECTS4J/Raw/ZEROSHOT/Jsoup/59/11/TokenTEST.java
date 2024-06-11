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
        
        String expected = "Token";
        String actual = token.tokenType();
        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TEST");
        doctype.pubSysKey = "KEY";
        doctype.publicIdentifier.append("PUBLIC");
        doctype.systemIdentifier.append("SYSTEM");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        
        tag.tagName = "TEST";
        tag.normalName = "test";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "valueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        
        Token.Tag resetTag = tag.reset();
        
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "TEST";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("attr", "value"));
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "TEST";
        endTag.normalName = "test";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("TEST");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "TEST";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEof = eof.reset();
        
        assertNotNull(resetEof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isDoctype();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Token.Doctype expected = null;
        Token.Doctype actual = token.asDoctype();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isStartTag();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Token.StartTag expected = null;
        Token.StartTag actual = token.asStartTag();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isEndTag();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Token.EndTag expected = null;
        Token.EndTag actual = token.asEndTag();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Token.Comment expected = null;
        Token.Comment actual = token.asComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isCharacter();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Token.Character expected = null;
        Token.Character actual = token.asCharacter();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        boolean expected = false;
        boolean actual = token.isEOF();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeString() {
        Token.TokenType tokenType = Token.TokenType.Doctype;
        
        String expected = "Doctype";
        String actual = tokenType.name();
        assertEquals(expected, actual);
    }
}