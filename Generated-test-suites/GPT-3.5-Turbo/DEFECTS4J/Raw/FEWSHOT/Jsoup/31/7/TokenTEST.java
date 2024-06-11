
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character("");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getName());

        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getPublicIdentifier());

        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getSystemIdentifier());

        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(false, doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.name());

        startTag.name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(false, startTag.isSelfClosing());

        startTag.selfClosing = true;
        assertEquals(true, startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.getAttributes());

        Attributes attributes = new Attributes();
        startTag.attributes = attributes;
        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagNameString() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.name());

        startTag.appendTagName("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.name());

        startTag.appendTagName('d');
        assertEquals("d", startTag.name());
    }

    @Test
    public void testStartTagAppendAttributeNameString() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeName);

        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeName);

        startTag.appendAttributeName('c');
        assertEquals("c", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValueString() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeValue);

        startTag.appendAttributeValue("container");
        assertEquals("container", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.pendingAttributeValue);

        startTag.appendAttributeValue('c');
        assertEquals("c", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.name());

        endTag.name("div");
        assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assertEquals("", comment.getData());

        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("");
        assertEquals("", character.getData());

        character.data.append("This is some character data");
        assertEquals("This is some character data", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertEquals(true, token.isDoctype());

        token = new Token.StartTag();
        assertEquals(false, token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertEquals(Token.Doctype.class, token.asDoctype().getClass());

        token = new Token.StartTag();
        assertEquals(null, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertEquals(true, token.isStartTag());

        token = new Token.Doctype();
        assertEquals(false, token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertEquals(Token.StartTag.class, token.asStartTag().getClass());

        token = new Token.Doctype();
        assertEquals(null, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertEquals(true, token.isEndTag());

        token = new Token.Doctype();
        assertEquals(false, token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertEquals(Token.EndTag.class, token.asEndTag().getClass());

        token = new Token.Doctype();
        assertEquals(null, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertEquals(true, token.isComment());

        token = new Token.Doctype();
        assertEquals(false, token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertEquals(Token.Comment.class, token.asComment().getClass());

        token = new Token.Doctype();
        assertEquals(null, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("");
        assertEquals(true, token.isCharacter());

        token = new Token.Doctype();
        assertEquals(false, token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("");
        assertEquals(Token.Character.class, token.asCharacter().getClass());

        token = new Token.Doctype();
        assertEquals(null, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertEquals(true, token.isEOF());

        token = new Token.Doctype();
        assertEquals(false, token.isEOF());
    }
}
