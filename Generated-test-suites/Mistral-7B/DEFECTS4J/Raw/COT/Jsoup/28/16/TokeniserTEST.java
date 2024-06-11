import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenizerTest;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.equalTo;
import static org.jsoup.parser.TokeniserState.AfterDoctype;
import static org.jsoup.parser.TokeniserState.AfterStartTag;
import static org.jsoup.parser.TokeniserState.BeforeDoctype;
import static org.jsoup.parser.TokeniserState.Data;

public class TokeniserTest extends TokenizerTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(new InputStreamReader(inputStream), errors);
 }

 @Test
 public void testRead_DataState_ReturnsCharacterToken() {
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 }

 @Test
 public void testRead_DataState_BuffersCharactersUntilTokenIsEmitted() {
 tokeniser.emit("test");
 tokeniser.read();
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 assertThat(token.content, is(equalTo("t")));
 }

 @Test
 public void testRead_DataState_EmitsTokenPendingWhenTokenIsEmitted() {
 tokeniser.emit("test");
 tokeniser.emit(" data");
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 assertThat(token.content, is(equalTo("d")));
 tokeniser.read();
 Token pendingToken = tokeniser.read();
 assertThat(pendingToken, is(instanceOf(Token.Character.class)));
 assertThat(pendingToken.content, is(equalTo("a")));
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_DataState_ErrorsWhenTokenIsPending() {
 tokeniser.emit("test");
 tokeniser.emit(" data");
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit(new Token.Character('a'));
 tokeniser.read();
 }

 @Test
 public void testEmit_CharacterToken() {
 char c = 't';
 tokeniser.emit(new Token.Character(c));
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 assertThat(token.content, is(equalTo(Character.toString(c))));
 }

 @Test
 public void testEmit_StartTagToken() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, false));
 Token token = tokeniser.read();
 Token startTagToken = (Token.StartTag) token;
 assertThat(startTagToken.tagName, is(equalTo(tagName)));
 }

 @Test
 public void testEmit_EndTagToken() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, true));
 tokeniser.emit(">");
 Token token = tokeniser.read();
 Token endTagToken = (Token.EndTag) token;
 assertThat(endTagToken.tagName, is(equalTo(tagName)));
 }

 @Test
 public void testRead_AfterStartTagState_ReturnsAppropriateEndTagToken() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, false));
 tokeniser.emit(">");
 tokeniser.read();
 Token token = tokeniser.read();
 Token endTagToken = (Token.EndTag) token;
 assertThat(endTagToken.tagName, is(equalTo(tagName)));
 }

 @Test
 public void testError_UnexpectedCharacter() {
 tokeniser.transition(Data);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.error(Data);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Unexpected character 'X' in input state [Data]")));
 }

 @Test
 public void testError_MissingSemicolon() {
 tokeniser.transition(Data);
 tokeniser.consumeCharacterReference("&amp;", false);
 tokeniser.consume(';');
 tokeniser.error(Data);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Missing semicolon")));
 }

 @Test
 public void testError_InvalidNamedReference() {
 tokeniser.transition(Data);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Invalid named reference ''")));
 }

 @Test
 public void testError_MissingSemicolonInAttribute() {
 tokeniser.transition(Data);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume('=');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Missing semicolon in attribute")));
 }

 @Test
 public void testTransition_DataStateToAfterDoctypeState() {
 tokeniser.transition(Data);
 tokeniser.transition(AfterDoctype);
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Doctype.class)));
 }

 @Test
 public void testTransition_DataStateToAfterStartTagState() {
 tokeniser.transition(Data);
 tokeniser.transition(AfterStartTag);
 Token token = tokeniser.read();
 Token startTagToken = (Token.StartTag) token;
 assertThat(startTagToken.selfClosing, is(false));
 }

 @Test
 public void testTransition_AfterStartTagStateToBeforeDoctypeState() {
 tokeniser.transition(AfterStartTag);
 tokeniser.transition(BeforeDoctype);
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 }

 @Test
 public void testTransition_AfterStartTagStateToAfterDoctypeState() {
 tokeniser.transition(AfterStartTag);
 tokeniser.transition(AfterDoctype);
 Token token = tokeniser.read();
 Token doctypeToken = (Token.Doctype) token;
 assertThat(doctypeToken.publicId, is(equalTo("test")));
 assertThat(doctypeToken.systemId, is(equalTo("data.html")));
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacterReference() {
 char c = 'a';
 Character referenceChar = new Character(c);
 Token token = tokeniser.read();
 Character character = tokeniser.consumeCharacterReference(referenceChar, false);
 assertThat(character, is(equalTo(referenceChar)));
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference() {
 tokeniser.transition(Data);
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertThat(character, is(nullValue()));
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReferenceInAttribute() {
 tokeniser.transition(Data);
 tokeniser.consumeLetterThenDigitSequence();
 Character character = tokeniser.consumeCharacterReference(null, true);
 assertThat(character, is(nullValue()));
 }

 @Test
 public void testConsumeCharacterReference_NamedCharacterReference() {
 tokeniser.transition(Data);
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 tokeniser.consumeLetterThenDigitSequence();
 tokeniser.consume(';');
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertThat(character, is(equalTo(new Character('™'))));
 }

 @Test
 public void testCreateTagPending_StartTag() {
 Token.StartTag tagPending = tokeniser.createTagPending(true);
 assertThat(tagPending.tagName, is(nullValue()));
 }

 @Test
 public void testCreateTagPending_EndTag() {
 Token.EndTag tagPending = tokeniser.createTagPending(false);
 assertThat(tagPending.tagName, is(nullValue()));
 }

 @Test
 public void testEmitTagPending_StartTag() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, false));
 tokeniser.emitTagPending();
 Token startTagToken = tokeniser.read();
 assertThat(startTagToken.tagName, is(equalTo(tagName)));
 }

 @Test
 public void testEmitTagPending_EndTag() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, true));
 tokeniser.emit(">");
 tokeniser.emitTagPending();
 Token endTagToken = tokeniser.read();
 assertThat(endTagToken.tagName, is(equalTo(tagName)));
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token doctypeToken = tokeniser.read();
 Token doctypePending = (Token.Doctype) doctypeToken;
 assertThat(doctypePending.publicId, is(nullValue()));
 assertThat(doctypePending.systemId, is(nullValue()));
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token doctypeToken = tokeniser.read();
 Token doctypePending = (Token.Doctype) doctypeToken;
 assertThat(doctypePending.publicId, is(equalTo("test")));
 assertThat(doctypePending.systemId, is(equalTo("data.html")));
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit("test");
 tokeniser.emit("data");
 tokeniser.emit(" <script>");
 tokeniser.emit("/script>");
 String data = tokeniser.read().content;
 assertThat(data, is(equalTo("testdata")));
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 String tagName = "p";
 tokeniser.emit(tagPending(tagName, false));
 tokeniser.emit(">");
 tokeniser.emit("<p>");
 tokeniser.emit("</p>");
 boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
 assertThat(isAppropriateEndTagToken, is(true));
 }

 @Test
 public void testIsAppropriateEndTagToken_WrongTagName() {
 String tagName1 = "p";
 String tagName2 = "div";
 tokeniser.emit(tagPending(tagName1, false));
 tokeniser.emit(">");
 tokeniser.emit("<" + tagName2 + ">");
 tokeniser.emit("</" + tagName1 + ">");
 boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
 assertThat(isAppropriateEndTagToken, is(false));
 }

 @Test
 public void testIsAppropriateEndTagToken_NoLastStartTag() {
 tokeniser.emit("test");
 boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
 assertThat(isAppropriateEndTagToken, is(false));
 }

 @Test
 public void testEmit_WhenInHtmlNS() {
 tokeniser.currentNodeInHtmlNS = true;
 char c = 'a';
 tokeniser.emit(new Token.Character(c));
 Token token = tokeniser.read();
 assertThat(token, is(instanceOf(Token.Character.class)));
 assertThat(token.content, is(equalTo(Character.toString(c))));
 }

 @Test
 public void testError_UnexpectedEndTag() {
 tokeniser.transition(Data);
 tokeniser.transition(Data);
 tokeniser.transition(AfterStartTag);
 tokeniser.transition(AfterStartTag);
 tokeniser.transition(BeforeDoctype);
 tokeniser.emitTagPending();
 tokeniser.emit('>');
 tokeniser.error(BeforeDoctype);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Unexpected end tag <p> in input state [BeforeDoctype]")));
 }

 @Test
 public void testError_UnexpectedEndOfFile() {
 tokeniser.transition(Data);
 tokeniser.transition(Data);
 tokeniser.transition(AfterStartTag);
 tokeniser.transition(Data);
 tokeniser.transition(BeforeDoctype);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.error(BeforeDoctype);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Unexpectedly reached end of file (EOF) in input state [BeforeDoctype]")));
 }

 @Test
 public void testCharacterReferenceError_InvalidCharacterReferenceMessage() {
 tokeniser.transition(Data);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.characterReferenceError("Error message");
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(not(equalTo("Error message"))));
 assertThat(errors.get(0).getMessage(), is(equalTo("Invalid character reference: Error message")));
 }

 @Test
 public void testCharacterReferenceError_ErrorMessageWithFormat() {
 tokeniser.transition(Data);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.characterReferenceError("Unexpected character '%s' in input state [%s]", 'X', Data);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Invalid character reference: Unexpected character 'X' in input state [Data]")));
 }

 @Test
 public void testError_ErrorMessageWithFormat() {
 tokeniser.transition(Data);
 tokeniser.error("Error message", Data);
 List<ParseError> errors = this.errors.getErrors();
 assertThat(errors, hasSize(1));
 assertThat(errors.get(0), isA(ParseError.class));
 assertThat(errors.get(0).getMessage(), is(equalTo("Error message")));
 }

}