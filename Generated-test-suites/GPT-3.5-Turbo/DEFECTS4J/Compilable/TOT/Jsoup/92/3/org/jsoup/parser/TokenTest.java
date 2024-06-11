package org.jsoup.parser;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctypeTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.name.toString());
        Assert.assertNull(doctype.pubSysKey);
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        Assert.assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        Assert.assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        Assert.assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagTokenType() {
        Token token = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, token.type);
    }

    // Additional test cases for other methods and classes will be added here.

}