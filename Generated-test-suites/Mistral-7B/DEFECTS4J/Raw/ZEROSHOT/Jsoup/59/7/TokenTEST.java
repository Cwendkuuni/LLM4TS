package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

/**
 * Test class for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.asDoctype().getName = "html";
        assertEquals("html", doctypeToken.asDoctype().getName());

        doctypeToken.asDoctype().setPubSysKey("foo");
        assertEquals("foo", doctypeToken.asDoctype().getPubSysKey());

        doctypeToken.asDoctype().reset();
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPubSysKey());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        assertEquals("StartTag", startTagToken.tokenType());

        startTagToken.asStartTag().name("div");
        assertEquals("div", startTagToken.asStartTag().name());
        assertEquals("div", startTagToken.asStartTag().normalName());

        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        startTagToken.asStartTag().attributes(attributes);
        assertEquals(2, startTagToken.asStartTag().getAttributes().size());

        assertFalse(startTagToken.asStartTag().isSelfClosing());

        startTagToken.asStartTag().reset();
        assertNull(startTagToken.asStartTag().name());
        assertNull(startTagToken.asStartTag().attributes);
        assertTrue(startTagToken.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        assertEquals("EndTag", endTagToken.tokenType());

        endTagToken.asEndTag().name("div");
        assertEquals("div", endTagToken.asEndTag().name());
        assertEquals("div", endTagToken.asEndTag().normalName());

        endTagToken.asEndTag().reset();
        assertNull(endTagToken.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        assertEquals("Comment", commentToken.tokenType());

        commentToken.asComment().getData("-- This is a comment");
        assertEquals("This is a comment", commentToken.asComment().getData());

        commentToken.asComment().reset();
        assertNull(commentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        assertEquals("Character", characterToken.tokenType());

        characterToken.asCharacter().getData("A");
        assertEquals("A", characterToken.asCharacter().getData());

        characterToken.asCharacter().reset();
        assertNull(characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType());

        eofToken.asEOF();
        assertTrue(eofToken.isEOF());

        eofToken.reset();
        assertFalse(eofToken.isEOF());
    }

    @Test
    public void testResetToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName = "html";

        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("div").attributes(new Attributes().put("class", "class1 class2"));

        Token commentToken = new Token.Comment();
        commentToken.asComment().getData("-- This is a comment");

        Token characterToken = new Token.Character().data('A');
        Token eofToken = new Token.EOF();

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.asStartTag().name());
        assertEquals(2, startTagToken.asStartTag().getAttributes().size());

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment", commentToken.asComment().getData());

        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());

        assertEquals("EOF", eofToken.tokenType());

        doctypeToken.reset();
        commentToken.reset();
        characterToken.reset();
        eofToken.reset();
        startTagToken.reset();

        assertNull(doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());

        assertNull(startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().name());
        assertNull(startTagToken.asStartTag().getAttributes());

        assertNull(commentToken.tokenType());
        assertNull(commentToken.asComment().getData());

        assertNull(characterToken.tokenType());
        assertNull(characterToken.asCharacter().getData());

        assertNull(eofToken.tokenType());
    }

    @Test
    public void testBooleanAttribute() {
        Token startTagToken = new Token.StartTag();

        startTagToken.asStartTag().name("input").attributes(new Attributes().put(new BooleanAttribute("checked"), true));
        assertTrue(startTagToken.asStartTag().getAttributes().getValue("checked"));
        assertEquals("checked", startTagToken.asStartTag().getAttributes().getName("checked"));

        startTagToken.asStartTag().reset();

        startTagToken.asStartTag().name("input").attributes(new Attributes().put(new BooleanAttribute("checked"), false));
        assertFalse(startTagToken.asStartTag().getAttributes().getValue("checked"));
        assertEquals("checked", startTagToken.asStartTag().getAttributes().getName("checked"));
    }
}