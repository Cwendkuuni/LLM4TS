package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("Doctype");
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeMethods() {
        Token doctype = new Token.Doctype();

        doctype.asDoctype().getName("HTML");
        assertEquals("HTML", doctype.asDoctype().getName());

        doctype.asDoctype().setPubSysKey("DTD HTML 5");
        assertEquals("DTD HTML 5", doctype.asDoctype().getPubSysKey());

        doctype.asDoctype().getPublicIdentifier().append("public ");
        assertEquals("public ", doctype.asDoctype().getPublicIdentifier().toString());

        doctype.asDoctype().getSystemIdentifier().append("system.html");
        assertEquals("system.html", doctype.asDoctype().getSystemIdentifier().toString());

        doctype.asDoctype().isForceQuirks();
        assertFalse(doctype.asDoctype().isForceQuirks());

        doctype.asDoctype().isForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagMethods() {
        Token tag = new Token.Tag();

        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.newAttribute().name("class").value("class1 class2");
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("class1 class2", tag.pendingAttributeValue.toString());
        assertEquals("class1 class2", tag.pendingAttributeValueS);
        assertTrue(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);

        tag.newAttribute();
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);

        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);
        assertNull(tag.selfClosing);
        assertNotNull(tag.getAttributes());

        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.toString();
        // The output will depend on the specific implementation of toString() in the derived class
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCommentMethods() {
        Token comment = new Token.Comment();

        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData().toString());

        comment.isBogus(true);
        assertTrue(comment.isBogus());

        comment.toString();
        // The output will be "<!--This is a comment-->"
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterMethods() {
        Token character = new Token.Character();

        character.data("Hello, World!");
        assertEquals("Hello, World!", character.getData());

        character.toString();
        // The output will be "Hello, World!"
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        assertNull(eof.toString());
    }
}