package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        tokeniser = new Tokeniser(reader);
        errors = new ArrayList<>();
        tokeniser.setTrackErrors(true);
    }

    @Test
    public void testReadNormalCharacters() {
        String input = "Hello World!";
        Mockito.when(reader.read()).thenReturn((char) 0); // read initial character
        for (char c : input.toCharArray()) {
            tokeniser.read();
            tokeniser.emit(new Token.Character(Character.toString(c)));
        }
        Mockito.verify(reader, Mockito.times(input.length() + 1)).read(); // one extra read for the final empty line
        assertEquals(input, tokeniser.charBuffer.toString());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadStartTag() {
        String input = "<p class='test'>Hello World!</p>";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('/').thenReturn('p').thenReturn(' ').thenReturn('>');
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("p", startTag.tagName);
        assertEquals(4, startTag.attributes.size());
        assertTrue(startTag.attributes.containsKey("class"));
        assertEquals("test", startTag.attributes.get("class").getValue());

        Mockito.verify(reader, Mockito.times(5)).read();
        assertEquals("p", tokeniser.lastStartTag.tagName);
    }

    @Test
    public void testReadEndTag() {
        String input = "<p>Hello World!</p>";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('/').thenReturn('p').thenReturn('>');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("p", endTag.tagName);

        Mockito.verify(reader, Mockito.times(3)).read();
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testReadSelfClosingTag() {
        String input = "<img src='image.png' />";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('/').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn(' ').thenReturn('>');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("img", startTag.tagName);
        assertEquals(1, startTag.attributes.size());
        assertTrue(startTag.attributes.containsKey("src"));
        assertEquals("image.png", startTag.attributes.get("src").getValue());
        assertFalse(startTag.selfClosing);

        Mockito.verify(reader, Mockito.times(6)).read();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testReadWhiteSpaces() {
        String input = " <tag name='test' class='testClass'> Test <script src=\"test.js\"></script> </tag > ";
        Mockito.when(reader.read()).thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn('<').thenReturn('t').thenReturn('a').thenReturn('g').thenReturn(' ').thenReturn('n').thenReturn('a').thenReturn('m').thenReturn('e').thenReturn('=').thenReturn('').thenReturn('"').thenReturn('t').thenReturn('e').thenReturn('s').thenReturn('t').thenReturn('C').thenReturn('l').thenReturn('a').thenReturn('s').thenReturn('s').thenReturn(' ').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn(' ').thenReturn('>').thenReturn(' ').thenReturn(' ').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('>').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn('>').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn(' ').thenReturn('/').thenReturn('t').thenReturn('a').thenReturn('g').thenReturn(' ').thenReturn('/').thenReturn(' ').thenReturn('>');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Mockito.verify(reader, Mockito.times(82)).read();
        assertEquals("tag", tokeniser.lastStartTag.tagName);
    }

    @Test
    public void testReadComment() {
        String input = "<!-- This is a comment -->";
        Mockito.when(reader.read()).thenReturn('--').thenReturn('>').thenReturn(' ').thenReturn('T').thenReturn('h').thenReturn('i').thenReturn('s').thenReturn(' ').thenReturn('i').thenReturn('s').thenReturn(' ').thenReturn('a').thenReturn(' ').thenReturn('c').thenReturn('o').thenReturn('m').thenReturn('m').thenReturn('e').thenReturn('n').thenReturn('t').thenReturn(' ').thenReturn('--');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals("This is a comment", comment.data);

        Mockito.verify(reader, Mockito.times(4)).read();
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testReadDoctype() {
        String input = "<!DOCTYPE html>";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('!').thenReturn('D').thenReturn('O').thenReturn('C').thenReturn('T').thenReturn('Y').thenReturn('P').thenReturn('E').thenReturn(' ').thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('l').thenReturn(' ').thenReturn('>');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals("html", doctype.data);

        Mockito.verify(reader, Mockito.times(9)).read();
        assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testErrorHandling() {
        String input = "<script>";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('>');
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.error(TokeniserState.SCRIPT_TAG);
        assertTrue(errors.size() > 0);
        assertEquals("Unexpected character in input", errors.get(0).getMessage());
        assertEquals(TokeniserState.SCRIPT_TAG.name(), errors.get(0).getState().name());
        assertEquals("script", errors.get(0).getTokenPosition());
        assertEquals(5, errors.get(0).getLineNumber());

        Mockito.verify(reader, Mockito.times(5)).read();
    }

    @Test
    public void testEofError() {
        String input = "<html>";
        Mockito.when(reader.read()).thenReturn('<').thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('l').thenReturn(' ').thenReturn('/').thenReturn('>').thenReturn('\u0000');

        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        tokeniser.eofError(TokeniserState.HTML);
        assertTrue(errors.size() > 0);
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
        assertEquals(TokeniserState.HTML.name(), errors.get(0).getState().name());
        assertEquals(5, errors.get(0).getLineNumber());

        Mockito.verify(reader, Mockito.times(5)).read();
    }
}