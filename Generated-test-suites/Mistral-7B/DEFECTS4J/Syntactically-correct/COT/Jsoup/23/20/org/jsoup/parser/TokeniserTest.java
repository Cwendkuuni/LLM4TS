import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_WhenCalledForTheFirstTime_ShouldReturnCharacterToken() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_WhenCalledAfterCharacterTokenIsEmitted_ShouldReturnPendingToken() {
 tokeniser.emit(new Token.Character('t'));
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertEquals(token.type, Token.TokenType.StartTag);
 }

 @Test
 public void testRead_WhenCalledAfterStartTagTokenIsEmitted_ShouldReturnEndTagTokenOrNull() {
 Token startTag = new Token.StartTag("div");
 tokeniser.emit(startTag);
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 if (token != null) {
 assertTrue(token.type == Token.TokenType.EndTag);
 assertEquals(token.tagName, "div");
 } else {
 assertNull(token);
 }
 }

 @Test
 public void testRead_WhenCharacterIsConsumed_ShouldReturnCharacterToken() {
 tokeniser.emitCharacterReference(null, false);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRead_WhenCharacterReferenceIsConsumedWithInvalidCharacter_ShouldThrowException() {
 tokeniser.emitCharacterReference('\uD801', false);
 tokeniser.read();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRead_WhenCharacterReferenceIsConsumedWithCharacterInAttribute_ShouldThrowException() {
 tokeniser.transition(TokeniserState.Attribute);
 tokeniser.emitCharacterReference(null, true);
 tokeniser.read();
 }

 @Test
 public void testRead_WhenReadAfterCharacterReferenceIsConsumed_ShouldReturnNextCharacter() {
 tokeniser.emitCharacterReference(null, false);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testRead_WhenReadAfterTagOrCommentIsEmitted_ShouldReturnTagOrCommentToken() {
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals(token.type, Token.TokenType.EndTag);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals(token.type, Token.TokenType.Comment);
 }

 @Test
 public void testEmit_WhenCalledWithStartTag_ShouldAddStartTagToPendingList() {
 tokeniser.emit(new Token.StartTag("div"));
 token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 }

 @Test
 public void testEmit_WhenCalledWithEndTag_ShouldAddEndTagToPendingList() {
 tokeniser.emit(new Token.EndTag("div"));
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 }

 @Test
 public void testEmit_WhenCalledWithCharacter_ShouldAddCharacterToPendingList() {
 tokeniser.emit('t');
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testEmit_WhenCalledWithString_ShouldAddStringToPendingList() {
 tokeniser.emit("test");
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 tokeniser.read();
 tokeniser.read();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testGetState_ShouldReturnCurrentStateOfTokeniser() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_ShouldChangeStateOfTokeniser() {
 tokeniser.transition(TokeniserState.StartTagName);
 assertEquals(TokeniserState.StartTagName, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition_ShouldChangeStateOfTokeniserAndAdvanceReader() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagName);
 assertEquals(TokeniserState.StartTagName, tokeniser.getState());
 assertEquals("t", reader.current().toString());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag_ShouldSetSelfClosingFlagAcknowledged() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testError_ShouldAddErrorToErrorList() {
 tokeniser.error(TokeniserState.Data);
 assertTrue(errors.hasErrors());
 }

 @Test
 public void testEofError_ShouldAddErrorToErrorList() {
 tokeniser.eofError(TokeniserState.Data);
 assertTrue(errors.hasErrors());
 }

 @Test
 public void testCharacterReferenceError_ShouldAddErrorToErrorList() {
 tokeniser.characterReferenceError("character outside of valid range");
 assertTrue(errors.hasErrors());
 }
}