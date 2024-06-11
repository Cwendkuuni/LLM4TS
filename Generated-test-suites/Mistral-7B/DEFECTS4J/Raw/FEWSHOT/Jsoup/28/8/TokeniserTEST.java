package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

    @Test
    public void testRead() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.matchConsume('A')).thenReturn(true);
        when(reader.matchConsume('B')).thenReturn(true);
        when(reader.matchConsume('C')).thenReturn(true);
        when(reader.matchConsume('D')).thenReturn(false);
        when(reader.matchConsume('\n')).thenReturn(true);
        when(reader.matchConsume(';')).thenReturn(true);
        when(reader.matchesAny(',')).thenReturn(false);
        when(reader.matchesAny('\t')).thenReturn(false);
        when(reader.matchesAny('\r')).thenReturn(false);
        when(reader.matchesAny('\f')).thenReturn(false);
        when(reader.matchesAny(' ')).thenReturn(false);
        when(reader.matchesAny('\u00A0')).thenReturn(false);
        when(reader.matchesLetter()).thenReturn(false);
        when(reader.matchesDigit()).thenReturn(false);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token);
        }

        assertEquals(10, errors.size());
        assertEquals("Unexpected character 'A' in input state Data", errors.get(0).getMessage());
        assertEquals("Unexpected character 'B' in input state Data", errors.get(1).getMessage());
        assertEquals("Unexpected character 'C' in input state Data", errors.get(2).getMessage());
        assertEquals("Unexpected character 'D' in input state Data", errors.get(3).getMessage());
        assertEquals("Unexpected character '\n' in input state Data", errors.get(4).getMessage());
        assertEquals("Unexpected character ',' in input state Data", errors.get(5).getMessage());
        assertEquals("Unexpected character '\t' in input state Data", errors.get(6).getMessage());
        assertEquals("Unexpected character '\r' in input state Data", errors.get(7).getMessage());
        assertEquals("Unexpected character '\f' in input state Data", errors.get(8).getMessage());
        assertEquals("Unexpected character ' ' in input state Data", errors.get(9).getMessage());

        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = new Character("A");
        Token startTag = new StartTag("DIV");
        Token endTag = new EndTag("DIV");

        tokeniser.emit(character);
        tokeniser.emit(startTag);
        tokeniser.emit(endTag);

        Token actualToken;
        while ((actualToken = tokeniser.read()) != null) {
            System.out.println(actualToken);
        }

        assertEquals(3, errors.size());
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
        assertEquals("Attributes incorrectly present on end tag", errors.get(1).getMessage());
        assertEquals("Attributes incorrectally present on start tag 'DIV'", errors.get(2).getMessage());
    }

    @Test
    public void testConsumeCharacterReference() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        when(reader.matchConsume('#')).thenReturn(true);
        when(reader.matchConsume('X')).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("0078");
        when(reader.consumeDigitSequence()).thenReturn("1234");
        when(reader.consumeLetterThenDigitSequence()).thenReturn("Ampersand");
        when(reader.matchConsume(';')).thenReturn(true);

        Character actualChar = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u0042', actualChar.getValue());

        actualChar = tokeniser.consumeCharacterReference('\u0026', false);
        assertEquals('\u0026', actualChar.getValue());

        actualChar = tokeniser.consumeCharacterReference("Ampersand", false);
        assertEquals('\u0026', actualChar.getValue());

        actualChar = tokeniser.consumeCharacterReference("#xDEADBEEF", false);
        assertEquals('\uDEAD\uBEEF', actualChar.getValue());

        actualChar = tokeniser.consumeCharacterReference("#xDEADBEEF", true);
        assertEquals('\uDFFD', actualChar.getValue());

        when(reader.matchConsume('#')).thenReturn(true);
        when(reader.consumeLetterThenDigitSequence()).thenReturn("amp");
        when(Entities.isNamedEntity("amp")).thenReturn(true);
        when(reader.matchConsume(';')).thenReturn(true);

        Character actualNamedChar = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', actualNamedChar.getValue());

        actualNamedChar = tokeniser.consumeCharacterReference("amp", false);
        assertEquals('&', actualNamedChar.getValue());

        actualNamedChar = tokeniser.consumeCharacterReference("AMPERSAND", false);
        assertEquals('&', actualNamedChar.getValue());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        Token.Tag tagStart = tokeniser.createTagPending(true);
        Token.Tag tagEnd = tokeniser.createTagPending(false);

        assertEquals("StartTag", tagStart.getClass().getSimpleName());
        assertEquals("EndTag", tagEnd.getClass().getSimpleName());
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = new StartTag("DIV");
        tokeniser.emitTagPending();

        Token actualToken;
        while ((actualToken = tokeniser.read()) != null) {
            System.out.println(actualToken);
        }

        assertEquals(1, errors.size());
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());

        tokeniser = new Tokeniser(reader, errors);
        startTag.selfClosing = true;
        tokeniser.emitTagPending();

        actualToken = tokeniser.read();
        assertEquals(StartTag.class, actualToken.getClass());
        assertEquals("DIV", actualToken.tagName);
        assertEquals(false, actualToken.selfClosing);

        tokeniser.acknowledgeSelfClosingFlag();

        actualToken = tokeniser.read();
        assertEquals(EndTag.class, actualToken.getClass());
        assertEquals("DIV", actualToken.tagName);
        assertEquals(true, actualToken.selfClosing);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        tokeniser.createCommentPending();

        Token actualToken = tokeniser.read();
        assertEquals(Comment.class, actualToken.getClass());
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token actualToken;
        while ((actualToken = tokeniser.read()) != null) {
            System.out.println(actualToken);
        }

        assertEquals(1, errors.size());
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        tokeniser.createDoctypePending();

        Token actualToken = tokeniser.read();
        assertEquals(Doctype.class, actualToken.getClass());
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token actualToken;
        while ((actualToken = tokeniser.read()) != null) {
            System.out.println(actualToken);
        }

        assertEquals(1, errors.size());
        assertEquals("Self closing flag not acknowledged", errors.get(0).getMessage());
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        tokeniser.createTempBuffer();

        StringBuilder buffer = tokeniser.dataBuffer;
        assertEquals(null, buffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        StartTag startTag1 = new StartTag("DIV");
        StartTag startTag2 = new StartTag("P");

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(startTag1);
        tokeniser.transition(TokeniserState.InTag);

        Token endTag = tokeniser.read();
        assertEquals(EndTag.class, endTag.getClass());
        assertEquals("DIV", endTag.tagName);

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(startTag2);
        tokeniser.transition(TokeniserState.InTag);

        assertNull(tokeniser.read());
    }
}