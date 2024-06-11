import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("Test Input");
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testRead_NormalCharacter() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("Test Input", token.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotFound() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read(); // this call should throw an exception
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit(new Character("A"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("A", token.toString());
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.emit(new StartTag("div"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName);
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.emit(new EndTag("div"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("div", token.tagName);
 }

 @Test
 public void testEmit_String() {
 tokeniser.emit("Test String");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Text, token.type);
 assertEquals("Test String", token.toString());
 }

 @Test
 public void testEmit_Char() {
 tokeniser.emit('A');
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("A", token.toString());
 }

 @Test
 public void testGetState() {
 TokeniserState state = tokeniser.getState();
 assertEquals(TokeniserState.Data, state);
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Whitespace);
 TokeniserState newState = tokeniser.getState();
 assertEquals(TokeniserState.Whitespace, newState);
 }

 @Test
 public void testAdvanceTransition() {
 reader.advance();
 tokeniser.advanceTransition(TokeniserState.Data);
 TokeniserState newState = tokeniser.getState();
 assertEquals(TokeniserState.Data, newState);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacter() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertEquals('A', character);
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacter() {
 Character character = tokeniser.consumeCharacterReference('B', false);
 assertEquals(replacementChar, character);
 }

 @Test
 public void testConsumeCharacterReference_InAttribute() {
 characterBuffer.setLength(0);
 tokeniser.consumeCharacterReference(null, true);
 assertFalse(characterBuffer.toString().isEmpty());
 }

 @Test
 public void testCreateTagPending_StartTag() {
 Token.StartTag tagPending = tokeniser.createTagPending(true);
 assertEquals("", tagPending.tagName);
 }

 @Test
 public void testCreateTagPending_EndTag() {
 Token.EndTag tagPending = tokeniser.createTagPending(false);
 assertEquals("", tagPending.tagName);
 }

 @Test
 public void testEmitTagPending_StartTag() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("", token.tagName);
 }

 @Test
 public void testEmitTagPending_EndTag() {
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("", token.tagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateCommentPending_StateNotData() {
 tokeniser.transition(TokeniserState.Whitespace);
 tokeniser.createCommentPending();
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("<!-- -->", token.toString());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testIsAppropriateEndTagToken_Matches() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.emit(new EndTag("div"));
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_Mismatches() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.emit(new StartTag("p"));
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors_True() {
 assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_False() {
 tokeniser.setTrackErrors(false);
 assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError_State() {
 tokeniser.error(TokeniserState.Data);
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @Test
 public void testError_Message() {
 tokeniser.error("Invalid character in input");
 assertTrue(tokeniser.errors.size() > 0);
 assertEquals("Invalid character in input", tokeniser.errors.get(0).getMessage());
 }
}