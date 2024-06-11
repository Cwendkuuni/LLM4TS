import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 mockCharacterReader = new CharacterReader("");
 tokeniser = new Tokeniser(mockCharacterReader);
 }

 @Test
 public void testConstructor() {
 tokeniser = new Tokeniser(mockCharacterReader);
 Assert.assertNotNull(tokeniser.reader);
 }

 @Test
 public void testRead_WhenReaderIsEmpty_ReturnsNull() throws Exception {
 Assert.assertNull(tokeniser.read());
 }

 @Test
 public void testRead_WhenCharacterIsConsumed_ReturnsCharacterToken() throws Exception {
 mockCharacterReader.setCurrent('a');
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals('a', token.asCharacter().getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_WhenEmitIsPending_ThrowsException() throws Exception {
 tokeniser.emit(new StartTag("div"));
 tokeniser.read();
 }

 @Test
 public void testEmit_WhenTokenIsNotNull_SetsEmitPending() throws Exception {
 tokeniser.emit(new StartTag("div"));
 Assert.assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testEmit_WhenTokenTypeIsStartTag_SetsLastStartTag() throws Exception {
 tokeniser.emit(new StartTag("div"));
 Token lastStartTag = tokeniser.lastStartTag;
 Assert.assertNotNull(lastStartTag);
 Assert.assertEquals("div", lastStartTag.tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmit_WhenTokenTypeIsEndTag_ThrowsExceptionWhenAttributesPresent() throws Exception {
 tokeniser.emit(new EndTag("div", new ArrayList<>()));
 }

 @Test
 public void testEmit_WhenTokenIsString_AppendsToCharBuffer() throws Exception {
 tokeniser.emit("hello world");
 String expected = "hello world";
 Assert.assertEquals(expected, charBuffer.toString());
 }

 @Test
 public void testEmit_WhenTokenIsCharacter_AppendsToCharBuffer() throws Exception {
 tokeniser.emit('a');
 Assert.assertEquals("a", charBuffer.toString());
 }

 @Test
 public void testGetState_ReturnsCurrentState() throws Exception {
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_SetsState() throws Exception {
 tokeniser.transition(TokeniserState.WhiteSpace);
 Assert.assertEquals(TokeniserState.WhiteSpace, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() throws Exception {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_WhenReaderIsEmpty_ThrowsException() throws Exception {
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testConsumeCharacterReference_WhenCharacterFound_ReturnsCharacter() throws Exception {
 mockCharacterReader.setCurrent('&#x61;'); // 'a' in hex
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals((char) 0x61, character.getValue());
 }

 @Test
 public void testConsumeCharacterReference_WhenNamedEntityFound_ReturnsNamedCharacter() throws Exception {
 mockCharacterReader.setCurrent('&amp;');
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('&', character.getValue());
 }

 @Test
 public void testCreateTagPending_WhenStartTag_SetsTagPendingToStartTag() throws Exception {
 tokeniser.createTagPending(true);
 Assert.assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
 }

 @Test
 public void testCreateTagPending_WhenEndTag_SetsTagPendingToEndTag() throws Exception {
 tokeniser.createTagPending(false);
 Assert.assertEquals(Token.TokenType.EndTag, tokeniser.tagPending.type);
 }

 @Test
 public void testEmitTagPending_WhenTagHasAttributes_ThrowsException() throws Exception {
 tokeniser.createTagPending(true);
 tokeniser.tagPending.attributes.add(new Attribute("key", "value"));
 tokeniser.emitTagPending();
 }

 @Test
 public void testEmitTagPending_WhenTagHasNoAttributes_EmitsTag() throws Exception {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Assert.assertEquals("div", tagPending.tagName);
 }

 @Test
 public void testCreateCommentPending_SetsCommentPending() throws Exception {
 tokeniser.createCommentPending();
 Assert.assertEquals(Token.TokenType.Comment, tokeniser.commentPending.type);
 }

 @Test
 public void testEmitCommentPending_EmitsComment() throws Exception {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Assert.assertEquals("", commentPending.getData());
 }

 @Test
 public void testCreateDoctypePending_SetsDoctypePending() throws Exception {
 tokeniser.createDoctypePending();
 Assert.assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
 }

 @Test
 public void testEmitDoctypePending_EmitsDoctype() throws Exception {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Assert.assertEquals("<!DOCTYPE html>", doctypePending.getData());
 }

 @Test
 public void testCreateTempBuffer_SetsDataBuffer() throws Exception {
 tokeniser.createTempBuffer();
 Assert.assertNotNull(dataBuffer);
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsTrueWhenTagsMatch() throws Exception {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit(new EndTag("div"));
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsFalseWhenTagsDonMatch() throws Exception {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit(new EndTag("p"));
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors_ReturnsTrueWhenTrackingErrors() throws Exception {
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_ReturnsFalseWhenNotTrackingErrors() throws Exception {
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError_SetsErrorOnState() throws Exception {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.error(TokeniserState.StartTag);
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Unexpected character in input", error.getMessage());
 }

 @Test
 public void testEofError_SetsErrorOnState() throws Exception {
 tokeniser.eofError(TokeniserState.StartTag);
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
 }

 @Test
 public void testCharacterReferenceError_SetsErrorOnState() throws Exception {
 tokeniser.characterReferenceError();
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Invalid character reference", error.getMessage());
 }
}