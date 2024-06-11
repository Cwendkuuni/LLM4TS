package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Test Page</title>"
                + "</head>"
                + "<body>"
                + "<h1>Test Heading</h1>"
                + "<p>Test Paragraph</p>"
                + "</body>"
                + "</html>");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token startTag = new Token.StartTag("html", new ArrayList<>());
        tokeniser.emit(startTag);
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(startTag, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "Test String";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(str, ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(String.valueOf(c), ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEParseErrorInEmit() {
        tokeniser.emit("Test Error");
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals("Test Error", ((Token.Character) emittedToken).getData());
        List<ParseError> errors = tokeniser.errors.getAll();
        assertEquals(1, errors.size());
        assertEquals("Test Error", errors.get(0).getErrorMessage());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character charRef = tokeniser.consumeCharacterReference(null, true);
        assertNull(charRef);

        tokeniser.reader.advance();
        charRef = tokeniser.consumeCharacterReference(null, true);
        assertEquals('!', charRef);

        tokeniser.reader.mark();
        charRef = tokeniser.consumeCharacterReference('&', true);
        assertNull(charRef);
        tokeniser.reader.unconsume();

        charRef = tokeniser.consumeCharacterReference('&', true);
        assertEquals('!', charRef);

        charRef = tokeniser.consumeCharacterReference('?', true);
        assertNull(charRef);

        charRef = tokeniser.consumeCharacterReference('-', true);
        assertNull(charRef);

        charRef = tokeniser.consumeCharacterReference('_', true);
        assertNull(charRef);

        charRef = tokeniser.consumeCharacterReference('=', true);
        assertNull(charRef);

        charRef = tokeniser.consumeCharacterReference(';', true);
        assertNull(charRef);

        tokeniser.reader.advance();
        charRef = tokeniser.consumeCharacterReference(';', true);
        assertEquals('!', charRef);

        tokeniser.reader.advance();
        charRef = tokeniser.consumeCharacterReference(';', true);
        assertEquals(Entities.getCharacterByName("DOCTYPE HTML"), charRef);

        tokeniser.reader.advance();
        charRef = tokeniser.consumeCharacterReference(';', true);
        assertEquals(Entities.getCharacterByName("TITLE"), charRef);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag startTag = tokeniser.createTagPending(true);
        assertNotNull(startTag);
        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Comment, emittedToken.type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token emittedToken = tokeniser.read();
        assertNotNull(emittedToken);
        assertEquals(Token.TokenType.Doctype, emittedToken.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.Tag startTag = new Token.StartTag("h1", new ArrayList<>());
        tokeniser.lastStartTag = startTag;

        Token.Tag endTag = tokeniser.createTagPending(false);
        endTag.tagName = "h1";
        tokeniser.tagPending = endTag;

        boolean isApprEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertEquals(true, isApprEndTagToken);

        Token.Tag nonApprEndTag = new Token.Tag();
        nonApprEndTag.tagName = "h2";
        tokeniser.tagPending = nonApprEndTag;

        isApprEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertEquals(false, isApprEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.Tag startTag = new Token.StartTag("h1", new ArrayList<>());
        tokeniser.lastStartTag = startTag;

        String apprEndTagName = tokeniser.appropriateEndTagName();
        assertEquals(startTag.tagName, apprEndTagName);
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = tokeniser.errors.getAll();
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '<' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = tokeniser.errors.getAll();
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Test Error");
        List<ParseError> errors = tokeniser.errors.getAll();
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: Test Error", errors.get(0).getErrorMessage());
    }

    @Test
    public void testError_String() {
        tokeniser.error("Test Error");
        List<ParseError> errors = tokeniser.errors.getAll();
        assertEquals(1, errors.size());
        assertEquals("Test Error", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertEquals(true, inHtmlNS);
    }


}