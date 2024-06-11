package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

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
        doctype.publicIdentifier.append("public identifier");
        doctype.systemIdentifier.append("system identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.name.toString());
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public identifier");
        doctype.systemIdentifier.append("system identifier");
        doctype.forceQuirks = true;

        Assert.assertEquals("name", doctype.getName());
        Assert.assertEquals("public identifier", doctype.getPublicIdentifier());
        Assert.assertEquals("system identifier", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "tag";
        tag.normalName = "normal tag";
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("attribute value");
        tag.pendingAttributeValueS = "attribute valueS";
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
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.newAttribute();

        Assert.assertNotNull(tag.attributes);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertEquals("", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("attribute value");

        tag.finaliseTag();

        Assert.assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "Tag";

        Assert.assertEquals("Tag", tag.name());
    }

    @Test
    public void testNormalName() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
            }
        };
        tag.tagName = "Tag";

        Assert.assertEquals("tag", tag.normalName());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "normal tag";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("attribute value");
        startTag.pendingAttributeValueS = "attribute valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("tag", attributes);

        Assert.assertEquals("tag", startTag.tagName);
        Assert.assertEquals(attributes, startTag.attributes);
        Assert.assertEquals("tag", startTag.normalName);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attribute", "value");

        Assert.assertEquals("<tag attribute=\"value\">", startTag.toString());
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
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        Assert.assertEquals("data", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        Assert.assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");

        Assert.assertEquals("data", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Assert.assertEquals("data", character.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertNotNull(eof);
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
        Token.TokenType tokenType = Token.TokenType.Doctype;

        Assert.assertEquals("Doctype", tokenType.name());
        Assert.assertEquals(4, tokenType.ordinal());
    }
}