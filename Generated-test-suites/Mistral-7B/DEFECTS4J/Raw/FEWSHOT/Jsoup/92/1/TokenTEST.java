package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype"); // check initial state
        doctype.reset();
        assertTrue(doctype.isEOF()); // check reset state
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().getPubSysKey("public");
        doctype.asDoctype().getPublicIdentifier("HTML");
        doctype.asDoctype().setSystemIdentifier("system.html");
        doctype.asDoctype().isForceQuirks(true);

        assertEquals("html", doctype.asDoctype().getName());
        assertEquals("public", doctype.asDoctype().getPubSysKey());
        assertEquals("HTML", doctype.asDoctype().getPublicIdentifier());
        assertEquals("system.html", doctype.asDoctype().getSystemIdentifier());
        assertTrue(doctype.asDoctype().isForceQuirks());

        doctype.reset();
        assertTrue(doctype.isEOF());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        tag.reset();
        assertTrue(tag.isEOF());
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();
        tag.asTag().newAttribute();
        assertNull(tag.asTag().attributes);
    }

    @Test
    public void testTagNewAttributeWithValue() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().newAttribute().setName("class").setValue("foo");
        assertEquals("div", tag.asTag().name());
        assertEquals("class:foo", tag.asTag().toString());
        assertEquals("foo", tag.asTag().getAttributes().getValue("class"));

        tag.asTag().newAttribute();
        tag.asTag().name("id").setValue("bar");
        assertEquals("class:foo id:bar", tag.asTag().toString());
        assertEquals("foo", tag.asTag().getAttributes().getValue("class"));
        assertEquals("bar", tag.asTag().getAttributes().getValue("id"));
    }

    @Test
    public void testTagNewAttributeEmptyValue() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().newAttribute().setName("class").setEmptyAttributeValue();
        assertEquals("div class=\"\"", tag.asTag().toString());
        assertEquals("class", tag.asTag().getAttributes().getName("class"));
        assertTrue(tag.asTag().getAttributes().getValue("class").isEmpty());

        tag.asTag().newAttribute();
        tag.asTag().name("id").setEmptyAttributeValue();
        assertEquals("div class=\"\" id=\"\"", tag.asTag().toString());
        assertEquals("class", tag.asTag().getAttributes().getName("class"));
        assertTrue(tag.asTag().getAttributes().getValue("class").isEmpty());
        assertTrue(tag.asTag().getAttributes().getValue("id").isEmpty());
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        tag.asTag().name("div").newAttribute().setName("class").setValue("foo");
        tag.asTag().finaliseTag();
        assertEquals("div class:foo", tag.asTag().toString());

        tag.asTag().name("div").newAttribute().setName("class").setValue("bar");
        tag.asTag().finaliseTag();
        assertEquals("div class:bar", tag.asTag().toString());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.Tag();
        tag.asTag().selfClosing(true);
        assertTrue(tag.asTag().isSelfClosing());

        tag = new Token.Tag();
        tag.asTag().selfClosing(false);
        assertFalse(tag.asTag().isSelfClosing());
    }

    @Test
    public void testTokenToString() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().finaliseTag();
        tag.asTag().toString(); // should call finaliseTag()

        assertEquals("<div>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.reset();
        assertTrue(comment.isEOF());
    }

    @Test
    public void testCommentgetData() {
        Token comment = new Token.Comment();
        comment.asComment().getData("--comment--");
        assertEquals("--comment--", comment.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.reset();
        assertTrue(character.isEOF());
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character();
        character.asCharacter().data("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testCDataReset() {
        Token cdata = new Token.CData("CDATA[content]");
        cdata.reset();
        assertTrue(cdata.isEOF());
    }

    @Test
    public void testCDatagetData() {
        Token cdata = new Token.CData("CDATA[content]");
        assertEquals("[content]", cdata.getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }
}