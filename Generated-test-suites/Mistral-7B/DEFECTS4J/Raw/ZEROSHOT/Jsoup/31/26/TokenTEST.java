package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeConstruction() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals("Doctype", doctype.tokenType());
    }

    @org.junit.Test
    public void testDoctypeGetName() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @org.junit.Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("publicID");
        assertEquals("publicID", doctype.getPublicIdentifier());
    }

    @org.junit.Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("systemID");
        assertEquals("systemID", doctype.getSystemIdentifier());
    }

    @org.junit.Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagConstruction() {
        Tag tag = new Tag();
        assertEquals(TokenType.Tag, tag.type);
        assertEquals("Tag", tag.tokenType());
    }

    @org.junit.Test
    public void testTagName() {
        Tag tag = new Tag();
        tag.name("div");
        assertEquals("div", tag.name());
    }

    @org.junit.Test
    public void testTagNameWithAppend() {
        Tag tag = new Tag();
        tag.appendTagName("div");
        tag.appendTagName("class");
        tag.appendTagName("myClass");
        assertEquals("divclassmyClass", tag.name());
    }

    @org.junit.Test
    public void testTagSelfClosing() {
        Tag tag = new Tag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Tag tag = new Tag();
        tag.newAttribute();
        assertNull(tag.getAttributes());
    }

    @org.junit.Test
    public void testTagNewAttributeWithValue() {
        Tag tag = new Tag();
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("myId");
        Attributes attributes = tag.getAttributes();
        assertEquals("myId", attributes.get("id"));
    }

    @org.junit.Test
    public void testTagNewAttributeWithMultipleAttributes() {
        Tag tag = new Tag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("myClass1");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("myClass2");
        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("myClass1", attributes.get("class").getValue());
        assertEquals("myClass2", attributes.get("class").getValue());
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Tag tag = new Tag();
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("myId");
        tag.finaliseTag();
        Attributes attributes = tag.getAttributes();
        assertEquals("myId", attributes.get("id").getValue());
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
    }

    @org.junit.Test
    public void testTokenTypeIsDoctype() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());
    }

    @org.junit.Test
    public void testTokenTypeIsStartTag() {
        StartTag startTag = new StartTag();
        assertTrue(startTag.isStartTag());
    }

    @org.junit.Test
    public void testTokenTypeIsEndTag() {
        EndTag endTag = new EndTag();
        assertTrue(endTag.isEndTag());
    }

    @org.junit.Test
    public void testTokenTypeIsComment() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());
    }

    @org.junit.Test
    public void testTokenTypeIsCharacter() {
        Character character = new Character("test");
        assertTrue(character.isCharacter());
    }

    @org.junit.Test
    public void testTokenTypeIsEOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
    }

    @org.junit.Test
    public void testTokenToString() {
        StartTag startTag = new StartTag("div");
        assertEquals("<div>", startTag.toString());

        StartTag startTagWithAttributes = new StartTag("img").name("img").appendAttributeName("src").appendAttributeValue("image.png");
        assertEquals("<img src=\"image.png\">", startTagWithAttributes.toString());

        EndTag endTag = new EndTag("div");
        assertEquals("</div>", endTag.toString());

        Comment comment = new Comment();
        comment.data.append("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());

        Character character = new Character('&');
        assertEquals("&", character.getData());

        EOF eof = new EOF();
        assertEquals("", eof.toString());

        Doctype doctype = new Doctype();
        doctype.name.append("html");
        assertEquals("<!DOCTYPE html>", doctype.toString());
    }
}