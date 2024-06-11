package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);

        String name = doctype.asDoctype().getName();
        assertEquals("html", name);

        String publicIdentifier = doctype.asDoctype().getPublicIdentifier();
        assertNull(publicIdentifier);

        String systemIdentifier = doctype.asDoctype().getSystemIdentifier();
        assertNull(systemIdentifier);

        boolean isForceQuirks = doctype.asDoctype().isForceQuirks();
        assertTrue(isForceQuirks);
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().reset();
        assertEquals(TokenType.Tag, tag.type);
        assertNull(tag.asTag().tagName);
        assertNull(tag.asTag().normalName);
        assertNull(tag.asTag().pendingAttributeName);
        reset(tag.asTag().pendingAttributeValue);
        assertFalse(tag.asTag().hasEmptyAttributeValue);
        assertFalse(tag.asTag().hasPendingAttributeValue);
        assertFalse(tag.asTag().selfClosing);
        assertNull(tag.asTag().attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("class");
        tag.asTag().appendAttributeValue("foo");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("id");
        tag.asTag().appendAttributeValue("bar");

        Attributes attributes = tag.asTag().getAttributes();
        assertEquals(2, attributes.size());

        Attribute classAttribute = attributes.get("class");
        assertEquals("foo", classAttribute.getValue());

        Attribute idAttribute = attributes.get("id");
        assertEquals("bar", idAttribute.getValue());

        reset(tag.asTag().pendingAttributeName);
        reset(tag.asTag().pendingAttributeValue);
        tag.asTag().reset();
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.Tag();
        tag.asTag().name("div");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("class");
        tag.asTag().appendAttributeValue("foo");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("id");
        tag.asTag().appendAttributeValue("bar");
        tag.asTag().finaliseTag();

        Attributes attributes = tag.asTag().getAttributes();
        assertEquals(2, attributes.size());

        Attribute classAttribute = attributes.get("class");
        assertEquals("foo", classAttribute.getValue());

        Attribute idAttribute = attributes.get("id");
        assertEquals("bar", idAttribute.getValue());

        // test reset after finaliseTag
        tag.asTag().reset();
        assertEquals(TokenType.Tag, tag.type);
        assertNull(tag.asTag().tagName);
        assertNull(tag.asTag().normalName);
        assertNull(tag.asTag().pendingAttributeName);
        reset(tag.asTag().pendingAttributeValue);
        assertFalse(tag.asTag().hasEmptyAttributeValue);
        assertFalse(tag.asTag().hasPendingAttributeValue);
        assertFalse(tag.asTag().selfClosing);
        assertNull(tag.asTag().attributes);
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.Tag();
        tag.asTag().name("div").name("newName");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("class");
        tag.asTag().appendAttributeValue("foo");
        tag.asTag().newAttribute();
        tag.asTag().appendAttributeName("id");
        tag.asTag().appendAttributeValue("bar");

        String name = tag.asTag().name();
        assertEquals("div", name);

        String normalName = tag.asTag().normalName();
        assertEquals("div", normalName);

        boolean isSelfClosing = tag.asTag().isSelfClosing();
        assertFalse(isSelfClosing);

        Attributes attributes = tag.asTag().getAttributes();
        assertEquals(2, attributes.size());

        Attribute classAttribute = attributes.get("class");
        assertEquals("foo", classAttribute.getValue());

        Attribute idAttribute = attributes.get("id");
        assertEquals("bar", idAttribute.getValue());

        tag.asTag().reset();
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag();
        tag.asStartTag().nameAttr("div", Attributes.EMPTY);

        String tagString = tag.toString();
        assertEquals("<div>", tagString);

        tag.asTag().selfClosing = true;
        tagString = tag.toString();
        assertEquals("<div/>", tagString);
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("comment1");
        comment.asComment().reset();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.asComment().getData());
        assertFalse(comment.asComment().bogus);
    }

    @Test
    public void testCommentGetters() {
        Token comment = new Token.Comment();
        comment.asComment().getData("comment1");

        String data = comment.asComment().getData();
        assertEquals("comment1", data);

        comment.asComment().reset();
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("char1");
        character.asCharacter().reset();
        assertEquals(TokenType.Character, character.type);
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCharacterGetters() {
        Token character = new Token.Character();
        character.asCharacter().data("char1");

        String data = character.asCharacter().getData();
        assertEquals("char1", data);

        character.asCharacter().reset();
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.asEOF().reset();
        assertEquals(TokenType.EOF, eof.type);
    }
}