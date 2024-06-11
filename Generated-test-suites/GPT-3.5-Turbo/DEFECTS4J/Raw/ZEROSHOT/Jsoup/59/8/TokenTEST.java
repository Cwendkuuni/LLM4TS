import static org.junit.Assert.*;
import org.junit.*;

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
        assertNull(token.reset());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertTrue(doctype.name.toString().isEmpty());
        assertNull(doctype.pubSysKey);
        assertTrue(doctype.publicIdentifier.toString().isEmpty());
        assertTrue(doctype.systemIdentifier.toString().isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("pub", doctype.getPubSysKey());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
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
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("123");
        tag.pendingAttributeValueS = "123";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals(0, tag.pendingAttributeValue.length());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "123");

        assertEquals("<div id=\"123\">", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.data("This is a character");

        assertEquals("This is a character", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.reset());
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

        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.asDoctype());

        Token.Doctype doctype = new Token.Doctype();
        assertSame(doctype, doctype.asDoctype());
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

        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.asStartTag());

        Token.StartTag startTag = new Token.StartTag();
        assertSame(startTag, startTag.asStartTag());
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

        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.asEndTag());

        Token.EndTag endTag = new Token.EndTag();
        assertSame(endTag, endTag.asEndTag());
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

        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.asComment());

        Token.Comment comment = new Token.Comment();
        assertSame(comment, comment.asComment());
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

        Token.Character character = new Token.Character();
        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNull(token.asCharacter());

        Token.Character character = new Token.Character();
        assertSame(character, character.asCharacter());
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

        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}