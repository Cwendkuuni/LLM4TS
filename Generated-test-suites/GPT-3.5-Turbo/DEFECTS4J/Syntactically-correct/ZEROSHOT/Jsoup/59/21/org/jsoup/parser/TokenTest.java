package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.reset();
        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals(null, doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.reset();
        Assert.assertEquals(null, startTag.tagName);
        Assert.assertEquals(null, startTag.normalName);
        Assert.assertEquals(null, startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, startTag.pendingAttributeValueS);
        Assert.assertEquals(false, startTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, startTag.hasPendingAttributeValue);
        Assert.assertEquals(false, startTag.selfClosing);
        Assert.assertEquals(null, startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        Assert.assertEquals(null, endTag.tagName);
        Assert.assertEquals(null, endTag.normalName);
        Assert.assertEquals(null, endTag.pendingAttributeName);
        Assert.assertEquals("", endTag.pendingAttributeValue.toString());
        Assert.assertEquals(null, endTag.pendingAttributeValueS);
        Assert.assertEquals(false, endTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, endTag.hasPendingAttributeValue);
        Assert.assertEquals(false, endTag.selfClosing);
        Assert.assertEquals(null, endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        Assert.assertEquals("", comment.getData());
        Assert.assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        Assert.assertEquals(null, character.getData());
    }

    @Test
    public void testEOFReset() {
        eof.reset();
    }
}