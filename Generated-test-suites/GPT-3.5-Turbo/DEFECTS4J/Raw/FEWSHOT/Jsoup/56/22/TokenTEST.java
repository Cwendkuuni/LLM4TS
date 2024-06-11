package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;
        startTag.attributes = new org.jsoup.nodes.Attributes().put("class", "container");

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";
        endTag.normalName = "p";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Some text";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testDoctypeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");

        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "div";

        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        assertEquals(true, startTag.isSelfClosing());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";

        assertEquals("p", endTag.name());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data = "Some text";

        assertEquals("Some text", character.getData());
    }

    @Test
    public void testDoctypeIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals(true, doctype.isDoctype());
    }

    @Test
    public void testDoctypeAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testStartTagIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals(true, startTag.isStartTag());
    }

    @Test
    public void testStartTagAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testEndTagIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals(true, endTag.isEndTag());
    }

    @Test
    public void testEndTagAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testCommentIsComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals(true, comment.isComment());
    }

    @Test
    public void testCommentAsComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testCharacterIsCharacter() {
        Token.Character character = new Token.Character();

        assertEquals(true, character.isCharacter());
    }

    @Test
    public void testCharacterAsCharacter() {
        Token.Character character = new Token.Character();

        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals(true, eof.isEOF());
    }

    @Test
    public void testEOFAsEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals(eof, eof.asEOF());
    }
}