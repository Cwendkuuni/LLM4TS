package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(true, doctype.asDoctype().isForceQuirks());
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
        tag.name("div");
        tag.newAttribute().name("class").value("class1 class2");
        assertEquals("div", tag.name());
        assertEquals("class1 class2", tag.getAttributes().toString());
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("This is a comment");
        assertEquals("This is a comment", comment.asComment().getData());
        comment.reset();
        assertNull(comment.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("A");
        assertEquals("A", character.asCharacter().getData());
        character.reset();
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCDataReset() {
        Token cData = new Token.CData("<script>alert(\"Hello\")</script>");
        assertEquals("<![CDATA[\n<script>alert(\"Hello\")</script>\n]]>", cData.toString());
        cData.reset();
        assertNull(cData.toString());
    }

    @Test
    public void testTagNormalName() {
        Token tag = new Token.StartTag();
        tag.name("Div");
        tag.name("div"); // auto converts to lowercase
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTokenType() {
        Token tag = new Token.StartTag();
        tag.name("div");
        assertTrue(tag.isStartTag());
        tag = new Token.EndTag();
        assertTrue(tag.isEndTag());
        tag = new Token.Comment();
        assertTrue(tag.isComment());
        tag = new Token.Character();
        assertTrue(tag.isCharacter());
        tag = new Token.Doctype();
        assertTrue(tag.isDoctype());
        tag = new Token.EOF();
        assertTrue(tag.isEOF());
    }
}