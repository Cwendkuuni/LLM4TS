import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(TokenType.Doctype, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("content");
        tag.pendingAttributeValueS = "val";
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
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        tag.nameAttr("div", new Attributes());

        assertEquals("<div>", tag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag tag = new Token.EndTag();
        tag.name("div");

        assertEquals("</div>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("Test character");

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("Test CDATA");

        assertEquals("<![CDATA[Test CDATA]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token doctype = new Token.Doctype();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(doctype.isDoctype());
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token doctype = new Token.Doctype();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(doctype, doctype.asDoctype());
        assertNull(token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token startTag = new Token.StartTag();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(startTag.isStartTag());
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(startTag, startTag.asStartTag());
        assertNull(token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token endTag = new Token.EndTag();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(endTag.isEndTag());
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(endTag, endTag.asEndTag());
        assertNull(token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token comment = new Token.Comment();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(comment.isComment());
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(comment, comment.asComment());
        assertNull(token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(character.isCharacter());
        assertFalse(token.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token.CData cData = new Token.CData("Test CDATA");
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(cData.isCData());
        assertFalse(token.isCData());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertEquals(character, character.asCharacter());
        assertNull(token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token token = new Token() {
            Token reset() {
                return null;
            }
        };

        assertTrue(eof.isEOF());
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}