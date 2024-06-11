import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Test
 public void testTokeniserConstructor() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
 Assert.assertNotNull(tokeniser);
 }

 @Test
 public void testReadCharacter() {
 reader = new CharacterReader("abc");
 tokeniser = new Tokeniser(reader);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("a", token.data);
 }

 @Test
 public void testReadSelfClosingTag() {
 reader = new CharacterReader("<img src=\"image.jpg\" />");
 tokeniser = new Tokeniser(reader);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("img", token.data.tagName);
 Assert.assertTrue(token.selfClosing);
 }

 @Test
 public void testReadStartTag() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("div", token.data.tagName);
 Assert.assertFalse(token.selfClosing);
 }

 @Test
 public void testReadEndTag() {
 reader = new CharacterReader("</div>");
 tokeniser = new Tokeniser(reader);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("div", token.data.tagName);
 }

 @Test
 public void testReadComment() {
 reader = new CharacterReader("<!-- comment -->");
 tokeniser = new Tokeniser(reader);
 tokeniser.createCommentPending();
 tokeniser.read(); // consume "<"
 tokeniser.read(); // consume "--"
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, token.type);
 Assert.assertEquals("comment", token.data.data);
 tokeniser.emitCommentPending();
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidCharacterReference() {
 reader = new CharacterReader("&#x0;");
 tokeniser = new Tokeniser(reader);
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testReadValidCharacterReference() {
 reader = new CharacterReader("&#123;");
 tokeniser = new Tokeniser(reader);
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals((char) 123, character.data);
 }

 @Test
 public void testReadValidCharacterReferenceWithAdditionalAllowedCharacter() {
 reader = new CharacterReader("&#xA0;x");
 tokeniser = new Tokeniser(reader);
 Character character = tokeniser.consumeCharacterReference('\u0007', '\u0007');
 Assert.assertEquals((char) 160, character.data);
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidNamedCharacterReference() {
 reader = new CharacterReader("&ab;");
 tokeniser = new Tokeniser(reader);
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testReadValidNamedCharacterReference() {
 reader = new CharacterReader("&auml;");
 tokeniser = new Tokeniser(reader);
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('ä', character.data);
 }

 @Test
 public void testReadInvalidNamedCharacterReferenceWithSemicolon() {
 reader = new CharacterReader("&auml;");
 tokeniser = new Tokeniser(reader);
 reader.mark();
 tokeniser.consumeCharacterReference(null, false); // consume "&" and "auml"
 Assert.assertFalse(reader.matched()); // expect "semicolon missing" error
 reader.rewind();
 }

 @Test
 public void testCreateTagPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createTagPending(true);
 Assert.assertNotNull(tokeniser.tagPending);
 Assert.assertEquals(TagType.StartTag, tokeniser.tagPending.tagType);
 }

 @Test
 public void testEmitTagPending() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.createTagPending(true);
 tokeniser.read(); // consume "<"
 tokeniser.read(); // consume "div"
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("div", token.data.tagName);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createCommentPending();
 Assert.assertNotNull(tokeniser.commentPending);
 Assert.assertEquals(TagType.Comment, tokeniser.commentPending.tagType);
 }

 @Test
 public void testEmitCommentPending() {
 reader = new CharacterReader("<!-- comment -->");
 tokeniser = new Tokeniser(reader);
 tokeniser.createCommentPending();
 tokeniser.read(); // consume "<"
 tokeniser.read(); // consume "--"
 tokeniser.read(); // consume ">"
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Comment, token.type);
 Assert.assertEquals("comment", token.data.data);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser = new Tokeniser(new CharacterReader(""));
 tokeniser.createDoctypePending();
 Assert.assertNotNull(tokeniser.doctypePending);
 Assert.assertEquals(TagType.Doctype, tokeniser.doctypePending.tagType);
 }

 @Test
 public void testEmitDoctypePending() {
 reader = new CharacterReader("<!DOCTYPE html>");
 tokeniser = new Tokeniser(reader);
 tokeniser.createDoctypePending();
 tokeniser.read(); // consume "<!"
 tokeniser.read(); // consume "DOCTYPE"
 tokeniser.read(); // consume "html"
 tokeniser.read(); // consume ">"
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 reader = new CharacterReader("<div><div></div></div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.createTagPending(true);
 tokeniser.read(); // consume "<div"
 tokeniser.createTagPending(true);
 tokeniser.read(); // consume "<div"
 tokeniser.read(); // consume "</div>"
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenFalse() {
 reader = new CharacterReader("<div><div></div></div><p>");
 tokeniser = new Tokeniser(reader);
 tokeniser.createTagPending(true);
 tokeniser.read(); // consume "<div"
 tokeniser.createTagPending(true);
 tokeniser.read(); // consume "</div>"
 tokeniser.read(); // consume "<p"
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrorsTrue() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testIsTrackErrorsFalse() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.setTrackErrors(false);
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(0, tokeniser.errors.size());
 }

 @Test
 public void testError() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.error(TokeniserState.Error);
 Assert.assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testEofError() {
 reader = new CharacterReader("");
 tokeniser = new Tokeniser(reader);
 tokeniser.eofError(TokeniserState.Data);
 Assert.assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testCharacterReferenceError() {
 reader = new CharacterReader("<div>");
 tokeniser = new Tokeniser(reader);
 tokeniser.characterReferenceError();
 Assert.assertEquals(1, tokeniser.errors.size());
 }
}