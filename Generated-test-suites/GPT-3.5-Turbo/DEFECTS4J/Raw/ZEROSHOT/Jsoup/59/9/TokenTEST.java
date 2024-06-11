package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

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
        assertNotNull(token.tokenType());
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
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");

        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "pubSysKey";

        assertEquals("pubSysKey", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("publicIdentifier");

        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemIdentifier");

        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue = new StringBuilder("pendingAttributeValue");
        startTag.pendingAttributeValueS = "pendingAttributeValueS";
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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("name", attributes);

        assertEquals("name", startTag.tagName);
        assertEquals("name", startTag.normalName);
        assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "name";
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("attr", "value"));

        assertEquals("<name attr=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "name";

        assertEquals("</name>", endTag.toString());
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
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("<!--data-->", comment.toString());
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
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.toString());
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
        token.type = Token.TokenType.EOF;

        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeValues() {
        assertEquals(6, Token.TokenType.values().length);
        assertEquals(Token.TokenType.Doctype, Token.TokenType.values()[0]);
        assertEquals(Token.TokenType.StartTag, Token.TokenType.values()[1]);
        assertEquals(Token.TokenType.EndTag, Token.TokenType.values()[2]);
        assertEquals(Token.TokenType.Comment, Token.TokenType.values()[3]);
        assertEquals(Token.TokenType.Character, Token.TokenType.values()[4]);
        assertEquals(Token.TokenType.EOF, Token.TokenType.values()[5]);
    }
}