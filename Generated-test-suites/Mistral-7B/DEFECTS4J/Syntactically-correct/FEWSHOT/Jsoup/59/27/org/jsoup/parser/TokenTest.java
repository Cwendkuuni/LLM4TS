package org.jsoup.parser;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token token = new Token.Doctype();
        token.asDoctype().getName().equals("Doctype");

        token.reset();

        assertNull(token.asDoctype().getName());
        assertNull(token.asDoctype().getPubSysKey());
        assertNull(token.asDoctype().getPublicIdentifier());
        assertNull(token.asDoctype().getSystemIdentifier());
        assertFalse(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testDoctypeSettersAndGetters() {
        Token token = new Token.Doctype();

        token.asDoctype().getName("html");
        assertEquals("html", token.asDoctype().getName());

        token.asDoctype().getPublicIdentifier().append("publicId");
        assertEquals("publicId", token.asDoctype().getPublicIdentifier().toString());

        token.asDoctype().setSystemIdentifier("systemId");
        assertEquals("systemId", token.asDoctype().getSystemIdentifier().toString());

        token.asDoctype().isForceQuirks(true);
        assertTrue(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token token = new Token.Tag();
        token.reset();

        assertNull(token.name());
        assertNull(token.normalName());
        assertNull(token.getAttributes());
        assertFalse(token.isSelfClosing());
    }

    @Test
    public void testTagSettersAndGetters() {
        Token token = new Token.Tag();

        token.name("tagName");
        assertEquals("tagName", token.name());

        token.normalName("normalName");
        assertEquals("normalName", token.normalName());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attributeName", "attributeValue"));
        token.getAttributes().put(attributes);
        assertEquals(1, token.getAttributes().size());

        token.isSelfClosing(true);
        assertTrue(token.isSelfClosing());
    }

    @Test
    public void testTagNewAttribute() {
        Token token = new Token.Tag();

        token.newAttribute();
        assertNull(token.getAttributes());

        token.appendAttributeName("attributeName");
        token.appendAttributeValue("attributeValue");
        token.newAttribute();

        Attributes attributes = token.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("attributeName");
        assertEquals("attributeValue", attribute.getValue());

        token.appendAttributeName("anotherAttributeName");
        token.finaliseTag();

        attributes = token.getAttributes();
        assertEquals(2, attributes.size());
        attribute = attributes.get("anotherAttributeName");
        assertNull(attribute.getValue());
    }

    @Test
    public void testTagToString() {
        Token token = new Token.StartTag();
        token.name("tagName");
        assertEquals("<tagName>", token.toString());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attributeName", "attributeValue"));
        token.getAttributes().put(attributes);
        assertEquals("<tagName attributeName=\"attributeValue\">", token.toString());
    }

    @Test
    public void testTagEndToString() {
        Token token = new Token.EndTag();
        token.name("tagName");
        assertEquals("</tagName>", token.toString());
    }

    @Test
    public void testCommentReset() {
        Token token = new Token.Comment();
        token.reset();

        assertNull(token.asComment().getData());
        assertFalse(token.asComment().isBogus());
    }

    @Test
    public void testCommentSettersAndGetters() {
        Token token = new Token.Comment();

        token.asComment().getData().append("commentData");
        assertEquals("commentData", token.asComment().getData().toString());

        token.asComment().isBogus(true);
        assertTrue(token.asComment().isBogus());
    }

    @Test
    public void testCharacterReset() {
        Token token = new Token.Character();
        token.reset();

        assertNull(token.asCharacter().getData());
    }

    @Test
    public void testCharacterSettersAndGetters() {
        Token token = new Token.Character();

        token.asCharacter().data("characterData");
        assertEquals("characterData", token.asCharacter().getData());
    }

    @Test
    public void testEOFReset() {
        Token token = new Token.EOF();
        token.reset();

        assertInstanceOf(Token.EOF.class, token);
    }
}