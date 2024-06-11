import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
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

 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private ParseErrorList errors;

 @Before
 public void setup() {
 charArrayInput characterInput = new charArrayInput(new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', ' ', 's', 't', 'a', 'r', 't', '>', '\n'});
 characterReader = new CharacterReader(characterInput);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testRead_Character() {
 tokeniser.emit("a");
 tokeniser.emit('b');
 tokeniser.charBuffer.delete(0, tokeniser.charBuffer.length());
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("b", token.toString());
 }

 @Test
 public void testRead_StartTag() {
 tokeniser.emit(new StartTag("html"));
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testRead_EndTag() {
 tokeniser.emit(new StartTag("html"));
 tokeniser.emit(new EndTag("html"));
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testRead_Error() {
 characterReader = new CharacterReader(new charArrayInput(new char[]{'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', ' ', '&', '#'}));
 tokeniser = new Tokeniser(characterReader, errors);
 tokeniser.read();
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testEmit_StartTag() {
 token = tokeniser.read();
 tokeniser.emit(new StartTag("html"));
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testEmit_EndTag() {
 token = tokeniser.read();
 tokeniser.emit(new EndTag("html"));
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit('a');
 tokeniser.emit(new Character('b'));
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("b", token.toString());
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 tokeniser.emit('&');
 tokeniser.emit('#');
 tokeniser.consumeCharacterReference('x', false);
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("x", token.toString());
 }

 @Test
 public void testConsumeCharacterReference_Invalid() {
 tokeniser.emit('&');
 tokeniser.emit('#');
 tokeniser.consumeCharacterReference('X', false); // case insensitive
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamed() {
 tokeniser.emit('&');
 tokeniser.emit('A'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // 'A' as first char of name
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamedWithAttributes() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_InvalidNamed() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // 'A' as first char of name
 tokeniser.characterReferenceError("invalid named reference 'A'");
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_MissingSemicolon() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consumeLetterThenDigit(); // missing ';'
 tokeniser.characterReferenceError("missing semicolon");
 }

 @Test
 public void testCreateTagPending_StartTag() {
 tokeniser.createTagPending(true);
 tokeniser.tagPending.tagName = "html";
 token = tokeniser.read();
 tokeniser.emitTagPending();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 tokeniser.createTagPending(false);
 tokeniser.tagPending.tagName = "html";
 token = tokeniser.read();
 tokeniser.emitTagPending();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("html", token.toString());
 }

 @Test
 public void testIsAppropriateEndTagToken_True() {
 tokeniser.emit(new StartTag("html"));
 tokeniser.createTagPending(true);
 tokeniser.tagPending.tagName = "body";
 token = tokeniser.read();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 Assert.assertEquals("body", tokeniser.appropriateEndTagName());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() {
 tokeniser.emit(new StartTag("html"));
 tokeniser.createTagPending(true);
 tokeniser.tagPending.tagName = "head";
 token = tokeniser.read();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError_UnexpectedCharacter() {
 tokeniser.emit('&');
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testError_UnexpectedEOF() {
 tokeniser.emit('&');
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test
 public void testCharacterReference_ValidHex() {
 tokeniser.emit('&'); tokeniser.consume('#'); tokeniser.consumeHexSequence("1F"); tokeniser.consume(';');
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("\u001f", token.toString());
 }

 @Test
 public void testCharacterReference_ValidNamed() {
 tokeniser.emit('&'); tokeniser.consume('A'); tokeniser.consume('m'); tokeniser.consume('p'); tokeniser.consume(';');
 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("\u00E2", token.toString());
 }

 @Test
 public void testCharacterReference_InvalidHex() {
 tokeniser.emit('&'); tokeniser.consume('#'); tokeniser.consumeHexSequence("1FG"); tokeniser.consume(';');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamed() {
 tokeniser.emit('&'); tokeniser.consume('A'); tokeniser.consume('m'); tokeniser.consume('p'); tokeniser.consume('p'); tokeniser.consume(';');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('='); tokeniser.consume('"'); tokeniser.consume('b'); tokeniser.consume('"');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes_InvalidAttributeValue() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('='); tokeniser.consume('>'); // '>' as attribute value
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes_MissingAttributeValue() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('=');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes_InvalidAttributeName() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('='); tokeniser.consumeLetterThenDigit(); // invalid attribute name
 tokeniser.consume(' '); // space
 tokeniser.consume('"'); tokeniser.consume('b'); tokeniser.consume('"');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes_InvalidAttributeValueQuotes() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('='); tokeniser.consumeLetterThenDigit(); // invalid attribute name
 tokeniser.consume(' '); // space
 tokeniser.consume('"'); tokeniser.consume('b');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCharacterReference_InvalidNamedWithAttributes_InvalidAttributeValueQuotesMissing() {
 tokeniser.emit('&'); tokeniser.consumeLetterThenDigit(); tokeniser.consume(';'); // empty name
 tokeniser.consume('='); tokeniser.consume('>'); // '>' as first char of attribute
 tokeniser.consume(' '); // space
 tokeniser.consume('a'); tokeniser.consume('='); tokeniser.consumeLetterThenDigit(); // invalid attribute name
 tokeniser.consume(' '); // space
 tokeniser.consume('b');
 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.dataBuffer.append("data");
 tokeniser.dataBuffer.append("buff");
 token = tokeniser.read();
 List<Token> expectedList = new ArrayList<>();
 expectedList.add(new Comment("<!--data buff-->"));
 Assert.assertEquals(expectedList, tokeniser.getOutput());
 }
}