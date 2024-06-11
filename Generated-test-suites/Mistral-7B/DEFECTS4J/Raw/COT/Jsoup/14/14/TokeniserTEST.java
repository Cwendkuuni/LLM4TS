import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Test
 public void testTokeniserConstructor() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 Assert.assertNotNull(tokeniser);
 }

 @Test
 public void testReadNormalCharacter() {
 reader = new CharacterReader("abc");
 tokeniser = new Tokeniser(reader);
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.Character);
 Assert.assertEquals(token.data, "a");
 }

 @Test
 public void testReadEndOfStream() {
 reader = new CharacterReader("");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.read();
 Assert.assertEquals(tokeniser.getState(), TokeniserState.End);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWhenEmitPending() {
 reader = new CharacterReader("a");
 tokeniser = new Tokeniser(reader);
 tokeniser.emit(new Token.Character('a'));
 tokeniser.read();
 }

 @Test
 public void testEmitCharacter() {
 reader = new CharacterReader("ab");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.emit(new Token.Character('a'));
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.Character);
 Assert.assertEquals(token.data, "b");
 }

 @Test
 public void testEmitTagStart() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("div"));
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.StartTag);
 Assert.assertEquals(token.tagName, "div");
 }

 @Test
 public void testEmitTagEnd() {
 reader = new CharacterReader("<div></div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.read();
 tokeniser.emit(new Token.EndTag("div"));
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.EndTag);
 Assert.assertEquals(token.tagName, "div");
 }

 @Test
 public void testEmitString() {
 reader = new CharacterReader("abc");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.emit("abc");
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.Character);
 Assert.assertEquals(token.data, "d");
 }

 @Test
 public void testEmitStringWithSelfClosingFlag() {
 reader = new CharacterReader("<img src=\"image.jpg\" />");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.emitStartTag("img");
 tokeniser.read();
 tokeniser.emit("src=\"image.jpg\" ");
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read();
 tokeniser.emitEndTag("img");
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.EndTag);
 Assert.assertEquals(token.tagName, "img");
 }

 @Test
 public void testConsumeCharacterReference() {
 reader = new CharacterReader("&#x1F603;");
 tokeniser = new Tokeniser(reader);
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(character, '\uD83D\uDE03');
 }

 @Test
 public void testConsumeCharacterReferenceWithInAttribute() {
 reader = new CharacterReader("&nbsp;");
 tokeniser = new Tokeniser(reader);
 Character character = tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals(character, (char) 160);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createTagPending(true);
 Assert.assertNotNull(tokeniser.tagPending);
 Assert.assertTrue(tokeniser.tagPending instanceof Token.StartTag);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Assert.assertNull(tokeniser.tagPending);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));tokeniser.createCommentPending();
 Assert.assertNotNull(tokeniser.commentPending);
 Assert.assertTrue(tokeniser.commentPending instanceof Token.Comment);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Assert.assertNull(tokeniser.commentPending);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createDoctypePending();
 Assert.assertNotNull(tokeniser.doctypePending);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Assert.assertNull(tokeniser.doctypePending);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 reader = new CharacterReader("<div><div></div></div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenFalse() {
 reader = new CharacterReader("<div><p></div></div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.createTagPending(true);
 tokeniser.read();
 tokeniser.createTagPending(false);
 tokeniser.read();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError() {
 reader = new CharacterReader("</div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.read();
 tokeniser.error(TokeniserState.ScriptData);
 }

 @Test
 public void testEofError() {
 reader = new CharacterReader("");
 tokeniser = new Tokeniser(reader);
 tokeniser.error(TokeniserState.ProcessingInstruction);
 }

 @Test
 public void testCharacterReferenceError() {
 reader = new CharacterReader("&#x1F603;");
 tokeniser = new Tokeniser(reader);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.characterReferenceError();
 }
}