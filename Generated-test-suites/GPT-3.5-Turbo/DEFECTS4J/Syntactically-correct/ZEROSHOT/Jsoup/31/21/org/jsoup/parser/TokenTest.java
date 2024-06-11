package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Before
    public void setUp() {
        // code to set up any necessary objects before each test method
    }
    
    @Test
    public void testTokenType() {
        Token token = new Token() {};
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {};
        assertNull(tag.attributes);
        tag.newAttribute();
        assertNotNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "testAttribute";
        tag.pendingAttributeValue = new StringBuilder("testValue");
        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());
        Attribute attribute = tag.attributes.get("testAttribute");
        assertNotNull(attribute);
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {};
        assertNull(tag.tagName);
        assertNotNull(tag.name());
    }

    @Test
    public void testTagAppendTagNameString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendTagName("test");
        assertNotNull(tag.tagName);
        assertEquals("test", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendTagName('t');
        assertNotNull(tag.tagName);
        assertEquals("t", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeNameString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeName("test");
        assertNotNull(tag.pendingAttributeName);
        assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeName('t');
        assertNotNull(tag.pendingAttributeName);
        assertEquals("t", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValueString() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeValue("test");
        assertNotNull(tag.pendingAttributeValue);
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeValue('t');
        assertNotNull(tag.pendingAttributeValue);
        assertEquals("t", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("test");
        assertEquals("<test>", startTag.toString());
        startTag.attributes = new Attributes();
        startTag.attributes.put("attribute", "value");
        assertEquals("<test attribute=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("test");
        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assertEquals("", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        assertEquals("<!-- -->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("test");
        assertEquals("test", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("test");
        assertEquals("test", character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            { type = TokenType.Doctype; }
        };
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
            { type = TokenType.StartTag; }
        };
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
            { type = TokenType.EndTag; }
        };
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
            { type = TokenType.Comment; }
        };
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
            { type = TokenType.Character; }
        };
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("test");
        Token token = character;
        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            { type = TokenType.EOF; }
        };
        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals(6, Token.TokenType.values().length);
        assertEquals("Doctype", Token.TokenType.Doctype.name());
        assertEquals("StartTag", Token.TokenType.StartTag.name());
        assertEquals("EndTag", Token.TokenType.EndTag.name());
        assertEquals("Comment", Token.TokenType.Comment.name());
        assertEquals("Character", Token.TokenType.Character.name());
        assertEquals("EOF", Token.TokenType.EOF.name());
    }
}