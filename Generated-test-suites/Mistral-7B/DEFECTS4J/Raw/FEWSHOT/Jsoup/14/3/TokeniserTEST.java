package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniserInstance() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.getState());
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testReadEmptyStream() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        for (String input : new String[]{"", "\'\'", "\'\'foo'", "\'\'foo\'", "\'\'foo\' bar\"", "\'\'foo\' bar\'\'", "\'\'_foo\'", "foo\'\'", "\'\'foo<tag\'\'"}) {
            String output = Tokeniser.stripLeadingAndTrailingQuotes(input);
            if (input.isEmpty()) {
                assertEquals("", output);
            } else if (input.charAt(0) == '\'' && input.charAt(input.length() - 1) == '\'') {
                assertEquals(input.substring(1, input.length() - 1), output);
            } else {
                assertEquals(input, output);
            }
        }
    }

    @Test
    public void testGetState() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        TokeniserState dataState = tokeniser.getState();
        tokeniser.transition(TokeniserState.TagName);
        assertFalse(dataState.equals(tokeniser.getState()));
        assertTrue(tokeniser.getState() == TokeniserState.TagName);
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        TokeniserState dataState = tokeniser.getState();
        tokeniser.advanceTransition(TokeniserState.TagName);
        reader.advance();
        tokeniser.advanceTransition(TokeniserState.TagName);
        assertFalse(dataState.equals(tokeniser.getState()));
        assertTrue(tokeniser.getState() == TokeniserState.TagName);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader reader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testReadWithDataBuffer() throws Exception {
        CharacterReader reader = new CharacterReader("abcdefghi<script>hello<script>");
        Tokeniser tokeniser = new Tokeniser(reader);
        StringBuilder dataBuffer = new StringBuilder();
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        tokeniser.emit('a');
        tokeniser.emit('b');
        tokeniser.emit('c');
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit("hello");
        tokeniser.emit(dataBuffer);
        tokeniser.emit('d');
        tokeniser.emit('e');
        tokeniser.emit('f');
        tokeniser.emit('g');
        tokeniser.emit('h');
        tokeniser.emit('i');
        assertEquals(12, tokeniser.errors.size());
    }

    @Test
    public void testReadWithDataBufferWithSelfClosingTag() throws Exception {
        CharacterReader reader = new CharacterReader("<script>hello<script/>");
        Tokeniser tokeniser = new Tokeniser(reader);
        StringBuilder dataBuffer = new StringBuilder();
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        tokeniser.emit('h');
        tokeniser.emit('e');
        tokeniser.emit('l');
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit("lo");
        tokeniser.emit('/');
        tokeniser.emit('s');
        tokeniser.emit('c');
        tokeniser.emit('r');
        tokeniser.emit('i');
        tokeniser.emit('p');
        tokeniser.emit('t');
        tokeniser.emit('/');
        tokeniser.emit('s');
        tokeniser.emit('c');
        tokeniser.emit('r');
        tokeniser.emit('i');
        tokeniser.emit('p');
        tokeniser.emit('t');
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testReadWithComment() throws Exception {
        CharacterReader reader = new CharacterReader("--comment");
        Tokeniser tokeniser = new Tokeniser(reader);
        StringBuilder dataBuffer = new StringBuilder();
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        tokeniser.emit('c');
        tokeniser.emit('o');
        tokeniser.emit('m');
        tokeniser.emit('m');
        tokeniser.emit('e');
        tokeniser.emit('n');
        tokeniser.emit('t');
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.emit("--");
        tokeniser.emit(dataBuffer);
        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testReadWithDoctype() throws Exception {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        Tokeniser tokeniser = new Tokeniser(reader);
        StringBuilder dataBuffer = new StringBuilder();
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        tokeniser.emit('D');
        tokeniser.emit('O');
        tokeniser.emit('C');
        tokeniser.emit('T');
        tokeniser.emit('Y');
        tokeniser.emit('P');
        tokeniser.emit('E');
        tokeniser.emit(' ');
        tokeniser.emit('h');
        tokeniser.emit('t');
        tokeniser.emit('m');
        tokeniser.emit('l');
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.createTempBuffer();
        tokeniser.emit(dataBuffer);
        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testReadWithAppropriateEndTag() throws Exception {
        CharacterReader reader = new CharacterReader("<div><p><p></p></p></div>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token startTagDiv = tokeniser.read();
        Token startTagP1 = tokeniser.read();
        Token startTagP2 = tokeniser.read();
        Token endTagP1 = tokeniser.read();
        Token endTagP2 = tokeniser.read();
        Token endTagDiv = tokeniser.read();
        assertSame(startTagDiv.tagName, endTagDiv.tagName);
        assertEquals(10, tokeniser.errors.size());
    }

    @Test
    public void testReadWithInappropriateEndTag() throws Exception {
        CharacterReader reader = new CharacterReader("<div><p><p></p></head>");
        Tokeniser tokeniser = new Tokeniser(reader);
        Token startTagDiv = tokeniser.read();
        Token startTagP1 = tokeniser.read();
        Token startTagP2 = tokeniser.read();
        Token endTagHead = tokeniser.read();
        assertEquals(2, tokeniser.errors.size());
    }
}