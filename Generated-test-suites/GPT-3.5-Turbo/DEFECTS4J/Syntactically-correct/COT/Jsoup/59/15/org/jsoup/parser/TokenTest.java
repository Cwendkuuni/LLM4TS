
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

        TokenType expected = TokenType.Doctype;
        token.type = expected;
        TokenType actual = token.type;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeString() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        String expected = "Doctype";
        String actual = token.tokenType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "test";
        doctype.publicIdentifier.append("test");
        doctype.systemIdentifier.append("test");
        doctype.forceQuirks = true;

        doctype.reset();

        String expectedName = "";
        String expectedPubSysKey = null;
        String expectedPublicIdentifier = "";
        String expectedSystemIdentifier = "";
        boolean expectedForceQuirks = false;

        String actualName = doctype.getName();
        String actualPubSysKey = doctype.getPubSysKey();
        String actualPublicIdentifier = doctype.getPublicIdentifier();
        String actualSystemIdentifier = doctype.getSystemIdentifier();
        boolean actualForceQuirks = doctype.isForceQuirks();

        Assert.assertEquals(expectedName, actualName);
        Assert.assertEquals(expectedPubSysKey, actualPubSysKey);
        Assert.assertEquals(expectedPublicIdentifier, actualPublicIdentifier);
        Assert.assertEquals(expectedSystemIdentifier, actualSystemIdentifier);
        Assert.assertEquals(expectedForceQuirks, actualForceQuirks);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "test";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = "test";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        String expectedTagName = null;
        String expectedNormalName = null;
        String expectedPendingAttributeName = null;
        String expectedPendingAttributeValue = "";
        String expectedPendingAttributeValueS = null;
        boolean expectedHasEmptyAttributeValue = false;
        boolean expectedHasPendingAttributeValue = false;
        boolean expectedSelfClosing = false;
        Attributes expectedAttributes = null;

        String actualTagName = startTag.tagName;
        String actualNormalName = startTag.normalName;
        String actualPendingAttributeName = startTag.pendingAttributeName;
        String actualPendingAttributeValue = startTag.pendingAttributeValue.toString();
        String actualPendingAttributeValueS = startTag.pendingAttributeValueS;
        boolean actualHasEmptyAttributeValue = startTag.hasEmptyAttributeValue;
        boolean actualHasPendingAttributeValue = startTag.hasPendingAttributeValue;
        boolean actualSelfClosing = startTag.selfClosing;
        Attributes actualAttributes = startTag.attributes;

        Assert.assertEquals(expectedTagName, actualTagName);
        Assert.assertEquals(expectedNormalName, actualNormalName);
        Assert.assertEquals(expectedPendingAttributeName, actualPendingAttributeName);
        Assert.assertEquals(expectedPendingAttributeValue, actualPendingAttributeValue);
        Assert.assertEquals(expectedPendingAttributeValueS, actualPendingAttributeValueS);
        Assert.assertEquals(expectedHasEmptyAttributeValue, actualHasEmptyAttributeValue);
        Assert.assertEquals(expectedHasPendingAttributeValue, actualHasPendingAttributeValue);
        Assert.assertEquals(expectedSelfClosing, actualSelfClosing);
        Assert.assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        endTag.normalName = "test";
        endTag.pendingAttributeName = "test";
        endTag.pendingAttributeValue.append("test");
        endTag.pendingAttributeValueS = "test";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        endTag.reset();

        String expectedTagName = null;
        String expectedNormalName = null;
        String expectedPendingAttributeName = null;
        String expectedPendingAttributeValue = "";
        String expectedPendingAttributeValueS = null;
        boolean expectedHasEmptyAttributeValue = false;
        boolean expectedHasPendingAttributeValue = false;
        boolean expectedSelfClosing = false;
        Attributes expectedAttributes = null;

        String actualTagName = endTag.tagName;
        String actualNormalName = endTag.normalName;
        String actualPendingAttributeName = endTag.pendingAttributeName;
        String actualPendingAttributeValue = endTag.pendingAttributeValue.toString();
        String actualPendingAttributeValueS = endTag.pendingAttributeValueS;
        boolean actualHasEmptyAttributeValue = endTag.hasEmptyAttributeValue;
        boolean actualHasPendingAttributeValue = endTag.hasPendingAttributeValue;
        boolean actualSelfClosing = endTag.selfClosing;
        Attributes actualAttributes = endTag.attributes;

        Assert.assertEquals(expectedTagName, actualTagName);
        Assert.assertEquals(expectedNormalName, actualNormalName);
        Assert.assertEquals(expectedPendingAttributeName, actualPendingAttributeName);
        Assert.assertEquals(expectedPendingAttributeValue, actualPendingAttributeValue);
        Assert.assertEquals(expectedPendingAttributeValueS, actualPendingAttributeValueS);
        Assert.assertEquals(expectedHasEmptyAttributeValue, actualHasEmptyAttributeValue);
        Assert.assertEquals(expectedHasPendingAttributeValue, actualHasPendingAttributeValue);
        Assert.assertEquals(expectedSelfClosing, actualSelfClosing);
        Assert.assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        String expectedData = "";
        boolean expectedBogus = false;

        String actualData = comment.getData();
        boolean actualBogus = comment.bogus;

        Assert.assertEquals(expectedData, actualData);
        Assert.assertEquals(expectedBogus, actualBogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";

        character.reset();

        String expectedData = null;

        String actualData = character.getData();

        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Token.EOF expected = eof;
        Token.EOF actual = eof.reset();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.Doctype;
        boolean actual = token.isDoctype();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.StartTag;
        boolean actual = token.isStartTag();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.EndTag;
        boolean actual = token.isEndTag();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.Comment;
        boolean actual = token.isComment();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.Character;
        boolean actual = token.isCharacter();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        boolean expected = true;
        token.type = TokenType.EOF;
        boolean actual = token.isEOF();

        Assert.assertEquals(expected, actual);
    }
}
