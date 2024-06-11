package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token expectedDoctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(expectedDoctype.getClass(), doctype.getClass());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.name("html");
        Token expectedDoctypeWithName = new Token.Doctype();
        expectedDoctypeWithName.name("html");
        assertEquals(expectedDoctypeWithName, doctypeWithName);

        Token doctypeWithNameAndPublicIdentifier = new Token.Doctype();
        doctypeWithNameAndPublicIdentifier.name("html");
        doctypeWithNameAndPublicIdentifier.publicIdentifier("XHTML");
        Token expectedDoctypeWithNameAndPublicIdentifier = new Token.Doctype();
        expectedDoctypeWithNameAndPublicIdentifier.name("html");
        expectedDoctypeWithNameAndPublicIdentifier.publicIdentifier("XHTML");
        assertEquals(expectedDoctypeWithNameAndPublicIdentifier, doctypeWithNameAndPublicIdentifier);

        Token doctypeWithNameAndPublicIdentifierAndSystemIdentifier = new Token.Doctype();
        doctypeWithNameAndPublicIdentifierAndSystemIdentifier.name("html");
        doctypeWithNameAndPublicIdentifierAndSystemIdentifier.publicIdentifier("XHTML");
        doctypeWithNameAndPublicIdentifierAndSystemIdentifier.systemIdentifier("index.html");
        Token expectedDoctypeWithNameAndPublicIdentifierAndSystemIdentifier = new Token.Doctype();
        expectedDoctypeWithNameAndPublicIdentifierAndSystemIdentifier.name("html");
        expectedDoctypeWithNameAndPublicIdentifierAndSystemIdentifier.publicIdentifier("XHTML");
        expectedDoctypeWithNameAndPublicIdentifierAndSystemIdentifier.systemIdentifier("index.html");
        assertEquals(expectedDoctypeWithNameAndPublicIdentifierAndSystemIdentifier, doctypeWithNameAndPublicIdentifierAndSystemIdentifier);

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.forceQuirks(true);
        Token expectedDoctypeWithForceQuirks = new Token.Doctype();
        expectedDoctypeWithForceQuirks.forceQuirks(true);
        assertEquals(expectedDoctypeWithForceQuirks, doctypeWithForceQuirks);
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        Token expectedTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.type);
        assertEquals(expectedTag.getClass(), tag.getClass());

        Token tagWithName = new Token.StartTag();
        tagWithName.name("p");
        Token expectedTagWithName = new Token.StartTag();
        expectedTagWithName.name("p");
        assertEquals(expectedTagWithName, tagWithName);

        Token tagWithAttributes = new Token.StartTag();
        tagWithAttributes.name("div");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("class");
        tagWithAttributes.appendAttributeValue("foo bar");
        tagWithAttributes.newAttribute();
        tagWithAttributes.appendAttributeName("id");
        tagWithAttributes.appendAttributeValue("myDiv");
        Token expectedTagWithAttributes = new Token.StartTag();
        expectedTagWithAttributes.name("div");
        Attributes attributes = new Attributes();
        attributes.put("class", "foo bar");
        attributes.put("id", "myDiv");
        expectedTagWithAttributes.attributes = attributes;
        assertEquals(expectedTagWithAttributes, tagWithAttributes);

        Token tagWithSelfClosing = new Token.StartTag();
        tagWithSelfClosing.selfClosing(true);
        tagWithSelfClosing.name("img");
        Token expectedTagWithSelfClosing = new Token.StartTag();
        expectedTagWithSelfClosing.selfClosing(true);
        expectedTagWithSelfClosing.name("img");
        assertEquals(expectedTagWithSelfClosing, tagWithSelfClosing);

        Token tagWithNameAndAttributes = new Token.StartTag();
        tagWithNameAndAttributes.name("button");
        tagWithNameAndAttributes.newAttribute();
        tagWithNameAndAttributes.appendAttributeName("type");
        tagWithNameAndAttributes.appendAttributeValue("button");
        tagWithNameAndAttributes.newAttribute();
        tagWithNameAndAttributes.appendAttributeName("name");
        tagWithNameAndAttributes.appendAttributeValue("submit");
        Token expectedTagWithNameAndAttributes = new Token.StartTag();
        expectedTagWithNameAndAttributes.name("button");
        Attributes attributes1 = new Attributes();
        attributes1.put("type", "button");
        expectedTagWithNameAndAttributes.attributes = attributes1;
        Token expectedTagWithNameAndAttributes2 = new Token.StartTag();
        expectedTagWithNameAndAttributes2.name("button");
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "submit");
        expectedTagWithNameAndAttributes2.attributes = attributes2;
        assertEquals(expectedTagWithNameAndAttributes1, tagWithNameAndAttributes);
        assertEquals(expectedTagWithNameAndAttributes2, tagWithNameAndAttributes);
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token expectedEndTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals(expectedEndTag.getClass(), endTag.getClass());

        Token endTagWithName = new Token.EndTag();
        endTagWithName.name("p");
        Token expectedEndTagWithName = new Token.EndTag();
        expectedEndTagWithName.name("p");
        assertEquals(expectedEndTagWithName, endTagWithName);
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token expectedComment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertEquals(expectedComment.getClass(), comment.getClass());

        Token commentWithData = new Token.Comment();
        commentWithData.data.append("This is a test comment.");
        Token expectedCommentWithData = new Token.Comment();
        expectedCommentWithData.data.append("This is a test comment.");
        assertEquals(expectedCommentWithData, commentWithData);
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&lt;");
        Token expectedCharacter = new Token.Character("&lt;");
        assertEquals(TokenType.Character, character.type);
        assertEquals(expectedCharacter.getClass(), character.getClass());
        assertEquals("&lt;", character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        Token expectedEof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals(expectedEof.getClass(), eof.getClass());
    }
}