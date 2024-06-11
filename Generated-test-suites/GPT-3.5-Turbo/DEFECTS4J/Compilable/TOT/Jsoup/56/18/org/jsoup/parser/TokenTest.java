package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class TokenTest {

    @Before
    public void setUp() {
        // initialization code
    }

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertNotNull(token.type);
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testResetWithDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");

        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");

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
        character.data("character data");

        assertEquals("character data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();

        assertEquals(true, token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();

        assertNotNull(doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();

        assertEquals(true, token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();

        assertNotNull(startTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();

        assertEquals(true, token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();

        assertNotNull(endTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();

        assertEquals(true, token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();

        assertNotNull(comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();

        assertEquals(true, token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        Token.Character character = token.asCharacter();

        assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        assertEquals(true, token.isEOF());
    }

    @Test
    public void testCompleteTokenClass() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertNotNull(doctype);
        assertNotNull(startTag);
        assertNotNull(endTag);
        assertNotNull(comment);
        assertNotNull(character);
        assertNotNull(eof);
    }

}