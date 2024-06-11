package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr", "value");

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
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "Tag";

        assertEquals("Tag", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "tag";

        assertEquals("tag", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTagNoClosing = new Token.StartTag();
        startTagNoClosing.selfClosing = false;

        Token.StartTag startTagClosing = new Token.StartTag();
        startTagClosing.selfClosing = true;

        assertFalse(startTagNoClosing.isSelfClosing());
        assertTrue(startTagClosing.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr", "value");

        assertEquals("value", startTag.getAttributes().get("attr"));
    }

    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "Tag";
        startTag.normalName = "tag";

        startTag.appendTagName("Name");

        assertEquals("TagName", startTag.tagName);
        assertEquals("tagname", startTag.normalName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "attr";

        startTag.appendAttributeName("Name");

        assertEquals("attrName", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.appendAttributeValue("value");

        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagSetEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "Tag";

        assertEquals("Tag", endTag.name());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }

}