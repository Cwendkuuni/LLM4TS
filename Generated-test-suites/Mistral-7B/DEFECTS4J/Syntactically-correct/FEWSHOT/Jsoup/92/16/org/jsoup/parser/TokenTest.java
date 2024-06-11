package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        doctype.reset();
        assertNotNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPubSysKey());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.name("p"); // should be overwritten by the second call
        tag.attributes(new Attributes());
        tag.attributes().put("class", "foo");
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("This is a comment");
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("A");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEFDRetest() {
        Token eof = new Token.EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypes() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());
    }
}