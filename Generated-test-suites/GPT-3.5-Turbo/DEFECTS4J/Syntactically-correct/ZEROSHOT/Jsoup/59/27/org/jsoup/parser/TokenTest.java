package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        Assert.assertEquals("Token", token.tokenType());
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

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue.append("pendingAttributeValue");
        startTag.pendingAttributeValueS = "pendingAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;

        startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "pendingAttributeName";
        endTag.pendingAttributeValue.append("pendingAttributeValue");
        endTag.pendingAttributeValueS = "pendingAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;

        endTag.reset();

        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
        Assert.assertNull(endTag.pendingAttributeName);
        Assert.assertEquals("", endTag.pendingAttributeValue.toString());
        Assert.assertNull(endTag.pendingAttributeValueS);
        Assert.assertFalse(endTag.hasEmptyAttributeValue);
        Assert.assertFalse(endTag.hasPendingAttributeValue);
        Assert.assertFalse(endTag.selfClosing);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertNotNull(eof);
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

        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Token token = doctype;

        Assert.assertEquals(doctype, token.asDoctype());
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

        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Token token = startTag;

        Assert.assertEquals(startTag, token.asStartTag());
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

        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Token token = endTag;

        Assert.assertEquals(endTag, token.asEndTag());
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

        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        Token token = comment;

        Assert.assertEquals(comment, token.asComment());
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

        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        Token token = character;

        Assert.assertEquals(character, token.asCharacter());
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

        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeEnumeration() {
        Assert.assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        Assert.assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        Assert.assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        Assert.assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        Assert.assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        Assert.assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}