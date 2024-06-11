package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        assertEquals("html", doctype.asDoctype().getName());
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPubSysKey());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.name("html");
        tag.newAttribute().name("class").value("test");
        assertEquals("html", tag.name());
        assertEquals("test", tag.getAttributes().get("class"));
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().data("This is a comment");
        assertEquals("This is a comment", comment.asComment().getData());
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("A");
        assertEquals("A", character.getData());
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCDataReset() {
        Token cData = new Token.CData("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals("<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>]]>", cData.toString());
        cData.reset();
        assertNull(cData.getData());
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.name("html");
        tag.newAttribute().name("class").value("test");
        tag.newAttribute().name("id").value("main");
        assertEquals(2, tag.getAttributes().size());
        assertEquals("test", tag.getAttributes().get("class"));
        assertEquals("main", tag.getAttributes().get("id"));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.name("html").newAttribute().name("class").value("test");
        tag.finaliseTag();
        assertEquals("<html class=\"test\">", tag.toString());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token tag = new Token.StartTag();
        tag.name("img").newAttribute().name("src").setEmptyAttributeValue();
        assertEquals("", tag.getAttributes().get("src"));
    }
}