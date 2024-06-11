package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testReset() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNull(token.reset());
    }

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertNull(token.tokenType());
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
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token.Tag reset() {
                return null;
            }
        };
        assertNull(tag.reset());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("name");
        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue.append("pendingAttributeValue");
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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name = "name";
        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "pendingAttributeName";
        endTag.pendingAttributeValue.append("pendingAttributeValue");
        endTag.pendingAttributeValueS = "pendingAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        endTag.reset();
        assertNull(endTag.name);
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
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
        assertEquals(eof, eof.reset());
    }

    @Test
    public void testDoctypeGetType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertEquals(doctype, token.asDoctype());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertTrue(token.isDoctype());
    }

    @Test
    public void testStartTagGetType() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertEquals(startTag, token.asStartTag());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertTrue(token.isStartTag());
    }

    @Test
    public void testEndTagGetType() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertEquals(endTag, token.asEndTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertTrue(token.isEndTag());
    }

    @Test
    public void testCommentGetType() {
        Token.Comment comment = new Token.Comment();
        assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertEquals(comment, token.asComment());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertTrue(token.isComment());
    }

    @Test
    public void testCharacterGetType() {
        Token.Character character = new Token.Character();
        assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        assertTrue(token.isCharacter());
    }

    @Test
    public void testEOFGetType() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token token = eof;
        assertTrue(token.isEOF());
    }
}