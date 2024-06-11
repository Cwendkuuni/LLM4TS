package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {};
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "href";
        tag.pendingAttributeValue = new StringBuilder();
        tag.pendingAttributeValue.append("https://www.example.com");
        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertEquals("href", tag.attributes.get("href").getKey());
        assertEquals("https://www.example.com", tag.attributes.get("href").getValue());
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "href";
        tag.pendingAttributeValue = null;
        tag.finaliseTag();
        assertNotNull(tag.attributes);
        assertEquals("href", tag.attributes.get("href").getKey());
        assertEquals("", tag.attributes.get("href").getValue());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = "a";
        assertEquals("a", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag() {};
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.Tag() {};
        tag.attributes = new Attributes();
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = null;
        tag.appendTagName("a");
        assertEquals("a", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = null;
        tag.appendTagName('a');
        assertEquals("a", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = null;
        tag.appendAttributeName("href");
        assertEquals("href", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = null;
        tag.appendAttributeName('h');
        assertEquals("h", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeValue = null;
        tag.appendAttributeValue("https://www.example.com");
        assertEquals("https://www.example.com", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeValue = null;
        tag.appendAttributeValue('h');
        assertEquals("h", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "a";
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("href", "https://www.example.com"));
        assertEquals("<a href=\"https://www.example.com\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "a";
        assertEquals("<a>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "a";
        assertEquals("</a>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            {
                type = Token.TokenType.Doctype;
            }
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
            {
                type = Token.TokenType.StartTag;
            }
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
            {
                type = Token.TokenType.EndTag;
            }
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
            {
                type = Token.TokenType.Comment;
            }
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
            {
                type = Token.TokenType.Character;
            }
        };
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("This is a character");
        Token token = character;
        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            {
                type = Token.TokenType.EOF;
            }
        };
        assertTrue(token.isEOF());
    }

}