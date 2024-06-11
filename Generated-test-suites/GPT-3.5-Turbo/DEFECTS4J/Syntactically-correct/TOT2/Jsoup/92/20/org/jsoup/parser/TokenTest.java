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
        doctype.name.append("test");
        doctype.reset();
        assertEquals("", doctype.getName());
    }

    @Test
    public void testStartTagCreation() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes());
        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("test");
        assertEquals("<![CDATA[test]]>", cdata.toString());
    }

    @Test
    public void testUnexpectedToken() {
        Token unexpectedToken = new Token.Character();
        assertNull(unexpectedToken.getData());
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        Token token = new Token.Character();
        Token.Doctype doctype = token.asDoctype();
    }

}

