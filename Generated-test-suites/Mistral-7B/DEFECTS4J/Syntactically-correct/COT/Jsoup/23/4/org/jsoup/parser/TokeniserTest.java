import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseErrorList;
import static org.junit.Assert.*;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader("Testing Tokeniser");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 assertNotNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testIsEmitPendingBeforeRead() {
 tokeniser.emit(new Token.Character("A"));
 tokeniser.read();
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit("A");
 Token token = tokeniser.read();
 assertEquals("A", token.getData());
 }

 @Test
 public void testEmitToken() {
 Token startTag = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.getType());
 assertEquals(startTag.tagName, token.getData());
 }

 @Test
 public void testTransitionState() {
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransitionState() {
 tokeniser.advanceTransition(TokeniserState.Script);
 assertEquals(TokeniserState.ScriptData, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference() {
 Character charRef = tokeniser.consumeCharacterReference(null, false);
 assertNull(charRef);

 Character ref = tokeniser.consumeCharacterReference('#', false);
 assertNull(ref);

 Character ref2 = tokeniser.consumeCharacterReference("a", false);
 assertEquals('a', ref2);

 Character ref3 = tokeniser.consumeCharacterReference("ampersand", false);
 assertEquals('&', ref3);

 Character ref4 = tokeniser.consumeCharacterReference("#x9", false);
 assertEquals('\t', ref4);

 Character ref5 = tokeniser.consumeCharacterReference("#x1F", false);
 assertEquals('\u001F', ref5);

 Character ref6 = tokeniser.consumeCharacterReference("#X1F", false);
 assertEquals('\u001F', ref6);

 Character ref7 = tokeniser.consumeCharacterReference("#x1F0001", false);
 assertEquals(replacementChar, ref7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReferenceInvalidCharacter() {
 tokeniser.consumeCharacterReference('!', false);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.getType());
 }

 @Test
 public void testTransitionToCommentState() {
 tokeniser.transition(TokeniserState.Comment);
 assertEquals(TokeniserState.Comment, tokeniser.getState());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitCommentPendingBeforeCommentState() {
 tokeniser.emitCommentPending();
 }

 @Test
 public void testEmitCommentPendingInCommentState() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.getType());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.getType());
 }

 @Test
 public void testTransitionToDoctypeState() {
 tokeniser.transition(TokeniserState.Doctype);
 assertEquals(TokeniserState.Doctype, tokeniser.getState());
 }

 @Test
 public void testEmitDoctypePendingInDoctypeState() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitTagPendingBeforeTagState() {
 tokeniser.emitTagPending();
 }

 @Test
 public void testEmitTagPendingInTagState() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.getType());
 assertEquals("div", token.getData());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitEndTagPendingBeforeEndTagState() {
 tokeniser.emitEndTagPending();
 }

 @Test
 public void testEmitEndTagPendingInEndTagState() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitEndTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.getType());
 assertEquals("div", token.getData());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitEndTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 tokeniser.transition(TokeniserState.Tag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitEndTagPending();
 assertEquals("div", tokeniser.appropriateEndTagName());
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 assertEquals(1, errors.size());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError("numeric reference with no numerals");
 assertEquals(1, errors.size());
 }
}