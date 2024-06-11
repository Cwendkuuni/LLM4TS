package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

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

        doctype.name.append("hello");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };

        tag.tagName = "hello";
        tag.normalName = "HELLO";
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "value";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertEquals("", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();

        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        startTag.nameAttr("tag", attributes);

        Assert.assertEquals("tag", startTag.tagName);
        Assert.assertEquals("tag", startTag.normalName);
        Assert.assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");

        Assert.assertEquals("<tag attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        Assert.assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("hello");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();

        character.data = "hello";

        character.reset();

        Assert.assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertEquals(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Assert.assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Assert.assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Assert.assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Assert.assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        Assert.assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();

        Assert.assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        Assert.assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        Assert.assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        Assert.assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        Assert.assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        Assert.assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        Assert.assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        Assert.assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        Assert.assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
    
}