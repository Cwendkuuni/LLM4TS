package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Doctype doctype = new Doctype();
        assertEquals(Token.TokenType.Doctype, doctype.type);

        Tag startTag = new Tag() {};
        assertEquals(Token.TokenType.StartTag, startTag.type);

        Tag endTag = new Tag() {};
        assertEquals(Token.TokenType.EndTag, endTag.type);

        Comment comment = new Comment();
        assertEquals(Token.TokenType.Comment, comment.type);

        Character character = new Character();
        assertEquals(Token.TokenType.Character, character.type);

        EOF eof = new EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype()
                      .reset();
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new Tag() {
            @Override
            public Tag reset() {
                return super.reset();
            }
        };

        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "test";
        tag.reset(null);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValueS);
        assertNull(tag.attributes);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testStartTagReset() {
        Tag.StartTag startTag = new Tag.StartTag()
                      .reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValueS);
        assertNull(startTag.attributes);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
    }

    @Test
    public void testEndTagReset() {
        Tag.EndTag endTag = new Tag.EndTag()
                      .reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValueS);
        assertNull(endTag.attributes);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment()
                      .reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character()
                      .reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF()
                      .reset();
        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Tag.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Tag.EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsComment() {
        Token token = new Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token token = new EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }

    @Test
    public void testDoctypeName() {
        Doctype doctype = new Doctype();
        doctype.name.append("test");
        assertEquals("test", doctype.getName());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("test");
        assertEquals("test", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("test");
        assertEquals("test", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testAttributeName() {
        Tag tag = new Tag() {
            @Override
            public void appendAttributeName(String append) {
                super.appendAttributeName(append);
            }
        };

        tag.appendAttributeName("test");
        assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testAttributeValueString() {
        Tag tag = new Tag() {
            @Override
            public void appendAttributeValue(String append) {
                super.appendAttributeValue(append);
            }
        };

        tag.appendAttributeValue("test");
        assertTrue(tag.hasPendingAttributeValue);
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testDefaultValue() {
        Tag tag = new Tag() {
            @Override
            public Tag reset() {
                return super.reset();
            }
        };

        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testFinalizeTag() {
        Tag tag = new Tag() {
            @Override
            public void newAttribute() {
                super.newAttribute();
            }

            @Override
            public void reset(StringBuilder sb) {
                super.reset(sb);
            }
        };

        tag.pendingAttributeName = "test";
        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testNameAttr() {
        Tag.StartTag startTag = new Tag.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("test", attributes);
        assertEquals("test", startTag.tagName);
        assertEquals("test", startTag.normalName);
        assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Tag.StartTag startTag = new Tag.StartTag();
        startTag.tagName = "test";
        startTag.attributes = new Attributes();
        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Tag.EndTag endTag = new Tag.EndTag();
        endTag.tagName = "test";
        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data.append("test");
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data = "test";
        assertEquals("test", character.toString());
    }
}