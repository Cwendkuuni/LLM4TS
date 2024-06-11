// Test cases for the Token class

package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
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
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tag";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "attributeName";
        tag.pendingAttributeValue.append("attributeValue");
        tag.pendingAttributeValueS = "attributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");

        assertEquals("<tag attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
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
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        assertEquals("data", comment.getData());
        assertTrue(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");

        assertEquals("<![CDATA[data]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNull(eof.reset());
    }
}