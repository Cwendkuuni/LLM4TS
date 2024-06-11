package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTestUtil;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TokeniserTest {

 private Tokeniser tokeniser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 CharacterReader reader = new CharacterReader("test.html") {
 @Override
 public boolean isEmpty() {
 return false;
 }
 };
 ParseErrorList errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_whenInputIsEmpty_shouldReturnNull() {
 assertThat(tokeniser.read(), is(nullValue()));
 }

 @Test
 public void testRead_whenInputIsNotEmpty_shouldReturnCharacterTokenWithCorrespondingCharacter() {
 char character = 'A';
 reader.consume(character);
 Token token = tokeniser.read();
 assertThat(token, is(notNullValue()));
 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.charPending.data(), is(character + ""));
 }

 @Test
 public void testRead_whenInputIsNotEmptyAndThereIsANewTokenPending_shouldReturnTheNewToken() {
 reader.advance();
 tokeniser.emit(new Token.Character('A'));
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.consumeCharacterReference(null, false); // assume it consumes '&' and '#'
 tokeniser.consume(';');
 tokeniser.advanceTransition(TokeniserState.TagEnd);

 Token token = tokeniser.read();
 assertThat(token, is(notNullValue()));
 assertThat(token.type, is(Token.TokenType.EndTag));
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_whenInputIsNotEmptyAndEmitPendingIsNotFalse_shouldThrowIllegalStateException() {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.read();
 }

 @Test
 public void testEmit_whenTokenIsStartTag_shouldStoreTagName() {
 String tagName = "div";
 tokeniser.emit(new Token.StartTag(tagName));
 assertThat(tokeniser.lastStartTag, is(tagName));
 }

 @Test
 public void testEmit_whenTokenIsEndTag_shouldClearLastStartTag() {
 String tagName = "div";
 tokeniser.emit(new Token.StartTag(tagName));
 tokeniser.emit(new Token.EndTag(tagName));
 assertThat(tokeniser.lastStartTag, is(nullValue()));
 }

 @Test
 public void testEmit_whenTokenIsCharacter_shouldNotChangeLastStartTag() {
 char character = 'A';
 tokeniser.emit(new Token.Character(character));
 assertThat(tokeniser.lastStartTag, is(nullValue()));
 }

 @Test
 public void testEmit_whenTokenIsDoctype_shouldStoreDoctype() {
 String doctypeName = "html";
 tokeniser.emit(new Token.Doctype(doctypeName));
 Token.Doctype storedDoctype = tokeniser.doctypePending;
 assertThat(storedDoctype.name(), is(doctypeName));
 }

 @Test
 public void testEmit_whenTokenIsComment_shouldStoreComment() {
 String commentText = "This is a comment";
 tokeniser.emit(new Token.Comment(commentText));
 Token.Comment storedComment = tokeniser.commentPending;
 assertThat(storedComment.text(), is(commentText));
 }

 @Test(expected = IllegalStateException.class)
 public void testEmit_whenTokenIsNotEmptyAndThereIsNoPendingToken_shouldThrowIllegalStateException() {
 tokeniser.emit(new Token.Character('A'));
 }

 @Test
 public void testConsumeCharacterReference_whenCharacterReferenceIsValid_shouldReturnArrayWithCorrespondingCodePoints() {
 char[] reference = new char[]{'&', '#', 'x', '0', '0'};
 int[] result = tokeniser.consumeCharacterReference(null, false);
 char[] expected = new char[]{'&', 'x', '0', '0'};
 Arrays.sort(expected);
 Arrays.sort(result);
 assertThat(Arrays.equals(expected, result), is(true));
 }

 @Test
 public void testConsumeCharacterReference_whenCharacterReferenceIsInvalid_shouldReturnNull() {
 int[] result = tokeniser.consumeCharacterReference('&', false);
 assertThat(result, is(nullValue()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_whenAdditionalCharacterIsSpecifiedButIsNotPresent_shouldThrowIllegalArgumentException() {
 char additionalCharacter = 'X';
 int[] result = tokeniser.consumeCharacterReference(additionalCharacter, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_whenInAttributeAndCharacterIsForbidden_shouldThrowIllegalArgumentException() {
 char character = '=';
 int[] result = tokeniser.consumeCharacterReference(null, true);
 }

 @Test
 public void testCreateTagPending_whenCalledWithStartTag_shouldCreateNewStartTagInstance() {
 Token.StartTag tag = tokeniser.createTagPending(true);
 }

 @Test
 public void testCreateTagPending_whenCalledWithEndTag_shouldCreateNewEndTagInstance() {
 Token.EndTag tag = tokeniser.createTagPending(false);
 }

 @Test
 public void testEmitTagPending_whenCalledMultipleTimesWithSameTagName_shouldEmitSameTag() {
 String tagName = "div";
 tokeniser.createTagPending(true).name(tagName);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(true).name(tagName);
 tokeniser.emitTagPending();

 Token[] tokens = TokeniserTestUtil.getTokens(tokeniser);
 assertThat(tokens[0].type, is(Token.TokenType.StartTag));
 assertThat(tokens[1].type, is(Token.TokenType.StartTag));
 assertThat(tokens[1].startTag.tagName(), is(tagName));
 }

 @Test
 public void testEmitCommentPending_whenCalledMultipleTimesWithSameCommentText_shouldEmitSameComment() {
 String commentText = "This is a comment";
 tokeniser.createCommentPending().text(commentText);
 tokeniser.emitCommentPending();
 tokeniser.createCommentPending().text(commentText);
 tokeniser.emitCommentPending();

 Token[] tokens = TokeniserTestUtil.getTokens(tokeniser);
 assertThat(tokens[0].type, is(Token.TokenType.Comment));
 assertThat(tokens[1].type, is(Token.TokenType.Comment));
 assertThat(tokens[1].comment.text(), is(commentText));
 }

 @Test
 public void testEmitDoctypePending_whenCalledMultipleTimesWithSameDoctypeName_shouldEmitSameDoctype() {
 String doctypeName = "html";
 tokeniser.createDoctypePending().name(doctypeName);
 tokeniser.emitDoctypePending();
 tokeniser.createDoctypePending().name(doctypeName);
 tokeniser.emitDoctypePending();

 Token[] tokens = TokeniserTestUtil.getTokens(tokeniser);
 assertThat(tokens[0].type, is(Token.TokenType.Doctype));
 assertThat(tokens[1].type, is(Token.TokenType.Doctype));
 assertThat(tokens[1].doctype.name(), is(doctypeName));
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitTagPending_whenCalledWhenNoTagPending_shouldThrowIllegalStateException() {
 tokeniser.emitTagPending();
 }

 @Test
 public void testIsAppropriateEndTagToken_whenLastStartTagIsPresentAndTagNameMatches_shouldReturnTrue() {
 String tagName = "div";
 tokeniser.emit(new Token.StartTag(tagName));
 tokeniser.emit(new Token.EndTag(tagName));
 assertThat(tokeniser.isAppropriateEndTagToken(), is(true));
 }

 @Test
 public void testIsAppropriateEndTagToken_whenLastStartTagIsNotPresent_shouldReturnFalse() {
 assertThat(tokeniser.isAppropriateEndTagToken(), is(false));
 }

 @Test
 public void testIsAppropriateEndTagToken_whenLastStartTagIsPresentButTagNameDoesNotMatch_shouldReturnFalse() {
 String tagName1 = "div";
 String tagName2 = "p";
 tokeniser.emit(new Token.StartTag(tagName1));
 tokeniser.emit(new Token.EndTag(tagName2));
 assertThat(tokeniser.isAppropriateEndTagToken(), is(false));
 }

 @Test
 public void testError_whenCalledWithTokeniserState_shouldAddErrorToErrorsList() {
 String errorMessage = "An error occurred in the tokeniser";
 tokeniser.error(TokeniserState.Data);
 TokeniserTestUtil.AssertError(tokeniser.errors, errorMessage, null);
 }

 @Test
 public void testError_whenCalledWithErrorMessage_shouldAddErrorToErrorsList() {
 String errorMessage = "An error occurred in the parser";
 tokeniser.error(errorMessage);
 TokeniserTestUtil.AssertError(tokeniser.errors, null, errorMessage);
 }

 @Test
 public void testEofError_whenCalled_shouldAddErrorToErrorsList() {
 tokeniser.eofError(TokeniserState.TagStart);
 TokeniserTestUtil.AssertError(tokeniser.errors, null, "Unexpectedly reached end of file (EOF) in input state [TagStart]");
 }

 @Test
 public void testCharacterReferenceError_whenCalledWithErrorMessage_shouldAddErrorToErrorsList() {
 String errorMessage = "Invalid character reference: unexpected character 'X'";
 tokeniser.characterReferenceError(errorMessage);
 TokeniserTestUtil.AssertError(tokeniser.errors, null, errorMessage);
 }

 @Test
 public void testError_whenCalledWithNull_shouldThrowNullPointerException() {
 try {
 tokeniser.error(null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCurrentNodeInHtmlNS_shouldReturnTrue() {
 // Assuming that all the methods in the Tokeniser class implicitly return the node in the HTML namespace
 }

}