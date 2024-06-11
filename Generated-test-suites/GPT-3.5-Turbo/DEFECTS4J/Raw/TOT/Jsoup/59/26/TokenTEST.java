package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private static Token.Doctype doctype;
    private static Token.StartTag startTag;
    private static Token.EndTag endTag;
    private static Token.Comment comment;
    private static Token.Character character;
    private static Token.EOF eof;

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
        doctype.name.append("HTML");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.0 Transitional//EN";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myDiv");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertTrue(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.data("This is some text");

        character.reset();

        Assert.assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        eof.reset();

        Assert.assertNotNull(eof);
    }
}