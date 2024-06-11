package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithNameAndPublicId = new Token.Doctype().name("html").publicIdentifier("_publicId");
        Token doctypeWithNamePublicIdAndSystemId = new Token.Doctype().name("html")
                .publicIdentifier("_publicId").systemIdentifier("_systemId");
        Token doctypeWithForceQuirks = new Token.Doctype().name("html").forceQuirks(true);

        assertEquals(TokenType.Doctype, doctype.tokenType());
        assertEquals(TokenType.Doctype, doctypeWithName.tokenType());
        assertEquals("Doctype", doctype.getClass().getSimpleName());
        assertEquals("Doctype", doctypeWithName.getClass().getSimpleName());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("html", doctypeWithNameAndPublicId.getName());
        assertEquals("html", doctypeWithNamePublicIdAndSystemId.getName());
        assertFalse(doctypeWithName.isForceQuirks());
        assertTrue(doctypeWithNameForceQuirks.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        Token startTagWithName = new Token.StartTag().name("p");
        Token startTagWithNameAndAttributes = new Token.StartTag().name("p").attributes(new Attributes().put("class", "test"));
        Token startTagWithNameSelfClosing = new Token.StartTag().name("img").selfClosing(true);

        assertEquals(TokenType.StartTag, startTag.tokenType());
        assertEquals(TokenType.StartTag, startTagWithName.tokenType());
        assertEquals("StartTag", startTag.getClass().getSimpleName());
        assertEquals("StartTag", startTagWithName.getClass().getSimpleName());
        assertEquals("p", startTagWithName.name());
        assertEquals("p", startTagWithNameAndAttributes.name());
        assertTrue(startTagWithNameSelfClosing.isSelfClosing());
        assertFalse(startTagWithName.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token endTagWithName = new Token.EndTag().name("p");

        assertEquals(TokenType.EndTag, endTag.tokenType());
        assertEquals(TokenType.EndTag, endTagWithName.tokenType());
        assertEquals("EndTag", endTag.getClass().getSimpleName());
        assertEquals("EndTag", endTagWithName.getClass().getSimpleName());
        assertEquals("p", endTagWithName.name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().getData("This is a comment");

        assertEquals(TokenType.Comment, comment.tokenType());
        assertEquals(TokenType.Comment, commentWithData.tokenType());
        assertEquals("Comment", comment.getClass().getSimpleName());
        assertEquals("Comment", commentWithData.getClass().getSimpleName());
        assertEquals("This is a comment", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        Token characterWithData = new Token.Character("&copy;");

        assertEquals(TokenType.Character, character.tokenType());
        assertEquals(TokenType.Character, characterWithData.tokenType());
        assertEquals("Character", character.getClass().getSimpleName());
        assertEquals("Character", characterWithData.getClass().getSimpleName());
        assertEquals("A", character.getData());
        assertEquals("&copy;", characterWithData.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals(TokenType.EOF, eof.tokenType());
        assertEquals("EOF", eof.getClass().getSimpleName());
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character("A");
        Token eof = new Token.EOF();

        assertFalse(doctype.isDoctype());
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        false;
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(character.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());

        Token doctypeWithName = new Token.Doctype().name("html");
        Token startTagWithName = new Token.StartTag().name("p");
        Token endTagWithName = new Token.EndTag().name("p");

        assertTrue(doctypeWithName.isDoctype());
        assertTrue(startTagWithName.isStartTag());
        assertTrue(endTagWithName.isEndTag());

        Token startTagWithNameAndAttributes = new Token.StartTag().name("p").attributes(new Attributes().put("class", "test"));
        assertTrue(startTagWithNameAndAttributes.isStartTag());
        assertTrue(startTagWithNameAndAttributes.getAttributes().size() > 0);
        Element element = new Element("p").classAttr("test");
        assertEquals(element.tagName(), startTagWithNameAndAttributes.name());
        assertEquals(element.attributes().size(), startTagWithNameAndAttributes.getAttributes().size());
    }
}