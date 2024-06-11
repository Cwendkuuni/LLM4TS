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
import org.jsoup.parser.TokeniserTest;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private InputStream input;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 input = new ByteArrayInputStream("Test Input".getBytes());
 errors = new CopyOnWriteArrayList<>();
 tokeniser = new Tokeniser(new CharacterReader(input), errors);
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 assertTrue(token instanceof Character);
 assertEquals("Test", ((Character) token).text);
 }

 @Test(expected = AssertionError.class)
 public void testReadWhenNoCharacterPending() {
 tokeniser.read();
 fail("A character should have been pending after setting up the tokeniser");
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new StartTag("div"));
 assertEquals("div", tokeniser.lastStartTag.tagName);
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("Test String");
 assertEquals("Test String", tokeniser.charBuffer.toString());
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit('A');
 assertEquals("A", tokeniser.charBuffer.toString());
 }

 @Test
 public void testGetState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() throws IOException {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.reader.advance();
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReferenceNormal() throws IOException {
 Character ref = tokeniser.consumeCharacterReference(null, false);
 assertEquals('T', ref.charValue);
 }

 @Test
 public void testConsumeCharacterReferenceInvalid() throws IOException {
 Character ref = tokeniser.consumeCharacterReference('a', false);
 assertNull(ref);
 }

 @Test
 public void testConsumeCharacterReferenceInAttribute() throws IOException {
 Character ref = tokeniser.consumeCharacterReference('&', true);
 assertNull(ref);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 assertNotNull(tokeniser.tagPending);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.emitTagPending();
 assertNotNull(tokeniser.emitPending);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 assertNotNull(tokeniser.commentPending);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.emitCommentPending();
 assertNotNull(tokeniser.emitPending);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 assertNotNull(tokeniser.doctypePending);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.emitDoctypePending();
 assertNotNull(tokeniser.emitPending);
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 assertNotNull(tokeniser.dataBuffer);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWhenNoPreviousStartTag() {
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test(expected = AssertionError.class)
 public void testIsAppropriateEndTagTokenWhenPreviousStartTagHasDifferentName() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.tagPending.tagName = "p";
 assertTrue(tokeniser.isAppropriateEndTagToken());
 fail("Should not reach this point as the previous start tag name is different");
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Script);
 assertEquals(1, errors.size());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError("Test Error");
 assertEquals(1, errors.size());
 }

 @Test
 public void testErrorWhenNoErrorsAllowed() {
 tokeniser.errors = new ArrayList<>();
 tokeniser.error(TokeniserState.Data);
 assertTrue(errors.isEmpty());
 }
}