// Test cases for the Token class

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
        token.type = Token.TokenType.Doctype;
        assertEquals("Doctype", token.tokenType());

        token.type = Token.TokenType.StartTag;
        assertEquals("StartTag", token.tokenType());

        token.type = Token.TokenType.EndTag;
        assertEquals("EndTag", token.tokenType());

        token.type = Token.TokenType.Comment;
        assertEquals("Comment", token.tokenType());

        token.type = Token.TokenType.Character;
        assertEquals("Character", token.tokenType());

        token.type = Token.TokenType.EOF;
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "TAG";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertNull(eof.reset());
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

        token.type = Token.TokenType.StartTag;
        assertFalse(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertSame(doctype, token.asDoctype());

        Token.StartTag startTag = new Token.StartTag();
        token = startTag;
        assertNull(token.asDoctype());
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

        token.type = Token.TokenType.Doctype;
        assertFalse(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertSame(startTag, token.asStartTag());

        Token.Comment comment = new Token.Comment();
        token = comment;
        assertNull(token.asStartTag());
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

        token.type = Token.TokenType.Doctype;
        assertFalse(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertSame(endTag, token.asEndTag());

        Token.Character character = new Token.Character();
        token = character;
        assertNull(token.asEndTag());
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

        token.type = Token.TokenType.Doctype;
        assertFalse(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertSame(comment, token.asComment());

        Token.EOF eof = new Token.EOF();
        token = eof;
        assertNull(token.asComment());
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

        token.type = Token.TokenType.Doctype;
        assertFalse(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        assertSame(character, token.asCharacter());

        Token.Doctype doctype = new Token.Doctype();
        token = doctype;
        assertNull(token.asCharacter());
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

        token.type = Token.TokenType.Character;
        assertFalse(token.isEOF());
    }

    @Test
    public void testTokenTypeToString() {
        assertEquals("Doctype", Token.TokenType.Doctype.toString());
        assertEquals("StartTag", Token.TokenType.StartTag.toString());
        assertEquals("EndTag", Token.TokenType.EndTag.toString());
        assertEquals("Comment", Token.TokenType.Comment.toString());
        assertEquals("Character", Token.TokenType.Character.toString());
        assertEquals("EOF", Token.TokenType.EOF.toString());
    }
}