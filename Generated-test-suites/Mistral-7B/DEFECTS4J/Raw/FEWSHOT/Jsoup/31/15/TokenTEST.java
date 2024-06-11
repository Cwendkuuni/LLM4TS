package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token docType = new Token.Doctype();
        assertEquals("Doctype", docType.tokenType());
        assertTrue(docType instanceof Token.Doctype);

        Token docTypeWithName = new Token.Doctype();
        docTypeWithName.asDoctype().getName().append("html");
        assertEquals("Doctype:html", docTypeWithName.tokenType());
        assertTrue(docTypeWithName.isDoctype());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        assertTrue(startTag instanceof Token.StartTag);

        Token startTagWithName = new Token.StartTag();
        startTagWithName.asStartTag().name("div");
        assertEquals("StartTag:div", startTagWithName.tokenType());
        assertTrue(startTagWithName.isStartTag());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().name("a").attributes().put("href", "http://example.com");
        assertEquals("StartTag:a:href=http://example.com", startTagWithAttributes.tokenType());
        assertTrue(startTagWithAttributes.isStartTag());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        assertTrue(endTag instanceof Token.EndTag);

        Token endTagWithName = new Token.EndTag();
        endTagWithName.asEndTag().name("p");
        assertEquals("EndTag:p", endTagWithName.tokenType());
        assertTrue(endTagWithName.isEndTag());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        assertTrue(comment instanceof Token.Comment);

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment.");
        assertEquals("Comment:This is a comment.", commentWithData.tokenType());
        assertTrue(commentWithData.isComment());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("a");
        assertEquals("Character:a", character.tokenType());
        assertTrue(character instanceof Token.Character);

        Token characterWithData = new Token.Character("&nbsp;");
        assertEquals("Character:&nbsp;", characterWithData.tokenType());
        assertTrue(characterWithData.isCharacter());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }

    @Test
    public void testAttributes() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("a").attributes().put("href", "http://example.com").put("class", "example");
        Attributes attributes = startTag.asStartTag().getAttributes();
        assertEquals("href=http://example.com class=example", attributes.toString());
        assertEquals("href", attributes.keySet().iterator().next());
        assertEquals("http://example.com", attributes.get("href"));
        assertEquals("example", attributes.get("class"));
        assertNull(attributes.get("title"));
    }
}