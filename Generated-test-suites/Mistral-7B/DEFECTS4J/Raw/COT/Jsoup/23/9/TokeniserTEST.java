import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private InputStream inputStream;

    @Before
    public void setup() {
        errors = new ArrayList<>();
        inputStream = new ByteArrayInputStream("<html><body><script>alert('Hello World');</script></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
    }

    @Test
    public void testRead_returnsExpectedToken() throws IOException {
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.Character("<");
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRead_throwsExceptionWhenCalledBeforeInitialization() {
        new Tokeniser(null, errors);
        tokeniser.read();
    }

    @Test
    public void testEmit_emitsExpectedToken() {
        tokeniser.emit(new Token.Character('H'));
        tokeniser.emit(new Token.Character('e'));
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('o'));
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.String("Hello");
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testEmit_doesNotEmitIfEmitPendingIsSet() {
        tokeniser.emit(new Token.Character('H'));
        tokeniser.emitPending = new Token.Character('e');
        tokeniser.isEmitPending = true;
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('o'));
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.String("Hello");
        Assert.assertEquals(expectedToken, actualToken);
        Assert.assertFalse(tokeniser.isEmitPending);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmit_throwsExceptionWhenCalledBeforeInitialization() {
        tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
        tokeniser.emit(new Token.Character('H'));
    }

    @Test
    public void testEmit_doesNotEmitIfStateIsData() {
        tokeniser.emit(new Token.Character('H'));
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit(new Token.Character('e'));
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('l'));
        tokeniser.emit(new Token.Character('o'));
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.String("Hello");
        Assert.assertEquals(expectedToken, actualToken);
        Assert.assertFalse(tokeniser.isEmitPending);
    }

    @Test
    public void testRead_returnsExpectedCharacterWhenConsumingCharacterReference() throws IOException {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('\u0026', null);
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.Character('&');
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testRead_returnsExpectedNamedCharacterReference() throws IOException {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, true);
        Token actualToken = tokeniser.read();
        Token expectedToken = Entities.getCharacterByName("#xamp;");
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test(expected = AssertionError.class)
    public void testRead_throwsExceptionWhenConsumingInvalidCharacterReference() throws IOException {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference('x', null);
        tokeniser.read();
    }

    @Test(expected = AssertionError.class)
    public void testRead_throwsExceptionWhenConsumingInvalidNamedCharacterReference() throws IOException {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, true);
        tokeniser.consumeCharacterReference("Invalid", true);
        tokeniser.read();
    }

    @Test(expected = AssertionError.class)
    public void testRead_throwsExceptionWhenConsumingCharacterReferenceOutsideAttribute() throws IOException {
        tokeniser.transition(TokeniserState.AfterTagName);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.read();
    }

    @Test
    public void testCreateTagPending_createsExpectedStartTag() {
        tokeniser.createTagPending(true);
        Token tagPending = tokeniser.tagPending;
        Token expectedTag = new Token.StartTag("div");
        Assert.assertEquals(expectedTag, tagPending);
    }

    @Test
    public void testCreateTagPending_createsExpectedEndTag() {
        tokeniser.createTagPending(false);
        Token tagPending = tokeniser.tagPending;
        Token expectedTag = new Token.EndTag("div");
        Assert.assertEquals(expectedTag, tagPending);
    }

    @Test
    public void testEmitTagPending_emitsExpectedStartTag() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.StartTag("div");
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testEmitTagPending_emitsExpectedEndTag() {
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        Token actualToken = tokeniser.read();
        Token expectedToken = new Token.EndTag("div");
        Assert.assertEquals(expectedToken, actualToken);
    }

    @Test(expected = AssertionError.class)
    public void testRead_throwsExceptionWhenTagPendingIsNotStartTag() {
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.read();
    }

    @Test
    public void testIsAppropriateEndTagToken_returnsTrueWhenIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        boolean actualResult = tokeniser.isAppropriateEndTagToken();
        boolean expectedResult = true;
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsAppropriateEndTagToken_returnsFalseWhenIsNotAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.transition(TokeniserState.EndTag);
        boolean actualResult = tokeniser.isAppropriateEndTagToken();
        boolean expectedResult = false;
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testError_addsExpectedErrorMessage() throws IOException {
        tokeniser.error(TokeniserState.Data);
        ParseError expectedError = new ParseError(1, "Unexpected character 'H' in input state [Data]");
        ParseError actualError = errors.get(0);
        Assert.assertEquals(expectedError, actualError);
    }

    @Test
    public void testEofError_addsExpectedErrorMessage() throws IOException {
        tokeniser.eofError(TokeniserState.Data);
        ParseError expectedError = new ParseError(1, "Unexpectedly reached end of file (EOF) in input state [Data]");
        ParseError actualError = errors.get(0);
        Assert.assertEquals(expectedError, actualError);
    }

    @Test
    public void testCharacterReferenceError_addsExpectedErrorMessage() throws IOException {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.characterReferenceError("Invalid named referenece 'invalid'");
        ParseError expectedError = new ParseError(1, "Invalid character reference: Invalid named referenece 'invalid'");
        ParseError actualError = errors.get(0);
        Assert.assertEquals(expectedError, actualError);
    }

    @Test
    public void testError_doesNotAddDuplicateErrorMessage() throws IOException {
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        int expectedErrorCount = 1;
        int actualErrorCount = errors.size();
        Assert.assertEquals(expectedErrorCount, actualErrorCount);
    }
}