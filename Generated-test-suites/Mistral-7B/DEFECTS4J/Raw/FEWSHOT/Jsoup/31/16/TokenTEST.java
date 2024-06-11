package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype token = new Doctype();
        token.getName().equals("doctype");

        Doctype tokenWithName = new Doctype();
        tokenWithName.name("html");
        tokenWithName.getName().equals("html");

        Doctype tokenWithPublicIdentifier = new Doctype();
        tokenWithPublicIdentifier.name("html");
        tokenWithPublicIdentifier.getPublicIdentifier().equals("");
        tokenWithPublicIdentifier.setPublicIdentifier("PUBLIC");
        tokenWithPublicIdentifier.getPublicIdentifier().equals("PUBLIC");

        Doctype tokenWithSystemIdentifier = new Doctype();
        tokenWithSystemIdentifier.name("html");
        tokenWithSystemIdentifier.getSystemIdentifier().equals("");
        tokenWithSystemIdentifier.setSystemIdentifier("system.xml");
        tokenWithSystemIdentifier.getSystemIdentifier().equals("system.xml");

        Doctype tokenWithForceQuirks = new Doctype();
        tokenWithForceQuirks.name("html");
        tokenWithForceQuirks.setForceQuirks(true);
        tokenWithForceQuirks.isForceQuirks();
        assertTrue(tokenWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        tag.tagName = "tag";
        tag.newAttribute();
        tag.appendAttributeName("attr");
        tag.appendAttributeValue("value");
        tag.newAttribute();
        tag.getName().equals("tag");
        tag.newAttribute().getName().equals("attr");
        tag.newAttribute().getValue().equals("value");
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("tag");
        startTag.newAttribute();
        startTag.appendAttributeName("attr");
        startTag.appendAttributeValue("value");
        startTag.newAttribute();
        startTag.getName().equals("tag");
        startTag.newAttribute().getName().equals("attr");
        startTag.newAttribute().getValue().equals("value");

        StartTag startTagWithAttributes = new StartTag();
        startTagWithAttributes.name("tag");
        startTagWithAttributes.attributes = new Attributes();
        startTagWithAttributes.attributes.put("attr1", new Attribute("attr1", "value1"));
        startTagWithAttributes.newAttribute();
        startTagWithAttributes.appendAttributeName("attr2");
        startTagWithAttributes.appendAttributeValue("value2");
        startTagWithAttributes.newAttribute();
        startTagWithAttributes.getName().equals("tag");
        startTagWithAttributes.newAttribute().getName().equals("attr2");
        startTagWithAttributes.newAttribute().getValue().equals("value2");

        StartTag startTagWithSelfClosing = new StartTag();
        startTagWithSelfClosing.name("img");
        startTagWithSelfClosing.selfClosing = true;
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.name("tag");

        EndTag endTagWithAttributes = new EndTag();
        endTagWithAttributes.name("tag");
        endTagWithAttributes.attributes = new Attributes();
        endTagWithAttributes.attributes.put("attr1", new Attribute("attr1", "value1"));
        endTagWithAttributes.newAttribute();
        endTagWithAttributes.appendAttributeName("attr2");
        endTagWithAttributes.appendAttributeValue("value2");
        endTagWithAttributes.newAttribute();

        EndTag endTagWithSelfClosing = new EndTag();
        endTagWithSelfClosing.name("img");
        endTagWithSelfClosing.selfClosing = true;
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.appendAttributeName("data");
        comment.appendAttributeValue("--This is a comment--");
        comment.newAttribute();
        comment.getName().equals("comment");
        comment.newAttribute().getName().equals("data");
        comment.newAttribute().getValue().equals("--This is a comment--");
    }

    @Test
    public void testCharacter() {
        Character character = new Character("");
        character.getData().equals("");

        Character characterWithData = new Character("data");
        characterWithData.getData().equals("data");

        Character characterWithSingleQuote = new Character("'");
        characterWithSingleQuote.getData().equals("'");

        Character characterWithDoubleQuote = new Character("\"");
        characterWithDoubleQuote.getData().equals("\"");
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        eof.isEOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        Doctype doctype = new Doctype();
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
        assertTrue(doctype.isDoctype());

        StartTag startTag = new StartTag();
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());
        assertTrue(startTag.isStartTag());

        EndTag endTag = new EndTag();
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());
        assertTrue(endTag.isEndTag());

        Comment comment = new Comment();
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        Character character = new Character("");
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertTrue(character.isCharacter());
        assertFalse(character.isEOF());

        EOF eof = new EOF();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}