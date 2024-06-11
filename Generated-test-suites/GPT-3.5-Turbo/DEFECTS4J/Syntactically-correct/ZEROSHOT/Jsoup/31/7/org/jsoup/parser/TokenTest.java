package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            {
                type = TokenType.Doctype;
            }
        };
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        Assert.assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        Assert.assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        Assert.assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeName = "attribute1";
                pendingAttributeValue = new StringBuilder("value1");
            }
        };
        tag.newAttribute();
        Attributes attributes = tag.getAttributes();
        Assert.assertEquals(1, attributes.size());

        Attribute attribute = attributes.get("attribute1");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeName = "attribute1";
                pendingAttributeValue = new StringBuilder("value1");
            }
        };
        tag.finaliseTag();
        Attributes attributes = tag.getAttributes();
        Assert.assertEquals(1, attributes.size());

        Attribute attribute = attributes.get("attribute1");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "tag1";
        Assert.assertEquals("tag1", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName("tag1");
        Assert.assertEquals("tag1", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName('t');
        Assert.assertEquals("t", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeName = "attribute1";
            }
        };
        tag.appendAttributeName("attribute2");
        Assert.assertEquals("attribute1attribute2", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeName = "attribute1";
            }
        };
        tag.appendAttributeName('2');
        Assert.assertEquals("attribute12", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeValue = new StringBuilder("value1");
            }
        };
        tag.appendAttributeValue("value2");
        Assert.assertEquals("value1value2", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeValue = new StringBuilder("value1");
            }
        };
        tag.appendAttributeValue('2');
        Assert.assertEquals("value12", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag1";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attribute1", "value1");
        Assert.assertEquals("<tag1 attribute1=\"value1\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag1";
        Assert.assertEquals("</tag1>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data1");
        Assert.assertEquals("data1", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data1");
        Assert.assertEquals("<!--data1-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data1");
        Assert.assertEquals("data1", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("data1");
        Assert.assertEquals("data1", character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            {
                type = TokenType.Doctype;
            }
        };
        Assert.assertTrue(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        Assert.assertFalse(token.isDoctype());
        Assert.assertTrue(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertTrue(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertTrue(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("data1");
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertTrue(token.isCharacter());
        Assert.assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        Assert.assertEquals(6, Token.TokenType.values().length);
        Assert.assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        Assert.assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        Assert.assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        Assert.assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        Assert.assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        Assert.assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}