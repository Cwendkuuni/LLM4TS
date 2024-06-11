import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream("<html><head><script></script></head><body></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharReader(inputStream), new ParseErrorList());
        errors = tokeniser.errors;
    }

    @Test
    public void testRead_NormalCharacter() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("html", token.toString());
    }

    @Test
    public void testRead_EmptyCharacterBuffer() {
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();

        assertEquals(Token.TokenType.StartTag, tokeniser.read().type);
    }

    @Test(expected = AssertionError.class)
    public void testRead_UnreadTokenPending() {
        tokeniser.emit(new Token.Character("a"));
        tokeniser.read();
    }

    @Test
    public void testEmit_Character() {
        tokeniser.emit(new Token.Character("a"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.toString());
    }

    @Test
    public void testEmit_StartTag() {
        tokeniser.emit(new Token.StartTag("html"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.toString());
    }

    @Test
    public void testEmit_EndTag() {
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.EndTag("html"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.toString());
    }

    @Test
    public void testEmit_String() {
        tokeniser.emit("html");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("h", token.toString());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.toString());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("m", token.toString());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.toString());
    }

    @Test(expected = AssertionError.class)
    public void testEmit_TokenIsPending() {
        tokeniser.emit(new Token.Character("a"));
        tokeniser.emit(new Token.Character("b"));
        tokeniser.emit(new Token.Character("c"));
        tokeniser.emit(new Token.Character("d"));
        tokeniser.emit(new Token.StartTag("html"));
    }

    @Test
    public void testGetState_InitialState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition_ToDataState() {
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition_ToScriptState() {
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_ToScriptState() {
        tokeniser.advanceTransition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_ValidCharacterReference() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(character, '&');
    }

    @Test
    public void testConsumeCharacterReference_InvalidCharacterReference() {
        Character character = tokeniser.consumeCharacterReference('x', false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending_StartTag() {
        Token.StartTag tag = tokeniser.createTagPending(true);
        assertEquals("html", tag.tagName);
    }

    @Test
    public void testCreateTagPending_EndTag() {
        Token.EndTag tag = tokeniser.createTagPending(false);
        assertEquals("html", tag.tagName);
    }

    @Test
    public void testEmitTagPending_StartTag() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tagToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tagToken.type);
        assertEquals("html", tagToken.toString());
    }

    @Test
    public void testEmitTagPending_EndTag() {
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        Token tagToken = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tagToken.type);
        assertEquals("html", tagToken.toString());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.read().type);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token commentToken = tokeniser.read();
        assertEquals(Token.TokenType.Comment, commentToken.type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctypeToken = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctypeToken = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        tokeniser.emit("tempBuffer");
        tokeniser.emit("data");
        tokeniser.emit("buffer");
        tokeniser.emit("</data>");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("data", token.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken_MatchedEndTag() {
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_MismatchedEndTag() {
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.emit(new Token.StartTag("body"));
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testError_InvalidCharacterReference() {
        tokeniser.consumeCharacterReference("#", false);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference: invalid named referenece '%#'", error.getMessage());
    }

    @Test
    public void testError_UnexpectedCharacter() {
        tokeniser.reader.mark(2);
        tokeniser.reader.consume();
        tokeniser.reader.mark(2);
        tokeniser.reader.consume();
        tokeniser.error(TokeniserState.Data);
        assertEquals(2, errors.size());
        ParseError error = errors.get(1);
        assertEquals("Unexpected character 'x' in input state [Data]", error.getMessage());
    }

    @Test
    public void testError_UnexpectedEOF() {
        tokeniser.reader.mark(2);
        tokeniser.reader.consume();
        tokeniser.reader.mark(2);
        tokeniser.reader.consume();
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
    }
}