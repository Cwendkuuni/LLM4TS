package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        Assert.assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character();
        Assert.assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        Assert.assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    // Add more test methods for other methods in the Token class

}