package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.AttributeValue;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.TokeniserState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testRead() throws IOException {
        CharacterReader readerMock = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(readerMock, errors);

        when(readerMock.peek()).thenReturn('a');
        when(readerMock.consume('a')).thenReturn(true);
        when(readerMock.consume('b')).thenReturn(true);
        when(readerMock.consume('c')).thenReturn(true);
        when(readerMock.consume('#')).thenReturn(true);
        when(readerMock.consume('x')).thenReturn(true);
        when(readerMock.consume('X')).thenReturn(true);
        when(readerMock.consumeLetter()).thenReturn('T');
        when(readerMock.consumeDigit()).thenReturn('1');
        when(readerMock.consumeLetter()).thenReturn('e');
        when(readerMock.consumeLetter()).thenReturn('m');
        when(readerMock.consume(';')).thenReturn(true);
        when(readerMock.matchConsume('#')).thenReturn(true);
        when(readerMock.matchConsume(';')).thenReturn(true);
        when(readerMock.matchesLetter()).thenReturn(true);
        when(readerMock.matchesDigit()).thenReturn(true);
        when(readerMock.matchesAny(Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, ' ', '<', '&')).thenReturn(true);
        when(readerMock.isEmpty()).thenReturn(false);
        when(readerMock.current()).thenReturn('a');

        Token token;
        for (int i = 0; i < 7; i++) {
            token = tokeniser.read();
            assertEquals(Token.TokenType.Character, token.type);
            assertEquals("a", token.toString());
        }

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        Token.StartTag startTag = (Token.StartTag) token;
        assertEquals("T", startTag.tagName);
        assertEquals(null, startTag.attributes);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        Token.EndTag endTag = (Token.EndTag) token;
        assertEquals("T", endTag.tagName);
        assertNull(endTag.attributes);

        tokeniser.emit(new Token.Character("b"));
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.toString());

        tokeniser.transition(TokeniserState.Comment);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        Comment comment = (Comment) token;
        assertEquals("--", comment.data().substring(0, 2));
        assertEquals("", comment.data().substring(2));

        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference(Character.MIN_VALUE, true); // invalid character reference with null character
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(Character.MIN_VALUE, token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference('\u0001', false); // valid character reference
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('\u0001', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("ampersand", false); // invalid named character reference
        token = tokeniser.read();
        assertNull(token);

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit("&amp;");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference(null, true); // null character reference
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(Tokeniser.replacementChar, token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit("&lt;");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("lt", false); // valid named character reference
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('<', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit("&gt;");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("gt", false); // valid named character reference
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('>', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("quot", false); // valid named character reference
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('"', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x24", false); // valid character reference with hex code
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('$', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x26", false); // valid character reference with hex code
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('&', token.toCharacter());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x3C", false); // valid character reference with hex code
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x3E", false); // valid character reference with hex code
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        startTag = (Token.StartTag) token;
        assertEquals("html", startTag.tagName);
        List<Attribute> attributes = startTag.attributes();
        List<AttributeValue> attributeValues = new ArrayList<>();
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setValue("lang");
        attributeValue.setValue("en");
        attributes.add(new Attribute("xml:lang", attributeValue));
        assertEquals(attributes, startTag.attributes());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit("<!-- This is a test comment -->");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x3C", false); // valid character reference with hex code
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitCharacterReference("#x3E", false); // valid character reference with hex code
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(" <!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitStartTag("html");
        tokeniser.transition(TokeniserState.EmptyElement);
        tokeniser.emit(" <head>");
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitStartTag("meta");
        tokeniser.emitAttribute("name", "description");
        tokeniser.emitAttribute("content", "This is a test description");
        tokeniser.emitEndTag(); // meta
        tokeniser.emitEndTag(); // head
        tokeniser.emitStartTag("body");
        tokeniser.emitEndTag(); // body
        tokeniser.emitEndTag(); // html

        List<Token> tokens = new ArrayList<>();
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        assertEquals(14, tokens.size());
        assertEquals("a", tokens.get(0).toString());
        assertEquals("T", tokens.get(1).toString());
        assertEquals("--", tokens.get(2).toString());
        assertEquals("", tokens.get(3).toString());
        assertEquals("", tokens.get(4).toString());
        assertEquals("", tokens.get(5).toString());
        assertEquals("", tokens.get(6).toString());
        assertEquals("", tokens.get(7).toString());
        assertEquals("html", tokens.get(8).toString());
        assertEquals("<html", tokens.get(9).toString());
        assertEquals(" xml:lang=\"en\"", tokens.get(10).toString());
        assertEquals("<!-- This is a test comment -->", tokens.get(11).toString());
        assertEquals("", tokens.get(12).toString());
        assertEquals(" <!DOCTYPE html SYSTEM \"about:legacy-compat\">", tokens.get(13).toString());

        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        ParseError parseError = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseError.getMessage());
        assertEquals(0, errors.get(0).getLineNumber());

        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        parseError = errors.get(0);
        assertEquals("Invalid character reference: invalid character reference with null character", parseError.getMessage());
        assertEquals(0, errors.get(0).getLineNumber());

        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        parseError = errors.get(0);
        assertEquals("Invalid named referenece '%quot''", parseError.getMessage());
        assertEquals(0, errors.get(0).getLineNumber());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        Tag tagPending = tokeniser.createTagPending(true);
        assertSame(Token.StartTag.class, tagPending.getClass());
        tagPending = tokeniser.createTagPending(false);
        assertSame(Token.EndTag.class, tagPending.getClass());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        assertEquals("", tokeniser.lastStartTag.tagName);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitStartTag("div");
        tokeniser.transition(TokeniserState.Data);
        tagPending = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitStartTag("div");
        tokeniser.transition(TokeniserState.Data);
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
        tokeniser.transition(TokeniserState.Data);
        tagPending = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Data);
        assertEquals(false, tokeniser.isAppropriateEndTagToken());
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitStartTag("p");
        tokeniser.transition(TokeniserState.Data);
        assertEquals(false, tokeniser.isAppropriateEndTagToken());
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emitStartTag("div");
        tokeniser.transition(TokeniserState.Data);
        assertEquals(true, tokeniser.isAppropriateEndTagToken());
    }
}