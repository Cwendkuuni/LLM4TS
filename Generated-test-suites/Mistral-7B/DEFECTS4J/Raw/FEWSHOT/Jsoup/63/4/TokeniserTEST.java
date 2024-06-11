package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Entities;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testReadAndEmit() {
        CharacterReader reader = new CharacterReader("abcdef<xyz>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        do {
            token = tokeniser.read();
            if (token != null) {
                tokeniser.emit(token);
            }
        } while (token != null);

        assertEquals(5, tokeniser.read().type.ordinal()); // EOF
    }

    @Test
    public void testReadAndEmitCharacter() {
        CharacterReader reader = new CharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Token expected = new Token.Character();
        assertEquals(expected.type, token.type);
        assertEquals(Character.toString(expected.data[0]), Character.toString(token.data));
    }

    @Test
    public void testReadAndEmitStartTag() {
        CharacterReader reader = new CharacterReader("<p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Token expected = new Token.StartTag("p");
        assertEquals(expected.type, token.type);
        assertEquals(expected.tagName, token.tagName);
        assertNull(token.data);
        assertNull(token.children);
    }

    @Test
    public void testReadAndEmitEndTag() {
        CharacterReader reader = new CharacterReader("</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Token expected = new Token.EndTag("p");
        assertEquals(expected.type, token.type);
        assertEquals(expected.tagName, token.tagName);
        assertNull(token.data);
        assertNull(token.children);
    }

    @Test
    public void testReadAndEmitEmptyElement() {
        CharacterReader reader = new CharacterReader("<img/>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Token expected = new Token.StartTag("img");
        expected.selfClosing = true;
        assertEquals(expected.type, token.type);
        assertEquals(expected.tagName, token.tagName);
        assertNull(token.data);
        assertNull(token.children);
    }

    @Test
    public void testReadAndEmitStartTagWithAttributes() {
        CharacterReader reader = new CharacterReader("<p class='red' id='item1'>Hello World</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        tokeniser.emit(startTag);

        Token expectedStartTag = new Token.StartTag("p");
        assertEquals(expectedStartTag.type, startTag.type);
        assertEquals(expectedStartTag.tagName, startTag.tagName);
        assertNull(startTag.data);

        Token attributes = startTag.attributes;
        Token attribute1 = attributes.get(0);
        Token attribute2 = attributes.get(1);

        assertEquals("class", attribute1.name);
        assertEquals("red", attribute1.value);

        assertEquals("id", attribute2.name);
        assertEquals("item1", attribute2.value);

        Token content = startTag.children.get(0);
        assertEquals(Token.TokenType.Text, content.type);
        assertEquals("Hello World", content.data);
    }

    @Test
    public void testReadAndEmitScriptTag() {
        CharacterReader reader = new CharacterReader("<script src='script.js'></script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        Token expected = new Token.StartTag("script");
        expected.attributes = new Token.AttributeList();
        Token attribute = new Token.Attribute();
        attribute.name = "src";
        attribute.value = "script.js";
        expected.attributes.add(attribute);
        expected.selfClosing = true;

        assertEquals(expected.type, token.type);
        assertEquals(expected.tagName, token.tagName);
        assertEquals(expected.attributes, token.attributes);
        assertNull(token.data);
        assertNull(token.children);
    }

    @Test
    public void testReadAndEmitError() {
        CharacterReader reader = new CharacterReader("<foo>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        ParseError error = errors.get(0);
        assertEquals("Unexpected character 'f' in input state [Data]", error.getMessage());
        assertEquals(1, errors.size());
    }

    @Test
    public void testReadAndEmitEofError() {
        CharacterReader reader = new CharacterReader("hello");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        tokeniser.emit(token);

        token = tokeniser.read();
        tokeniser.emit(token);

        tokeniser.read(); // consume EOF

        assertEquals(Token.TokenType.Eof, tokeniser.read().type);

        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
        assertEquals(1, errors.size());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&eacute;");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        int[] codepoint = tokeniser.consumeCharacterReference(null, false);
        assertEquals(0x00E9, codepoint[0]);
    }

    @Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
        CharacterReader reader = new CharacterReader("&#xCA&eacute;");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        int[] codepoint = tokeniser.consumeCharacterReference('&', false);
        assertEquals(0x00CA, codepoint[0]);
    }

    @Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacterInAttribute() {
        CharacterReader reader = new CharacterReader("class='&eacute;red'");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        int[] codepoint = tokeniser.consumeCharacterReference(null, true);
        assertEquals(0x00E9, codepoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeCharacterReferenceWithInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&foo;");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        tokeniser.consumeCharacterReference(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeCharacterReferenceWithInvalidCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader("class='&foo;red'");
        Tokeniser tokeniser = new Tokeniser(reader, null);

        tokeniser.consumeCharacterReference(null, true);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        Token.StartTag startTagPending = tokeniser.createTagPending(true);
        Token.EndTag endTagPending = tokeniser.createTagPending(false);

        assertSame(Tokens.START_TAG, startTagPending.tagType);
        assertSame(Tokens.END_TAG, endTagPending.tagType);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("p");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.StartTag startTagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token expected = new Token.StartTag("p");
        assertEquals(expected.type, startTagPending.type);
        assertEquals(expected.tagName, startTagPending.tagName);
        assertNull(startTagPending.data);
        assertNull(startTagPending.children);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        Token commentPending = tokeniser.commentPending;

        assertEquals(Token.TokenType.Comment, commentPending.type);
        assertEquals(commentPending.data, tokeniser.commentPending.data);
        assertNull(commentPending.tagName);
        assertNull(commentPending.children);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token comment = tokeniser.commentPending;
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(comment.data, tokeniser.commentPending.data);
        assertNull(comment.tagName);
        assertNull(comment.children);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        Token doctypePending = tokeniser.doctypePending;

        assertEquals(Token.TokenType.Doctype, doctypePending.type);
        assertEquals(doctypePending.data, tokeniser.doctypePending.data);
        assertNull(doctypePending.tagName);
        assertNull(doctypePending.children);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token doctype = tokeniser.doctypePending;
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(doctype.data, tokeniser.doctypePending.data);
        assertNull(doctype.tagName);
        assertNull(doctype.children);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("<p><p>Hello World</p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.read();

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("<p><p>Hello World</p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String endTagName = tokeniser.appropriateEndTagName();
        assertEquals("p", endTagName);

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        endTagName = tokeniser.appropriateEndTagName();
        assertEquals("p", endTagName);

        tokeniser.read();

        assertNull(tokeniser.appropriateEndTagName());
    }
}