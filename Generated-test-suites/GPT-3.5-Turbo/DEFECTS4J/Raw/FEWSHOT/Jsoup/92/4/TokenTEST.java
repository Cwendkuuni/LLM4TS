package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.CData("data");
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertTrue(token.isEOF());
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        Doctype resettedDoctype = doctype.reset();

        assertEquals("", resettedDoctype.getName());
        assertNull(resettedDoctype.getPubSysKey());
        assertEquals("", resettedDoctype.getPublicIdentifier());
        assertEquals("", resettedDoctype.getSystemIdentifier());
        assertFalse(resettedDoctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "attributeName";
        startTag.pendingAttributeValue.append("attributeValue");
        startTag.pendingAttributeValueS = "attributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        StartTag resettedStartTag = startTag.reset();

        assertNull(resettedStartTag.tagName);
        assertNull(resettedStartTag.normalName);
        assertNull(resettedStartTag.pendingAttributeName);
        assertEquals("", resettedStartTag.pendingAttributeValue.toString());
        assertNull(resettedStartTag.pendingAttributeValueS);
        assertFalse(resettedStartTag.hasEmptyAttributeValue);
        assertFalse(resettedStartTag.hasPendingAttributeValue);
        assertFalse(resettedStartTag.selfClosing);
        assertNull(resettedStartTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "attributeName";
        endTag.pendingAttributeValue.append("attributeValue");
        endTag.pendingAttributeValueS = "attributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        EndTag resettedEndTag = endTag.reset();

        assertNull(resettedEndTag.tagName);
        assertNull(resettedEndTag.normalName);
        assertNull(resettedEndTag.pendingAttributeName);
        assertEquals("", resettedEndTag.pendingAttributeValue.toString());
        assertNull(resettedEndTag.pendingAttributeValueS);
        assertFalse(resettedEndTag.hasEmptyAttributeValue);
        assertFalse(resettedEndTag.hasPendingAttributeValue);
        assertFalse(resettedEndTag.selfClosing);
        assertNull(resettedEndTag.attributes);
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        Token.Comment resettedComment = comment.reset();

        assertEquals("", resettedComment.getData());
        assertFalse(resettedComment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Token.Character resettedCharacter = character.reset();

        assertNull(resettedCharacter.data);
    }

    @Test
    public void testToStringStartTagWithAttributes() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tagName";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attribute1", "value1");
        startTag.attributes.put("attribute2", "value2");
        startTag.attributes.put("attribute3", "value3");

        assertEquals("<tagName attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">", startTag.toString());
    }

    @Test
    public void testToStringStartTagWithoutAttributes() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tagName";

        assertEquals("<tagName>", startTag.toString());
    }

    @Test
    public void testToStringEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName = "tagName";

        assertEquals("</tagName>", endTag.toString());
    }

    @Test
    public void testToStringComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testToStringCharacter() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.toString());
    }

    @Test
    public void testToStringCData() {
        Token.CData cData = new Token.CData("data");

        assertEquals("<![CDATA[data]]>", cData.toString());
    }

    @Test
    public void testToStringEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals("", eof.toString());
    }
}