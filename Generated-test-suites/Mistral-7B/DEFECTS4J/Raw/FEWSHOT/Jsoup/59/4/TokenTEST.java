package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertNull(doctype.pubSysKey);
        assertNull(doctype.publicIdentifier);
        assertNull(doctype.systemIdentifier);
        assertFalse(doctype.isForceQuirks());

        doctype.pubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());

        doctype.publicIdentifier("publicid");
        assertEquals("publicid", doctype.getPublicIdentifier());

        doctype.systemIdentifier("systemid");
        assertEquals("systemid", doctype.getSystemIdentifier());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testResetDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.reset();

        assertNull(doctype.getName());
        assertNull(doctype.pubSysKey);
        assertNull(doctype.publicIdentifier);
        assertNull(doctype.systemIdentifier);
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2");
        startTag.nameAttr("class", attributes);
        assertEquals("class1 class2", startTag.getAttributes().toString());

        String tagName = "table";
        startTag.name(tagName);
        assertEquals(tagName, startTag.name());
        assertEquals(tagName.toLowerCase(), startTag.normalName());

        assertEquals("<div class=\"class1 class2\">", startTag.toString());
    }

    @Test
    public void testResetStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertSame(startTag, startTag.reset());

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());
        assertFalse(endTag.isSelfClosing());

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testResetEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        assertSame(endTag, endTag.reset());

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertFalse(endTag.isSelfClosing);
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("Comment", comment.tokenType());

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testResetCommentToken() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertSame(comment, comment.reset());

        assertNull(comment.data);
        assertTrue(comment.bogus);
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("Hello, World!");
        assertEquals("Character", character.tokenType());

        assertEquals("Hello, World!", character.getData());

        assertEquals("Hello, World!", character.toString());
    }

    @Test
    public void testResetCharacterToken() {
        Character character = new Character();
        character.data("Hello, World!");
        assertSame(character, character.reset());

        assertNull(character.data);
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsDoctypeToken() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isDoctype());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isDoctype());

        Comment comment = new Comment();
        assertFalse(comment.isDoctype());

        Character character = new Character();
        assertFalse(character.isDoctype());

        EOF eof = new EOF();
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctypeToken() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());
        Doctype doctype2 = (Doctype) doctype.asDoctype();
        assertSame(doctype, doctype2);

        StartTag startTag = new StartTag();
        assertFalse(startTag.isDoctype());
        assertNull(startTag.asDoctype());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isDoctype());
        assertNull(endTag.asDoctype());

        Comment comment = new Comment();
        assertFalse(comment.isDoctype());
        assertNull(comment.asDoctype());

        Character character = new Character();
        assertFalse(character.isDoctype());
        assertNull(character.asDoctype());

        EOF eof = new EOF();
        assertFalse(eof.isDoctype());
        assertNull(eof.asDoctype());
    }

    @Test
    public void testIsStartTagToken() {
        StartTag startTag = new StartTag();
        assertTrue(startTag.isStartTag());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isStartTag());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isStartTag());

        Comment comment = new Comment();
        assertFalse(comment.isStartTag());

        Character character = new Character();
        assertFalse(character.isStartTag());

        EOF eof = new EOF();
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTagToken() {
        StartTag startTag = new StartTag();
        assertTrue(startTag.isStartTag());
        StartTag startTag2 = (StartTag) startTag.asStartTag();
        assertSame(startTag, startTag2);

        Doctype doctype = new Doctype();
        assertFalse(doctype.isStartTag());
        assertNull(doctype.asStartTag());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isStartTag());
        assertNull(endTag.asStartTag());

        Comment comment = new Comment();
        assertFalse(comment.isStartTag());
        assertNull(comment.asStartTag());

        Character character = new Character();
        assertFalse(character.isStartTag());
        assertNull(character.asStartTag());

        EOF eof = new EOF();
        assertFalse(eof.isStartTag());
        assertNull(eof.asStartTag());
    }

    @Test
    public void testIsEndTagToken() {
        EndTag endTag = new EndTag();
        assertTrue(endTag.isEndTag());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isEndTag());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isEndTag());

        Comment comment = new Comment();
        assertFalse(comment.isEndTag());

        Character character = new Character();
        assertFalse(character.isEndTag());

        EOF eof = new EOF();
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTagToken() {
        EndTag endTag = new EndTag();
        assertTrue(endTag.isEndTag());
        EndTag endTag2 = (EndTag) endTag.asEndTag();
        assertSame(endTag, endTag2);

        Doctype doctype = new Doctype();
        assertFalse(doctype.isEndTag());
        assertNull(doctype.asEndTag());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isEndTag());
        assertNull(startTag.asEndTag());

        Comment comment = new Comment();
        assertFalse(comment.isEndTag());
        assertNull(comment.asEndTag());

        Character character = new Character();
        assertFalse(character.isEndTag());
        assertNull(character.asEndTag());

        EOF eof = new EOF();
        assertFalse(eof.isEndTag());
        assertNull(eof.asEndTag());
    }

    @Test
    public void testIsCommentToken() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isComment());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isComment());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isComment());

        Character character = new Character();
        assertFalse(character.isComment());

        EOF eof = new EOF();
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsCommentToken() {
        Comment comment = new Comment();
        assertTrue(comment.isComment());
        Comment comment2 = (Comment) comment.asComment();
        assertSame(comment, comment2);

        Doctype doctype = new Doctype();
        assertFalse(doctype.isComment());
        assertNull(doctype.asComment());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isComment());
        assertNull(startTag.asComment());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isComment());
        assertNull(endTag.asComment());

        Character character = new Character();
        assertFalse(character.isComment());
        assertNull(character.asComment());

        EOF eof = new EOF();
        assertFalse(eof.isComment());
        assertNull(eof.asComment());
    }

    @Test
    public void testIsCharacterToken() {
        Character character = new Character();
        assertTrue(character.isCharacter());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isCharacter());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isCharacter());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isCharacter());

        Comment comment = new Comment();
        assertFalse(comment.isCharacter());

        EOF eof = new EOF();
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacterToken() {
        Character character = new Character();
        assertTrue(character.isCharacter());
        Character character2 = (Character) character.asCharacter();
        assertSame(character, character2);

        Doctype doctype = new Doctype();
        assertFalse(doctype.isCharacter());
        assertNull(doctype.asCharacter());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isCharacter());
        assertNull(startTag.asCharacter());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isCharacter());
        assertNull(endTag.asCharacter());

        Comment comment = new Comment();
        assertFalse(comment.isCharacter());
        assertNull(comment.asCharacter());

        EOF eof = new EOF();
        assertFalse(eof.isCharacter());
        assertNull(eof.asCharacter());
    }

    @Test
    public void testIsEOFToken() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isEOF());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isEOF());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isEOF());

        Comment comment = new Comment();
        assertFalse(comment.isEOF());

        Character character = new Character();
        assertFalse(character.isEOF());
    }

    @Test
    public void testAsEOFToken() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
        EOF eof2 = (EOF) eof.asEOF();
        assertSame(eof, eof2);

        Doctype doctype = new Doctype();
        assertFalse(doctype.isEOF());
        assertNull(doctype.asEOF());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isEOF());
        assertNull(startTag.asEOF());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isEOF());
        assertNull(endTag.asEOF());

        Comment comment = new Comment();
        assertFalse(comment.isEOF());
        assertNull(comment.asEOF());

        Character character = new Character();
        assertFalse(character.isEOF());
        assertNull(character.asEOF());
    }
}