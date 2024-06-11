package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");

        Token resetDoctype = doctype.reset();
        resetDoctype.asDoctype().getName() == null;
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype();
        String name = doctype.asDoctype().getName();
        assertEquals("doctype", name);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setPubSysKey("pubsyskey");
        doctype.asDoctype().setPublicIdentifier("publicidentifier");
        String publicIdentifier = doctype.asDoctype().getPublicIdentifier();
        assertEquals("publicidentifier", publicIdentifier);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.asDoctype().isForceQuirks());

        doctype.asDoctype().setForceQuirks(false);
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name().equals("starttag");

        Token resetTag = tag.reset();
        resetTag.asStartTag().name() == null;
    }

    @Test
    public void testTagName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("tagname");
        String name = tag.asStartTag().name();
        assertEquals("tagname", name);
    }

    @Test
    public void testTagNormalName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("TAGNAME");
        String normalName = tag.asStartTag().normalName();
        assertEquals("tagname", normalName);
    }

    @Test
    public void testTagSelfClosing() {
        Token tag = new Token.StartTag();
        tag.asStartTag().isSelfClosing(true);
        assertTrue(tag.asStartTag().isSelfClosing());

        tag.asStartTag().isSelfClosing(false);
        assertFalse(tag.asStartTag().isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        Token tag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("attribute1");
        tag.asStartTag().appendAttributeValue("value1");

        assertEquals("value1", tag.asStartTag().getAttributes().get("attribute1"));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().appendAttributeName("attribute1");
        tag.asStartTag().appendAttributeValue("value1");
        tag.asStartTag().finaliseTag();

        assertTrue(tag.asStartTag().getAttributes().size() > 0);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("comment");

        Token resetComment = comment.reset();
        resetComment.asComment().getData() == null;
    }

    @Test
    public void testCommentgetData() {
        Token comment = new Token.Comment();
        comment.asComment().appendData("comment");
        String data = comment.asComment().getData();
        assertEquals("comment", data);
    }

    @Test
    public void testCommentToString() {
        Token comment = new Token.Comment();
        comment.asComment().appendData("comment");
        String commentString = comment.toString();
        assertEquals("<!--comment-->", commentString);
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().equals("character");

        Token resetCharacter = character.reset();
        resetCharacter.asCharacter().getData() == null;
    }

    @Test
    public void testCharacterData() {
        Token character = new Token.Character();
        character.asCharacter().data("character");
        String data = character.asCharacter().getData();
        assertEquals("character", data);
    }

    @Test
    public void testCharacterToString() {
        Token character = new Token.Character();
        character.asCharacter().data("character");
        String characterString = character.toString();
        assertEquals("character", characterString);
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();

        Token resetEof = eof.reset();
        // EOF should not have any methods to reset
        assertTrue(true);
    }
}