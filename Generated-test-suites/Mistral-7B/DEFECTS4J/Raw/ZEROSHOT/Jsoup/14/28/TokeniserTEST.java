package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        reader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><p>Hello World</p></body>".getBytes()));
        errors = new ArrayList<>();
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testReadEmptyStream() {
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacter() {
        tokeniser.emit('T');
        tokeniser.emit('e');
        tokeniser.emit('s');
        tokeniser.emit('t');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", token.getText());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadStartTag() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.read();
        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertTrue(startTag instanceof Token.StartTag);
        assertEquals("html", ((Token.StartTag) startTag).tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadEndTag() {
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.read();
        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertTrue(endTag instanceof Token.EndTag);
        assertEquals("html", ((Token.EndTag) endTag).tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadString() {
        tokeniser.emit('H');
        tokeniser.emit('e');
        tokeniser.emit('l');
        tokeniser.emit('l');
        tokeniser.emit('o');
        tokeniser.emit(' ');
        tokeniser.emit('W');
        tokeniser.emit('o');
        tokeniser.emit('r');
        tokeniser.emit('l');
        tokeniser.emit('d');
        tokeniser.emit(' ');
        tokeniser.read();
        tokeniser.read();
        Token stringToken = tokeniser.read();
        assertEquals(Token.TokenType.String, stringToken.type);
        assertEquals("Hello World", stringToken.getText());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadError() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('X');
        tokeniser.reader.consume(' '); // unconsume
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.isTrackErrors());
        errors.clear();
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        assertEquals(1, errors.size());
        assertEquals("Unexpected character in input", errors.get(0).getMessage());
        assertEquals(6, errors.get(0).getLineNumber());
        assertEquals(TokeniserState.Data, errors.get(0).getState());
        assertEquals(11, errors.get(0).getPosition());
    }

    @Test
    public void testReadEof() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('X');
        tokeniser.reader.consume(' '); // unconsume
        tokeniser.reader.consume(' ');
        tokeniser.reader.consume(' ');
        tokeniser.reader.consume('\n');
        tokeniser.reader.consume('\n');
        tokeniser.reader.consume('>');
        tokeniser.read(); // read <head>
        tokeniser.read(); // read <title>
        tokeniser.read(); // read <title>
        tokeniser.read(); // read >
        tokeniser.read(); // read <body>
        tokeniser.read(); // read <p>
        tokeniser.read(); // read H
        tokeniser.read(); // read e
        tokeniser.read(); // read l
        tokeniser.read(); // read o
        tokeniser.read(); // read >
        tokeniser.read(); // read <p>
        tokeniser.read(); // read >
        tokeniser.reader.consume('\n');
        tokeniser.reader.consume('\n');
        tokeniser.reader.consume('\n');
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.isTrackErrors());
        errors.clear();
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
        assertEquals(0, errors.get(0).getLineNumber());
        assertEquals(TokeniserState.Data, errors.get(0).getState());
        assertEquals(41, errors.get(0).getPosition());
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference('&', false);
        assertNull(reference);
        assertEquals(0, errors.size());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume(' '); // unconsume
        Character reference2 = tokeniser.consumeCharacterReference(Character.class, false);
        assertEquals((char) 0x0068, reference2);
        assertEquals("&x;", tokeniser.reader.currentString());
        assertEquals(0, errors.size());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('x');
        tokeniser.reader.consume(' '); // unconsume
        tokeniser.reader.consume('p');
        tokeniser.reader.consume(';');
        Character reference3 = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u0007', reference3);
        assertEquals("&x;", tokeniser.reader.currentString());
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).getMessage());
        assertEquals(0, errors.get(0).getLineNumber());
        assertEquals(TokeniserState.Data, errors.get(0).getState());
        assertEquals(5, errors.get(0).getPosition());
    }

    @Test
    public void testCreateTagPending() {
        Token tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);
        assertEquals(0, tagPending.attributes.size());
        tagPending.tagName = "div";
        tagPending.tagName = "p";
        assertEquals("p", tagPending.tagName);
        assertEquals(0, errors.size());

        tokeniser.emitTagPending();
        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertTrue(startTag instanceof Token.StartTag);
        assertEquals("p", ((Token.StartTag) startTag).tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token commentPending = tokeniser.read();
        assertEquals(Token.TokenType.Comment, commentPending.type);
        assertTrue(commentPending instanceof Token.Comment);
        assertEquals("", commentPending.getText());
        assertEquals(0, errors.size());

        tokeniser.emitCommentPending();
        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertTrue(comment instanceof Token.Comment);
        assertEquals("", comment.getText());
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token doctypePending = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctypePending.type);
        assertTrue(doctypePending instanceof Token.Doctype);
        assertEquals(0, errors.size());

        tokeniser.emitDoctypePending();
        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertTrue(doctype instanceof Token.Doctype);
        assertEquals(0, errors.size());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.setTrackErrors(false);
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals(0, errors.size());

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertFalse(tokeniser.isAppropriateEndTagToken());
        assertEquals(0, errors.size());
    }
}