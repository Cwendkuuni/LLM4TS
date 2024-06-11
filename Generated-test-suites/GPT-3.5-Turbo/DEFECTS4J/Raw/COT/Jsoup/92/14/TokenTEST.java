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
        assertEquals(null, token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        Token result = doctype.reset();
        assertEquals("", result.getName());
        assertEquals(null, result.getPubSysKey());
        assertEquals("", result.getPublicIdentifier());
        assertEquals("", result.getSystemIdentifier());
        assertFalse(result.isForceQuirks());
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
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValueS = "content";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        Token result = tag.reset();
        assertEquals(null, result.tagName);
        assertEquals(null, result.normalName);
        assertEquals(null, result.pendingAttributeName);
        assertEquals("", result.pendingAttributeValueS);
        assertFalse(result.hasEmptyAttributeValue);
        assertFalse(result.hasPendingAttributeValue);
        assertFalse(result.isSelfClosing());
        assertEquals(null, result.getAttributes());
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes());
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "div";
        startTag.attributes = new Attributes();
        assertEquals("<div>", startTag.toString());
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
        comment.data.append("comment");
        comment.bogus = true;
        Token result = comment.reset();
        assertEquals("", result.getData());
        assertFalse(result.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "A";
        Token result = character.reset();
        assertEquals(null, result.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");
        assertEquals("<![CDATA[data]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        Token result = eof.reset();
        assertEquals(eof, result);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
            public TokenType getType() {
                return TokenType.Doctype;
            }
        };
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
            public TokenType getType() {
                return TokenType.StartTag;
            }
        };
        assertTrue(token.isStartTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
            public TokenType getType() {
                return TokenType.EndTag;
            }
        };
        assertTrue(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
            public TokenType getType() {
                return TokenType.Comment;
            }
        };
        assertTrue(token.isComment());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
            public TokenType getType() {
                return TokenType.Character;
            }
        };
        assertTrue(token.isCharacter());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCData() {
        Token token = new Token.CData("data");
        assertTrue(token.isCData());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
    }
}