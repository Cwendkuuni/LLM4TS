import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertEquals("Token", token.tokenType());
    }

    @Test
    void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("name");
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;
        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
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
    void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "tag";
        endTag.normalName = "tag";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "valueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
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

    @Test
    void testCommentReset() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("comment");
        comment.bogus = true;
        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    void testCharacterReset() {
        Token.Character character = new Token.Character();

        character.data = "data";
        character.reset();

        assertNull(character.data);
    }

    @Test
    void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.reset());
    }

    @Test
    void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Doctype;

        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.StartTag;

        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EndTag;

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Comment;

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Character;

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.EOF;

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }

    @Test
    void testTokenTypeOfDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    void testTokenTypeOfStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals("StartTag", startTag.tokenType());
    }

    @Test
    void testTokenTypeOfEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals("EndTag", endTag.tokenType());
    }

    @Test
    void testTokenTypeOfComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals("Comment", comment.tokenType());
    }

    @Test
    void testTokenTypeOfCharacter() {
        Token.Character character = new Token.Character();

        assertEquals("Character", character.tokenType());
    }

    @Test
    void testTokenTypeOfEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
    }

}