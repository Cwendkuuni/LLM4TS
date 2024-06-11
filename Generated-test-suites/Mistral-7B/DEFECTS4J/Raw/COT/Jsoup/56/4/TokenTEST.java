package org.jsoup.parser; // make sure to import all necessary packages

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class TokenTest {
    // Basic JUnit 4 test cases for each method

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        String name = "html";
        doctype.name(name);

        doctype.reset();

        assertEquals(null, doctype.getName());
        assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testResetTag() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "div";
        tag.name(tagName);

        tag.reset();

        assertEquals(null, tag.name());
        assertEquals(null, tag.normalName());
        assertEquals(false, tag.isSelfClosing());
        assertEquals(null, tag.getAttributes());
    }

    @Test
    public void testNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");

        tag.newAttribute();

        tag.appendAttributeName("id");
        tag.appendAttributeValue("box");

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("red", attributes.getAttribute("class"));
        assertEquals("box", attributes.getAttribute("id"));
    }

    @Test
    public void testFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");
        startTag.finaliseTag();

        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertEquals(true, startTag.isSelfClosing());
        assertEquals(null, startTag.getAttributes());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeNameTooLong() {
        Token.Doctype doctype = new Token.Doctype();
        String name = "html";
        for (int i = 0; i < 255; i++) {
            doctype.appendTagName(name.charAt(i));
        }
        doctype.newAttribute();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagNameTooLong() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "tagName";
        for (int i = 0; i < 255; i++) {
            tag.appendTagName(tagName.charAt(i));
        }
        tag.newAttribute();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeNameTooLong() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "tagName";
        tag.name(tagName);
        for (int i = 0; i < 255; i++) {
            tag.appendAttributeName(tagName.charAt(i));
        }
        tag.newAttribute();
    }

    @Test
    public void testAttributeValueTooLong() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "tagName";
        tag.name(tagName);
        tag.appendAttributeName("class");

        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 65535; i++) {
            value.append("a");
        }

        tag.appendAttributeValue(value.toString());
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("aa..............za", attributes.getAttribute("class"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName(null);
        tag.newAttribute();
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName(null);
        tag.newAttribute();
    }

    @Test
    public void testEmptyAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("");
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(0, attributes.size());
    }

    @Test
    public void testEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("", attributes.getAttribute("class"));
    }

    @Test
    public void testMultipleEmptyAttributeValues() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("", attributes.getAttribute("class"));
    }

    @Test
    public void testInvalidCharacterInAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red & blue");
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("red & blue", attributes.getAttribute("class"));
    }

    @Test
    public void testInvalidCharacterInTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("tag&Name");
        tag.newAttribute();

        // No exception should be thrown as '&' is just a normal character in this context
    }
}