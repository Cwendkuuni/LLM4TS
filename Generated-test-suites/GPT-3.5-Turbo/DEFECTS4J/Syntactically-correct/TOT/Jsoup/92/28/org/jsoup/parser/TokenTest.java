
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.CData cdata = new Token.CData("");
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.Character, cdata.type);
        assertEquals(Token.TokenType.EOF, eof.type);
        assertFalse(cdata.isCharacter()); // CData should not be treated as Character
        assertTrue(character.isCharacter());
        assertTrue(cdata.isCData());
        assertFalse(character.isCData());
    }

    @Test
    public void testTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        Token.Tag tag = new Token.StartTag();
        tag.tagName = "span";
        tag.normalName = "span";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValueS = "test123";
        tag.hasPendingAttributeValue = true;
        tag.attributes = new Attributes();
        tag.attributes.put("id", "test123");
        tag.selfClosing = true;

        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");
        comment.bogus = true;

        Token.Character character = new Token.Character();
        character.data("test character");

        Token.CData cdata = new Token.CData("");
        cdata.data("test cdata");

        doctype.reset();
        tag.reset();
        comment.reset();
        character.reset();
        cdata.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);

        assertNull(character.getData());

        assertNull(cdata.getData());
    }

    @Test
    public void testTokenToString() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test doctype");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        String expectedDoctypeString = "<!DOCTYPE test doctype pubSysKey publicIdentifier systemIdentifier>";

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "span";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "test123");
        String expectedStartTagString = "<span id=\"test123\">";

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "span";
        String expectedEndTagString = "</span>";

        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");
        String expectedCommentString = "<!--test comment-->";

        Token.Character character = new Token.Character();
        character.data("test character");
        String expectedCharacterString = "test character";

        Token.CData cdata = new Token.CData("");
        cdata.data("test cdata");
        String expectedCDataString = "<![CDATA[test cdata]]>";

        Token.EOF eof = new Token.EOF();
        String expectedEOFString = "";

        assertEquals(expectedDoctypeString, doctype.toString());
        assertEquals(expectedStartTagString, startTag.toString());
        assertEquals(expectedEndTagString, endTag.toString());
        assertEquals(expectedCommentString, comment.toString());
        assertEquals(expectedCharacterString, character.toString());
        assertEquals(expectedCDataString, cdata.toString());
        assertEquals(expectedEOFString, eof.toString());
    }

    @Test
    public void testTagMethods() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "span";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValueS = "test123";
        startTag.hasPendingAttributeValue = true;
        startTag.attributes = new Attributes();
        startTag.appendAttributeValue(" hello");
        startTag.appendAttributeValue(32);
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("");
        startTag.setEmptyAttributeValue();
        startTag.newAttribute();
        startTag.appendAttributeName("data-name");
        startTag.appendAttributeValue("test");

        assertEquals("span", startTag.name());
        assertEquals("span", startTag.normalName());
        assertTrue(startTag.isSelfClosing());
        assertEquals("span", startTag.getAttributes().get("id"));
        assertEquals("", startTag.getAttributes().get("class"));
        assertEquals("test", startTag.getAttributes().get("data-name"));
    }

    @Test
    public void testCharacterMethods() {
        Token.Character character = new Token.Character();
        character.data("test character");

        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token cdata = new Token.CData("");
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(cdata.isCData());
        assertTrue(eof.isEOF());

        assertFalse(doctype.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(character.isComment());
        assertFalse(cdata.isCharacter());
        assertFalse(eof.isCharacter());
    }
}
