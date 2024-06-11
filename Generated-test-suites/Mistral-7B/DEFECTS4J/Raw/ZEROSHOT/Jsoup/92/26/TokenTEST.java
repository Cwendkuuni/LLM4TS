package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import static org.jsoup.internal.Normalizer.lowerCase;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
        assertEquals(Token.TokenType.Doctype, resetDoctypeToken.type);

        doctypeToken.getName() = "html";
        assertEquals("html", doctypeToken.getName());
        assertEquals("HTML", doctypeToken.name());

        doctypeToken.setPubSysKey("public -/system/dtd html 4.01 Transitional//EN");
        assertEquals("public -/system/dtd html 4.01 Transitional//EN", doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.setPublicIdentifier("DTD HTML 4.01");
        assertEquals("DTD HTML 4.01", doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.setSystemIdentifier("/html/dtd/html4.dtd");
        assertEquals("/html/dtd/html4.dtd", doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.setForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
    }

    @org.junit.Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Token resetTagToken = tagToken.reset();

        assertEquals(Token.TokenType.StartTag, tagToken.type);
        assertEquals(Token.TokenType.StartTag, resetTagToken.type);

        tagToken.name("html").nameAttr("xmlns:html", new Attributes());
        assertEquals("html", tagToken.name());
        assertEquals("html", tagToken.normalName());
        assertEquals("html", tagToken.getName());
        assertEquals("xmlns:html", tagToken.getAttributes().keySet().iterator().next());
        assertEquals(Arrays.asList("xmlns:html"), tagToken.getAttributes().keySet());
        assertEquals(new Attributes().getClass(), tagToken.getAttributes().getClass());

        tagToken.newAttribute();
        tagToken.appendAttributeName("class");
        tagToken.appendAttributeValue("myClass");
        tagToken.newAttribute();
        tagToken.appendAttributeName("id");
        tagToken.appendAttributeValue("myId");
        tagToken.finaliseTag();

        assertEquals("class=myClass id=myId", tagToken.toString());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals(Token.TokenType.Comment, commentToken.type);
        assertEquals(Token.TokenType.Comment, resetCommentToken.type);

        commentToken.getData().append("This is a comment.");
        assertEquals("This is a comment.", commentToken.getData().toString());
        assertEquals("This is a comment.", commentToken.toString());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals(Token.TokenType.Character, characterToken.type);
        assertEquals(Token.TokenType.Character, resetCharacterToken.type);

        characterToken.data("&lt;");
        assertEquals("&lt;", characterToken.getData().toString());
        assertEquals("<", characterToken.toString());

        characterToken.data("&amp;");
        assertEquals("&amp;", characterToken.getData().toString());
        assertEquals("&", characterToken.toString());
    }

    @org.junit.Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Token resetCDataToken = cDataToken.reset();

        assertEquals(Token.TokenType.Character, cDataToken.type);
        assertEquals(Token.TokenType.Character, resetCDataToken.type);

        assertEquals("<![CDATA[\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n]]>", cDataToken.toString());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertEquals(Token.TokenType.EOF, eofToken.type);
        assertEquals(Token.TokenType.EOF, resetEofToken.type);

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertFalse(eofToken.isCData());
    }
}