import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TokeniserTest {

 private CharacterReader mockReader;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockReader = mock(CharacterReader.class);
 tokeniser = new Tokeniser(mockReader);
 }

 @Test
 public void testRead_ReturnsCharacterToken() {
 when(mockReader.current()).thenReturn('a');
 when(mockReader.read()).thenReturn(true);

 Token actual = tokeniser.read();
 Token expected = new Token.Character("a");

 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_ThrowsExceptionWhenSelfClosingFlagNotAcknowledged() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit(new Token.Character('a'));
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read(); // should acknowledge flag before reading
 }

 @Test
 public void testRead_ReturnsStartTagToken() {
 when(mockReader.current()).thenReturn('<');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('h');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('t').thenReturn(true);
 when(mockReader.current()).thenReturn('/').thenReturn(true);
 when(mockReader.current()).thenReturn('h').thenReturn(true);
 when(mockReader.current()).thenReturn('>').thenReturn(true);

 Token actual = tokeniser.read();
 Token expected = new Token.StartTag("html");

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testRead_ReturnsEndTagToken() {
 when(mockReader.current()).thenReturn('<').thenReturn(true);
 when(mockReader.current()).thenReturn('/').thenReturn(true);
 when(mockReader.current()).thenReturn('h').thenReturn(true);
 when(mockReader.current()).thenReturn('>').thenReturn(true);

 Token actual = tokeniser.read();
 Token expected = new Token.EndTag("html");

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmit_EmitsToken() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.emit(new Token.Character('a'));

 Token actual = tokeniser.read();
 Token expected = new Token.Character("a");

 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmit_ThrowsExceptionWhenEmitPending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('b'));

 tokeniser.read(); // should not be able to read yet
 }

 @Test
 public void testEmit_EmitsStartTag() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("html"));

 Token actual = tokeniser.read();
 Token expected = new Token.Character("<");

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmit_EmitsEndTag() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit(new Token.EndTag("html"));

 Token actual = tokeniser.read();
 Token expected = new Token.Character(">");

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmit_EmitsString() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit("hello");

 String actual = tokeniser.read().toString();
 String expected = "hello";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmit_EmitsCharacter() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.read();
 tokeniser.read();
 tokeniser.emit('a');

 char actual = tokeniser.read().charAt(0);
 char expected = 'a';

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetState_ReturnsCurrentState() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.transition(TokeniserState.Data);

 TokeniserState actual = tokeniser.getState();
 TokeniserState expected = TokeniserState.Data;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testTransition_SetsState() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.transition(TokeniserState.StartTag);

 TokeniserState actual = tokeniser.getState();
 TokeniserState expected = TokeniserState.StartTag;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag_SetsFlag() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.acknowledgeSelfClosingFlag();

 boolean actual = tokeniser.selfClosingFlagAcknowledged();
 boolean expected = true;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCharacterReference_ReturnsCharacter() {
 tokeniser = new Tokeniser(mockReader);
 when(mockReader.current()).thenReturn('&');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('#');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('x').thenReturn(true);
 when(mockReader.current()).thenReturn(';').thenReturn(true);

 Character actual = tokeniser.consumeCharacterReference(null, false);
 Character expected = 'x';

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeCharacterReference_ReturnsReplacementCharacter() {
 tokeniser = new Tokeniser(mockReader);
 when(mockReader.current()).thenReturn('&');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('#');
 when(mockReader.current()).thenReturn('X').thenReturn(true);
 when(mockReader.current()).thenReturn(';').thenReturn(true);

 Character actual = tokeniser.consumeCharacterReference(null, false);
 Character expected = '\uFFFD';

 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_ThrowsExceptionWhenNoCharacterFound() {
 tokeniser = new Tokeniser(mockReader);
 when(mockReader.current()).thenReturn('&');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('>').thenReturn(true);

 tokeniser.consumeCharacterReference(null, false);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_ThrowsExceptionWhenInAttribute() {
 tokeniser = new Tokeniser(mockReader);
 when(mockReader.current()).thenReturn('&');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('a').thenReturn(true);
 when(mockReader.current()).thenReturn(' ').thenReturn(true);
 when(mockReader.current()).thenReturn('=');
 when(mockReader.read()).thenReturn(true);
 when(mockReader.current()).thenReturn('>').thenReturn(true);

 tokeniser.consumeCharacterReference(null, true);
 }

 @Test
 public void testCreateTagPending_CreatesTagPending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createTagPending(true);

 Token.StartTag actual = tokeniser.tagPending;
 Token.StartTag expected = new Token.StartTag();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmitTagPending_EmitsTagPending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Token actual = tokeniser.read();
 Token expected = new Token.StartTag();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCreateCommentPending_CreatesCommentPending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createCommentPending();

 Token.Comment actual = tokeniser.commentPending;
 Token.Comment expected = new Token.Comment();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmitCommentPending_EmitsCommentPending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();

 Token actual = tokeniser.read();
 Token expected = new Token.Comment();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCreateDoctypePending_CreatesDoctypePending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createDoctypePending();

 Token.Doctype actual = tokeniser.doctypePending;
 Token.Doctype expected = new Token.Doctype();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEmitDoctypePending_EmitsDoctypePending() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();

 Token actual = tokeniser.read();
 Token expected = new Token.Doctype();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCreateTempBuffer_CreatesTempBuffer() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createTempBuffer();

 StringBuilder actual = tokeniser.dataBuffer;
 StringBuilder expected = new StringBuilder();

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsTrueWhenAppropriateEndTag() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createTagPending(true).tagName = "html";
 tokeniser.createTagPending(false).tagName = "head";

 boolean actual = tokeniser.isAppropriateEndTagToken();
 boolean expected = true;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsFalseWhenInappropriateEndTag() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.createTagPending(true).tagName = "html";
 tokeniser.createTagPending(false).tagName = "body";

 boolean actual = tokeniser.isAppropriateEndTagToken();
 boolean expected = false;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsTrackErrors_ReturnsTrueWhenTrackingErrors() {
 tokeniser = new Tokeniser(mockReader);

 boolean actual = tokeniser.isTrackErrors();
 boolean expected = true;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsTrackErrors_ReturnsFalseWhenNotTrackingErrors() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.setTrackErrors(false);

 boolean actual = tokeniser.isTrackErrors();
 boolean expected = false;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testError_AddsErrorToErrorsList() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.error(TokeniserState.Data);

 ParseError actual = tokeniser.errors.get(0);
 ParseError expected = new ParseError("Unexpected character in input", 0, TokeniserState.Data, 0);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEofError_AddsErrorToErrorsList() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.eofError(TokeniserState.Data);

 ParseError actual = tokeniser.errors.get(0);
 ParseError expected = new ParseError("Unexpectedly reached end of file (EOF)", TokeniserState.Data, 0);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCharacterReferenceError_AddsErrorToErrorsList() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.characterReferenceError();

 ParseError actual = tokeniser.errors.get(0);
 ParseError expected = new ParseError("Invalid character reference", 0);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testError_ThrowsExceptionWhenTrackingErrorsOff() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.setTrackErrors(false);
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testError_ThrowsExceptionWhenTrackingErrorsOffAndNoErrorsList() {
 tokeniser = new Tokeniser(mockReader);
 tokeniser.setTrackErrors(false);
 tokeniser.errors = null;
 tokeniser.error(TokeniserState.Data);
 }
}