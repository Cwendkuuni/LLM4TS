package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.parser.TokeniserState.Data;

public class TokeniserTest {

    private ByteArrayInputStream inputStream;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    private Token expected;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream("test".getBytes());
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
    }

    @Test
    public void testRead_ReturnsTokenWhenCharacterIsConsumed() throws IOException {
        Token actual = tokeniser.read();
        expected = new Token.Character("t");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testRead_ReturnsNullWhenNoCharacterIsConsumed() throws IOException {
        inputStream.read(); // consume first character
        Token actual = tokeniser.read();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void testRead_ReturnsPendingTokenWhenCharacterIsPending() throws IOException {
        tokeniser.emit('t');
        tokeniser.emitPending();
        Token actual = tokeniser.read();
        expected = new Token.Character("t");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testRead_ReturnsPendingTokenWhenCharacterReferenceIsConsumed() throws IOException {
        inputStream.read(); // consume first character
        inputStream.mark(1);
        inputStream.write("#x27".getBytes());
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.acknowledgeSelfClosingFlag();
        inputStream.reset();
        Token actual = tokeniser.read();
        expected = new Token.Character("'");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test(expected = AssertionError.class)
    public void testRead_ThrowsAssertionErrorWhenCharacterReferenceIsNotConsumed() throws IOException {
        inputStream.read(); // consume first character
        inputStream.mark(1);
        inputStream.write("#x27".getBytes());
        tokeniser.consumeCharacterReference(null, false);
        Token actual = tokeniser.read();
        // assertion error should be thrown here
        inputStream.reset();
    }

    @Test
    public void testRead_ReturnsPendingStartTagTokenWhenTagIsConsumed() throws IOException {
        inputStream.read(); // consume <
        tokeniser.createTagPending(true);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.StartTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.StartTag));
        assertThat(actual.tagName, is(expected.tagName));
    }

    @Test
    public void testRead_ReturnsPendingEndTagTokenWhenEndTagIsConsumed() throws IOException {
        inputStream.read(); // consume <
        tokeniser.createTagPending(false);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.EndTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.EndTag));
        assertThat(actual.tagName, is(expected.tagName));
    }

    @Test
    public void testRead_ReturnsPendingEndTagTokenWhenSelfClosingTagIsConsumed() throws IOException {
        inputStream.read(); // consume <
        tokeniser.createTagPending(true);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.EndTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.EndTag));
        assertThat(actual.tagName, is(expected.tagName));
    }

    @Test
    public void testRead_ReturnsPendingEndTagTokenWhenAppropriateEndTagIsConsumed() throws IOException {
        inputStream.read(); // consume <
        tokeniser.createTagPending(true);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        inputStream.read(); // consume </
        tokeniser.createTagPending(false);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.EndTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.EndTag));
        assertThat(actual.tagName, is(expected.tagName));
    }

    @Test
    public void testRead_ReturnsErrorTokenWhenErrorIsEncountered() throws IOException {
        inputStream.read(); // consume <
        tokeniser.error(Data);
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.Error();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Error));
    }

    @Test
    public void testRead_ReturnsEOFTokenWhenEndOfFileIsReached() throws IOException {
        inputStream.read(); // consume first character
        inputStream.close();
        Token actual = tokeniser.read();
        expected = new Token.EOF();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.EOF));
    }

    @Test
    public void testEmit_EmitsToken() throws IOException {
        tokeniser.emit(new Token.Character("t"));
        Token actual = tokeniser.read();
        expected = new Token.Character("t");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testEmit_EmitsPendingTokenWhenCalled() throws IOException {
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.Character("t");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testEmit_DoesNotConsumeNextTokenWhenCalled() throws IOException {
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        inputStream.read(); // consume next character
        Token actual = tokeniser.read();
        expected = new Token.Character("e");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testEmitPendingTag_EmitsTagPendingToken() throws IOException {
        tokeniser.createTagPending(true);
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.StartTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.StartTag));
        assertThat(actual.tagName, is(expected.tagName));
    }

    @Test
    public void testEmitPendingComment_EmitsCommentPendingToken() throws IOException {
        tokeniser.createCommentPending();
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.Comment();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Comment));
    }

    @Test
    public void testEmitPendingDoctype_EmitsDoctypePendingToken() throws IOException {
        tokeniser.createDoctypePending();
        tokeniser.emitPending();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.Doctype();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Doctype));
    }

    @Test
    public void testTransition_TransitionsState() throws IOException {
        tokeniser.transition(Data);
        Token actual = tokeniser.read();
        expected = new Token.Character("t");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testTransition_TransitionsStateAndConsumesCharacter() throws IOException {
        tokeniser.transition(Data);
        inputStream.read(); // consume first character
        Token actual = tokeniser.read();
        expected = new Token.Character("e");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testAdvanceTransition_TransitionsStateAndAdvancesInput() throws IOException {
        tokeniser.advanceTransition(Data);
        inputStream.read(); // consume first character
        Token actual = tokeniser.read();
        expected = new Token.Character("e");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.Character));
        assertThat(actual.data, is(expected.data));
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_AcknowledgesFlag() throws IOException {
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.acknowledgeSelfClosingFlag();
        Token actual = tokeniser.read();
        expected = new Token.EndTag("tagName");
        assertThat(actual, is(notNullValue()));
        assertThat(actual.type, is(Token.TokenType.EndTag));
        assertThat(actual.tagName, is(expected.tagName));
    }
}