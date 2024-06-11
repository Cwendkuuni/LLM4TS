import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setup() {
 char[] input = new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', '>', "\n"};
 CharacterReader reader = new CharacterReader(input);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead_DataState() {
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_EmitPending() {
 tokeniser.emit(new Token.Character('a'));
 tokeniser.read();
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit(new Token.Character('a'));
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("a", token.text);
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.read();
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, tag.type);
 Assert.assertEquals("p", tag.tagName);
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.EndTag("p"));
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, tag.type);
 Assert.assertEquals("p", tag.tagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_EndTagNotAppropriate() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.EndTag("div"));
 tokeniser.read();
 }

 @Test
 public void testError_UnexpectedCharacter() {
 tokeniser.error(TokeniserState.Data);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_UnexpectedEof() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.reader.advance(); // move past <
 tokeniser.reader.advance(); // move past h
 tokeniser.reader.advance(); // move past t
 tokeniser.reader.advance(); // move past m
 tokeniser.reader.advance(); // move past l
 tokeniser.reader.mark();
 tokeniser.read(); // expected to throw an EOFException, but instead it throws an IllegalStateException
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testConsumeCharacterReference_Invalid() {
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 tokeniser.consumeCharacterReference("#x41", false);
 Assert.assertEquals('A', tokeniser.read().text.charAt(0));
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.emitTagPending();
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.emitCommentPending();
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.emitDoctypePending();
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.createTagPending(false);
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 tokeniser.emit(new Token.EndTag("div"));
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError_ParseError() {
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testError_MultipleParsingErrors() {
 char[] input = new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', '>', "\n", '<', 'p', '>', "\n"}; // multiple errors
 CharacterReader reader = new CharacterReader(input);
 Tokeniser tokeniser = new Tokeniser(reader, errors);
 tokeniser.error(TokeniserState.Data); // error 1
 tokeniser.error(TokeniserState.Data); // error 2
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testError_ParseErrorWithMessage() {
 char[] input = new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', '>', "\n", '<', 'p', '>', "\n"}; // multiple errors
 CharacterReader reader = new CharacterReader(input);
 Tokeniser tokeniser = new Tokeniser(reader, errors);
 tokeniser.error(TokeniserState.Data, "Unexpected character '%s' in input state [%s]", 'p', TokeniserState.Data); // error message
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Unexpected character 'p' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 tokeniser.consumeCharacterReference("amp", false);
 Assert.assertEquals('&', tokeniser.read().text.charAt(0));
 Assert.assertEquals('a', tokeniser.read().text.charAt(0));
 Assert.assertEquals('m', tokeniser.read().text.charAt(0));
 Assert.assertEquals('p', tokeniser.read().text.charAt(0));
 }

 @Test
 public void testConsumeCharacterReference_NamedWithAttribute() {
 tokeniser.consumeCharacterReference("amp", true);
 Assert.assertEquals("&amp;", tokeniser.read().text);
 }
}