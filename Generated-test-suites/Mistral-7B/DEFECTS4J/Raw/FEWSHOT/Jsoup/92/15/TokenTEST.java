package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // initially set
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.getPubSysKey("DTD HTML 5");
        doctype.getPublicIdentifier("public \"-//W3C//DTD HTML 5//EN\"");
        doctype.setSystemIdentifier("/path/to/html.dtd");
        doctype.isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("DTD HTML 5", doctype.getPubSysKey());
        assertEquals("public \"-//W3C//DTD HTML 5//EN\"", doctype.getPublicIdentifier());
        assertEquals("/path/to/html.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName("p");
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagGetterSetters() {
        Tag tag = new StartTag();
        tag.tagName("p").normalName("p").name("p");
        assertEquals("p", tag.tagName());
        assertEquals("p", tag.name());
        assertEquals("p", tag.normalName());

        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.newAttribute().pendingAttributeName("id").pendingAttributeValue("id1").appendAttributeValue("id2");
        assertEquals("id", tag.pendingAttributeName());
        assertEquals("id1 id2", tag.getAttributes().get("id"));
        assertEquals("id2", tag.pendingAttributeValue());
        assertEquals("id1 id2", tag.toString());

        tag.newAttribute().pendingAttributeName("class").setEmptyAttributeValue();
        assertNull(tag.pendingAttributeValue());
        assertTrue(tag.hasEmptyAttributeValue());
        assertEquals("class", tag.pendingAttributeName());
        assertEquals("<p class>", tag.toString());

        tag.finaliseTag();
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);
        assertEquals("<p class='id1 id2'>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCommentGetterSetter() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.isBogus());

        comment.bogus(true);
        assertTrue(comment.isBogus());
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A character");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetterSetter() {
        Character character = new Character();
        character.data("A character");
        assertEquals("A character", character.getData());
        assertEquals("A character", character.toString());
    }

    @Test
    public void testCDataReset() {
        CData cData = new CData("<![CDATA[This is CData]]>");
        cData.reset();
        assertNull(cData.getData());
    }

    @Test
    public void testCDataGetterSetter() {
        CData cData = new CData("<![CDATA[This is CData]]>");
        assertEquals("<![CDATA[This is CData]]>", cData.toString());
        assertEquals("<![CDATA[This is CData]]>", cData.getData());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());

        token = new StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());

        token = new EndTag();
        assertTrue(token.isEndTag());

        token = new Comment();
        assertTrue(token.isComment());

        token = new Character();
        assertTrue(token.isCharacter());
        token = new CData("");
        assertTrue(token.isCData());

        token = new EOF();
        assertTrue(token.isEOF());
    }
}