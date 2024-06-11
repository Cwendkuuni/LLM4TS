package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TestName");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.name("testName");
        startTag.selfClosing = true;

        assertNotNull(startTag.getAttributes());
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.name("testName");

        assertEquals("</testName>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("Test Comment");

        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();

        character.data("Test Character");

        assertEquals("Test Character", character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("Test CData");

        assertEquals("<![CDATA[Test CData]]>", cdata.toString());
    }
}