package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getPublicIdentifier("publicId");
        assertEquals("publicId", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setSystemIdentifier("systemId");
        assertEquals("systemId", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().isForceQuirks();
        assertFalse(doctype.asDoctype().isForceQuirks());
        doctype.asDoctype().setForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.attributes);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagName() {
        Token tag = new Token.Tag();
        tag.name("p");
        assertEquals("p", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Token tag = new Token.Tag();
        tag.name("P").name("normal");
        assertEquals("normal", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag = new Token.Tag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagAttributes() {
        Token tag = new Token.Tag();
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue("testClass");
        assertEquals("class", tag.asStartTag().getAttributes().getClassNames().get(0));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        tag.newAttribute();
        tag.newAttribute();
        tag.finaliseTag();
        assertTrue(tag.attributes != null);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.reset();
        assertNull(comment.asComment().getData());
        comment.asComment().setBogus(true);
        assertTrue(comment.asComment().isBogus());
    }

    @Test
    public void testCommentgetData() {
        Token comment = new Token.Comment();
        comment.asComment().setData("commentData");
        assertEquals("commentData", comment.asComment().getData());
    }

    @Test
    public void testCommentisBogus() {
        Token comment = new Token.Comment();
        comment.asComment().setBogus(true);
        assertTrue(comment.asComment().isBogus());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.reset();
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character();
        character.asCharacter().setData("characterData");
        assertEquals("characterData", character.asCharacter().getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }
}