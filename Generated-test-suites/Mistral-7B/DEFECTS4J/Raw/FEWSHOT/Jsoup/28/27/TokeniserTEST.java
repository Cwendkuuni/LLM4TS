package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser.TokeniserState;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        CharacterReader reader = new CharacterReader("test <tag /> <script src=\"test.js\"></script> <tag attr=\"val\" /> <tag>foo</tag> <tag attr=\"val\" bar=\"baz\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token;
        for (int i = 0; i < 6; i++) {
            token = tokeniser.read();
            assertEquals(i == 0 ? Token.TokenType.StartTag : (i == 1 ? Token.TokenType.EndTag : (i == 2 ? Token.TokenType.Doctype : Token.TokenType.Comment)), token.type);
            if (token.type == Token.TokenType.StartTag || token.type == Token.TokenType.EndTag) {
                assertEquals("tag", token.tagName);
                if (token.type == Token.TokenType.StartTag) {
                    assertNull(token.attributes);
                }
            } else if (token.type == Token.TokenType.Comment) {
                assertEquals("", token.data);
            } else if (token.type == Token.TokenType.Doctype) {
                assertSame(token.doctype, Tokeniser.TokeniserState.Doctype);
            }
        }

        token = tokeniser.read();
        assertNull(token);

        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit(new Token.Character("t"));
        tokeniser.emit(new Token.Character("e"));
        tokeniser.emit(new Token.Character("s"));
        tokeniser.emit(new Token.Character("t"));

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", token.data);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\uD835\uDD10', character.intValue());

        character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);

        character = tokeniser.consumeCharacterReference("&foo;", false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertEquals(Tag.START_TAG, tagPending.tagType);
        assertNull(tagPending.tagName);
        assertNull(tagPending.attributes);

        tagPending = tokeniser.createTagPending(false);
        assertEquals(Tag.END_TAG, tagPending.tagType);
        assertNull(tagPending.tagName);
        assertNull(tagPending.attributes);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader reader = new CharacterReader("test <tag />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("tag", token.tagName);
        assertNull(token.attributes);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("--comment");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("", token.data);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader(" <!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.doctype);
    }

    @Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("test <tag error=\"invalid\">");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);

        tokeniser.error(TokeniserState.Data);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        assertEquals("Unexpected character 'e' in input state [DATA]", errors.get(0).getMessage());
    }

    @Test
    public void testEOFErrorHandling() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createTagPending(true);

        tokeniser.eofError(TokeniserState.Data);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [DATA]", errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceErrorHandling() {
        CharacterReader reader = new CharacterReader("&#x1F600;error");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.consumeCharacterReference(null, false);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: error", errors.get(0).getMessage());
    }
}