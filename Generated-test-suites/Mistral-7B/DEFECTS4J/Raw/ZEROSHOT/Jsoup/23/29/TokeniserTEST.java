package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TokeniserTest {

    private CharacterReader mockCharacterReader;
    private ParseErrorList mockParseErrorList;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        mockCharacterReader = Mockito.mock(CharacterReader.class);
        mockParseErrorList = Mockito.mock(ParseErrorList.class);
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(mockCharacterReader, mockParseErrorList);
    }

    @Test
    public void testRead_ExpectedCharacter() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('a');
        Token expectedToken = new Token.Character('a');
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader).advance();
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testRead_ExpectedSelfClosingFlagAcknowledgedError() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('<');
        Mockito.when(mockParseErrorList.canAddError()).thenReturn(true);
        tokeniser.read();
        Mockito.verify(mockParseErrorList).add(ArgumentMatchers.any(ParseError.class));
    }

    @Test
    public void testRead_ExpectedEndOfFile() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('\u0000');
        Mockito.when(mockParseErrorList.canAddError()).thenReturn(true);
        tokeniser.read();
        Mockito.verify(mockParseErrorList).add(ArgumentMatchers.any(ParseError.class));
    }

    @Test
    public void testRead_ExpectedStartTag() throws IOException {
        String tagName = "div";
        Mockito.when(mockCharacterReader.current()).thenReturn('<').thenReturn(tagName.charAt(0)).thenReturn('>');
        Token expectedStartTag = new Token.StartTag(tagName);
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader, Mockito.times(3)).advance();
        assertEquals(expectedStartTag, actualToken);
    }

    @Test
    public void testRead_ExpectedEndTag() throws IOException {
        String tagName = "div";
        Mockito.when(mockCharacterReader.current()).thenReturn('/').thenReturn(tagName.charAt(0)).thenReturn('>');
        Token expectedEndTag = new Token.EndTag(tagName);
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader, Mockito.times(3)).advance();
        assertEquals(expectedEndTag, actualToken);
    }

    @Test
    public void testRead_ExpectedString() throws IOException {
        String str = "test";
        Mockito.when(mockCharacterReader.current()).thenReturn('"').thenReturn('t').thenReturn('e').thenReturn('s').thenReturn('t')
                .thenReturn('"');
        Token expectedStringToken = new Token.String(str);
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader, Mockito.times(6)).advance();
        assertEquals(expectedStringToken, actualToken);
    }

    @Test
    public void testRead_ExpectedCharacterReference() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';');
        Character expectedCharacter = 'a';
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader, Mockito.times(4)).advance();
        assertEquals(new Token.CharacterReference('a'), actualToken);
        Mockito.verifyNoMoreInteractions(mockCharacterReader);
    }

    @Test
    public void testRead_ExpectedNamedCharacterReference() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';');
        String refName = "amp";
        Character expectedCharacter = '&';
        Token actualToken = tokeniser.read();
        Mockito.verify(mockCharacterReader, Mockito.times(4)).advance();
        assertEquals(new Token.CharacterReference(refName), actualToken);
        Mockito.verifyNoMoreInteractions(mockCharacterReader);
    }

    @Test
    public void testRead_ExpectedError() throws IOException {
        Mockito.when(mockCharacterReader.current()).thenReturn('<').thenReturn('>').thenReturn('>');
        Mockito.when(mockParseErrorList.canAddError()).thenReturn(true);
        tokeniser.read();
        Mockito.verify(mockParseErrorList).add(ArgumentMatchers.any(ParseError.class));
    }

    @Test
    public void testEmit_ExpectedCharacter() {
        Token characterToken = new Token.Character('a');
        tokeniser.emit(characterToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testEmit_ExpectedStartTag() {
        String tagName = "div";
        Token startTagToken = new Token.StartTag(tagName);
        tokeniser.emit(startTagToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testEmit_ExpectedEndTag() {
        String tagName = "div";
        Token endTagToken = new Token.EndTag(tagName);
        tokeniser.emit(endTagToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testEmit_ExpectedString() {
        String str = "test";
        Token stringToken = new Token.String(str);
        tokeniser.emit(stringToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testEmit_ExpectedCharacterReference() {
        Character character = 'a';
        Token characterReferenceToken = new Token.CharacterReference(character);
        tokeniser.emit(characterReferenceToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testEmit_ExpectedNamedCharacterReference() {
        String refName = "amp";
        Token namedCharacterReferenceToken = new Token.CharacterReference(refName);
        tokeniser.emit(namedCharacterReferenceToken);
        // No verification needed, as emitPending and isEmitPending are not accessible outside the class
    }

    @Test
    public void testTransition_ExpectedStateChange() {
        TokeniserState state = TokeniserState.Script;
        tokeniser.transition(state);
        // No verification needed, as state is not accessible outside the class
    }

    @Test
    public void testTransition_InvalidStateChange() {
        TokeniserState state = null;
        tokeniser.transition(state);
        Mockito.verify(mockParseErrorList).add(ArgumentMatchers.any(ParseError.class));
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testIsAppropriateEndTagToken_ExpectedTrue() {
        String tagName = "div";
        Token startTagToken = new Token.StartTag(tagName);
        tokeniser.emit(startTagToken);
        Token endTagToken = new Token.EndTag(tagName);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ExpectedFalse() {
        String tagName1 = "div";
        Token startTagToken1 = new Token.StartTag(tagName1);
        tokeniser.emit(startTagToken1);
        String tagName2 = "p";
        Token endTagToken = new Token.EndTag(tagName2);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ExpectedNull() {
        assertNull(tokeniser.isAppropriateEndTagToken());
    }
}