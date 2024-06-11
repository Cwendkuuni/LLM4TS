import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private List<Token> expectedTokens;

 @Before
 public void setUp() {
 tokeniser = new Tokeniser(new CharacterReader(getTestInputStream()), new ParseErrorList());
 errors = tokeniser.errors;
 expectedTokens = new ArrayList<>();
 }

 @Test
 public void testRead_EmptyInputStream() {
 tokeniser.read();
 Assert.assertFalse(errors.hasErrors());
 Assert.assertTrue(errors.isEmpty());
 }

 @Test
 public void testRead_SingleCharacter() {
 given(tokeniser, "a").whenReading().expectCharacter('a');
 }

 @Test
 public void testRead_MultipleCharacters() {
 given(tokeniser, "hello").whenReading().expectCharacters("hello");
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser.selfClosingFlagAcknowledged = false;
 tokeniser.read();
 }

 @Test
 public void testEmit_Character() {
 given(tokeniser, "a").whenReading().thenEmitCharacter('a');
 }

 @Test
 public void testEmit_StartTag() {
 given(tokeniser, "<div>").whenReading().thenEmitTag(new StartTag("div"));
 }

 @Test
 public void testEmit_EndTag() {
 given(tokeniser, "</div>").whenReading().thenEmitTag(new EndTag("div"));
 }

 @Test
 public void testEmit_Comment() {
 given(tokeniser, "<!--comment-->").whenReading().thenEmitComment(new Comment("comment"));
 }

 @Test
 public void testEmit_Doctype() {
 given(tokeniser, "<!DOCTYPE html>").whenReading().thenEmitDoctype();
 }

 @Test
 public void testConsumeCharacterReference_Numeric() {
 given(tokeniser, "&#123;").whenConsumingCharacterReference().expectCharacter((char) 123);
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 given(tokeniser, "&amp;").whenConsumingCharacterReference().expectCharacter('&');
 }

 @Test
 public void testConsumeCharacterReference_InvalidNumeric() {
 given(tokeniser, "&#xABCDEF;").whenConsumingCharacterReference().expectCharacter(Tokeniser.replacementChar);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamed() {
 given(tokeniser, "&xyz;").whenConsumingCharacterReference().expectNull();
 }

 @Test
 public void testCreateTagPending_StartTag() {
 given(tokeniser).whenCreatingTagPending(true).expectTagPending(new StartTag(""));
 }

 @Test
 public void testCreateTagPending_EndTag() {
 given(tokeniser).whenCreatingTagPending(false).expectTagPending(new EndTag(""));
 }

 @Test
 public void testCreateCommentPending() {
 given(tokeniser).whenCreatingCommentPending().expectCommentPending(new Comment(""));
 }

 @Test
 public void testCreateDoctypePending() {
 given(tokeniser).whenCreatingDoctypePending().expectDoctypePending(new Doctype(""));
 }

 @Test
 public void testTokeniserError_UnexpectedCharacter() {
 given(tokeniser, "!")
 .whenReading()
 .expectError(ParseError.class, "Unexpected character '!' in state [Data]");
 }

 @Test
 public void testTokeniserError_UnexpectedEndOfFile() {
 given(tokeniser)
 .whenReading()
 .expectError(ParseError.class, "Unexpectedly reached end of file (EOF) in state [Data]");
 }

 @Test
 public void testTokeniserError_InvalidCharacterReference() {
 given(tokeniser, "&#invalid;")
 .whenConsumingCharacterReference()
 .expectError(ParseError.class, "Invalid character reference: invalid");
 }

 @Test
 public void testTokeniserError_InvalidNamedCharacterReference() {
 given(tokeniser, "&xyz;")
 .whenConsumingCharacterReference()
 .expectError(ParseError.class, "Invalid named character reference: xyz");
 }

 @Test
 public void testIsAppropriateEndTagToken_MatchingEndTag() {
 given(tokeniser, "<div>")
 .given(tokeniser, "</div>")
 .whenReading()
 .thenEmitTag(new StartTag("div"))
 .thenReading()
 .expectTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_MismatchingEndTag() {
 given(tokeniser, "<div>")
 .given(tokeniser, "</p>")
 .whenReading()
 .thenEmitTag(new StartTag("div"))
 .thenReading()
 .expectFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testGetState_Data() {
 given(tokeniser)
 .whenGettingState()
 .expect(TokeniserState.Data.class);
 }

 @Test
 public void testGetState_StartTag() {
 given(tokeniser, "<div>")
 .whenReading()
 .thenEmitTag(new StartTag("div"))
 .whenGettingState()
 .expect(TokeniserState.StartTag.class);
 }

 @Test
 public void testGetState_EndTag() {
 given(tokeniser, "</div>")
 .whenReading()
 .thenEmitTag(new EndTag("div"))
 .whenGettingState()
 .expect(TokeniserState.EndTag.class);
 }

 @Test
 public void testGetState_Comment() {
 given(tokeniser, "<!--comment-->")
 .whenReading()
 .thenEmitComment(new Comment("comment"))
 .whenGettingState()
 .expect(TokeniserState.Comment.class);
 }

 @Test
 public void testGetState_Doctype() {
 given(tokeniser, "<!DOCTYPE html>")
 .whenReading()
 .thenEmitDoctype()
 .whenGettingState()
 .expect(TokeniserState.Doctype.class);
 }

 private void given(Tokeniser tokeniser, String input) {
 given(tokeniser, new ByteArrayInputStream(input.getBytes(Locale.ENGLISH)));
 }

 private void whenReading() {
 Token token = tokeniser.read();
 expectedTokens.add(token);
 }

 private void thenEmitCharacter(char expectedChar) {
 char actualChar = tokeniser.read().character.toString().charAt(0);
 Assert.assertEquals(expectedChar, actualChar);
 }

 private void thenEmitTag(Token expectedTag) {
 Token actualTag = tokeniser.read();
 Assert.assertEquals(expectedTag.getClass(), actualTag.getClass());
 if (expectedTag instanceof StartTag) {
 StartTag expectedStartTag = (StartTag) expectedTag;
 StartTag actualStartTag = (StartTag) actualTag;
 Assert.assertEquals(expectedStartTag.tagName, actualStartTag.tagName);
 Assert.assertArrayEquals(expectedStartTag.attributes.getNames(), actualStartTag.attributes.getNames());
 } else if (expectedTag instanceof EndTag) {
 EndTag expectedEndTag = (EndTag) expectedTag;
 EndTag actualEndTag = (EndTag) actualTag;
 Assert.assertEquals(expectedEndTag.tagName, actualEndTag.tagName);
 }
 }

 private void thenEmitComment(Comment expectedComment) {
 Comment actualComment = tokeniser.read();
 Assert.assertEquals(expectedComment.data, actualComment.data);
 }

 private void thenEmitDoctype() {
 Doctype actualDoctype = tokeniser.read();
 Assert.assertEquals(expectedTokens.size(), tokeniser.errors.size()+1);
 }

 private void thenConsumingCharacterReference() {
 Character expectedCharacter = null;
 Character actualCharacter = null;
 if (tokeniser.consumeCharacterReference(null, false) != null) {
 expectedCharacter = tokeniser.read().character;
 actualCharacter = tokeniser.read().character;
 }
 Assert.assertEquals(expectedCharacter, actualCharacter);
 }

 private void thenCreatingTagPending(boolean isStartTag) {
 Token expectedTag = null;
 if (isStartTag) {
 expectedTag = new StartTag("");
 } else {
 expectedTag = new EndTag("");
 }
 tokeniser.createTagPending(isStartTag);
 Assert.assertEquals(expectedTokens.size(), tokeniser.errors.size()+1);
 Assert.assertEquals(expectedTag.getClass(), tokeniser.tagPending.getClass());
 }

 private void thenCreatingCommentPending() {
 Comment expectedComment = new Comment("");
 tokeniser.createCommentPending();
 Assert.assertEquals(expectedTokens.size(), tokeniser.errors.size()+1);
 Assert.assertEquals(expectedComment.data, tokeniser.commentPending.data);
 }

 private void thenCreatingDoctypePending() {
 Doctype expectedDoctype = new Doctype("");
 tokeniser.createDoctypePending();
 Assert.assertEquals(expectedTokens.size(), tokeniser.errors.size()+1);
 Assert.assertEquals(expectedDoctype.publicId, tokeniser.doctypePending.publicId);
 Assert.assertEquals(expectedDoctype.systemId, tokeniser.doctypePending.systemId);
 }

 private void expectTagPending(Token expectedTag) {
 Token actualTag = tokeniser.tagPending;
 Assert.assertEquals(expectedTag.getClass(), actualTag.getClass());
 if (expectedTag instanceof StartTag) {
 StartTag expectedStartTag = (StartTag) expectedTag;
 StartTag actualStartTag = (StartTag) actualTag;
 Assert.assertEquals(expectedStartTag.tagName, actualStartTag.tagName);
 Assert.assertArrayEquals(expectedStartTag.attributes.getNames(), actualStartTag.attributes.getNames());
 } else if (expectedTag instanceof EndTag) {
 EndTag expectedEndTag = (EndTag) expectedTag;
 EndTag actualEndTag = (EndTag) actualTag;
 Assert.assertEquals(expectedEndTag.tagName, actualEndTag.tagName);
 }
 }

 private void expectDoctypePending(Doctype expectedDoctype) {
 Doctype actualDoctype = tokeniser.doctypePending;
 Assert.assertEquals(expectedDoctype.publicId, actualDoctype.publicId);
 Assert.assertEquals(expectedDoctype.systemId, actualDoctype.systemId);
 }

 private InputStream getTestInputStream() {
 return new ByteArrayInputStream("test".getBytes(Locale.ENGLISH));
 }
}