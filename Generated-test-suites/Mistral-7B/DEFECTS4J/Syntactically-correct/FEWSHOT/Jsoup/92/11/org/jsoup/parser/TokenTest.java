package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("");
        doctype.reset();
        doctype.asDoctype().getName().equals("");
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().name("HTML");
        assertEquals("HTML", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().isForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
        doctype.reset();
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.attributes);
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getName());
        assertNull(tag.toString());
    }

    @Test
    public void testTagName() {
        Token tag = new Token.Tag();
        tag.name("html");
        assertEquals("html", tag.getName());
        tag.reset();
    }

    @Test
    public void testTagNameLower() {
        Token tag = new Token.Tag();
        tag.name("HTML");
        assertEquals("html", tag.normalName());
        tag.reset();
    }

    @Test
    public void testTagSelfClosing() {
        Token tag = new Token.Tag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
        tag.reset();
    }

    @Test
    public void testTagAttributes() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.newAttribute();
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
        tag.reset();
    }

    @Test
    public void testTagAttributesSet() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", "123");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals("123", attributes.get("id"));
        tag.reset();
    }

    @Test
    public void testTagAttributesAdd() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", "123").nameAttr("class", "test");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("123", attributes.get("id"));
        assertEquals("test", attributes.get("class"));
        tag.reset();
    }

    @Test
    public void testTagAttributesDuplicate() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", "123").nameAttr("id", "456");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals("123", attributes.get("id"));
        tag.reset();
    }

    @Test
    public void testTagAttributesEmptyValue() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", "").nameAttr("class", "");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("", attributes.get("id"));
        assertEquals("", attributes.get("class"));
        tag.reset();
    }

    @Test
    public void testTagAttributesNullValue() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", null).nameAttr("class", null);
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
        tag.reset();
    }

    @Test
    public void testTagAttributesNotEmptyValue() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.asStartTag().nameAttr("id", "123").nameAttr("class", "test");
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("123", attributes.get("id"));
        assertEquals("test", attributes.get("class"));
        tag.reset();
    }

    @Test
    public void testTagGetAttributesNull() {
        Token tag = new Token.Tag();
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token tag = new Token.Tag();
        tag.appendTagName("html");
        assertEquals("html", tag.getName());
        tag.reset();
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token tag = new Token.Tag();
        tag.appendTagName('h');
        assertEquals("h", tag.getName());
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeName() {
        Token tag = new Token.Tag();
        tag.appendAttributeName("class");
        assertEquals("class", tag.pendingAttributeName);
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token tag = new Token.Tag();
        tag.appendAttributeName('c');
        assertEquals("c", tag.pendingAttributeName);
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token tag = new Token.Tag();
        tag.appendAttributeValue("class");
        tag.ensureAttributeValue();
        tag.asStartTag().appendAttributeValue("test");
        assertEquals("test", tag.pendingAttributeValue);
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token tag = new Token.Tag();
        tag.appendAttributeValue("class");
        tag.ensureAttributeValue();
        tag.asStartTag().appendAttributeValue('t');
        assertEquals("t", tag.pendingAttributeValue.charAt(0));
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeValueCodepoints() {
        Token tag = new Token.Tag();
        tag.appendAttributeValue("class");
        tag.ensureAttributeValue();
        tag.asStartTag().appendAttributeValue("\u0068\u0065\u0078");
        assertEquals("php", tag.pendingAttributeValue);
        tag.reset();
    }

    @Test
    public void testTagAppendAttributeValueCodepointsArray() {
        Token tag = new Token.Tag();
        tag.appendAttributeValue("class");
        tag.ensureAttributeValue();
        tag.asStartTag().appendAttributeValue(new int[] { '\u0068', '\u0065', '\u0078' });
        assertEquals("php", tag.pendingAttributeValue);
        tag.reset();
    }

    @Test
    public void testTagEmptyAttributeValue() {
        Token tag = new Token.Tag();
        tag.appendAttributeValue("class");
        tag.setEmptyAttributeValue();
        assertEquals("", tag.pendingAttributeValue);
        tag.reset();
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.reset();
        assertNull(comment.getData());
        comment.asComment().getData().equals("");
    }

    @Test
    public void testCommentGetData() {
        Token comment = new Token.Comment();
        comment.asComment().getData("<!-- test -->");
        assertEquals("test", comment.asComment().getData());
        comment.reset();
    }

    @Test
    public void testCommentToString() {
        Token comment = new Token.Comment();
        comment.asComment().getData("<!-- test -->");
        assertEquals("<!--test-->", comment.toString());
        comment.reset();
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.reset();
        assertNull(character.getData());
        assertEquals("", character.toString());
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character();
        character.asCharacter().data("test");
        assertEquals("test", character.getData());
        character.reset();
    }

    @Test
    public void testCharactertoString() {
        Token character = new Token.Character();
        character.asCharacter().data("test");
        assertEquals("test", character.toString());
        character.reset();
    }

    @Test
    public void testCDataReset() {
        Token cData = new Token.CData();
        cData.reset();
        assertNull(cData.getData());
        assertEquals("<![CDATA[]]", cData.toString());
    }

    @Test
    public void testCDatagetData() {
        Token cData = new Token.CData();
        cData.asCData().data("<script src=\"script.js\"></script>");
        assertEquals("<script src=\"script.js\"></script>", cData.getData());
        cData.reset();
    }

    @Test
    public void testCDatatoString() {
        Token cData = new Token.CData();
        cData.asCData().data("<script src=\"script.js\"></script>");
        assertEquals("<![CDATA[<script src=\"script.js\"></script>]]>", cData.toString());
        cData.reset();
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        assertFalse(eof.isEOF());
    }

    @Test
    public void testEOFIsEOF() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}