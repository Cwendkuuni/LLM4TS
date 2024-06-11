import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseError;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testEmptyInput() {
 Token token = tokeniser.read();
 Validate.isNull(token);
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testReadNonCharacterToken() {
 tokeniser.emit(new Token.StartTag("html"));
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.tagName());
 }

 @Test
 public void testReadCharacterToken() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('H');
 tokeniser.emit('e');
 tokeniser.emit('l');
 tokeniser.emit('l');
 tokeniser.emit('o');
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.Character, token.type);
 Validate.assertEquals("Hello", token.data());
 }

 @Test
 public void testReadMultipleCharacterToken() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('H');
 tokeniser.emit('e');
 tokeniser.emit('l');
 tokeniser.emit('l');
 tokeniser.emit('o');
 tokeniser.emit(' ');
 tokeniser.emit('W');
 tokeniser.emit('o');
 tokeniser.emit('r');
 tokeniser.emit('l');
 tokeniser.emit('d');
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.Character, token.type);
 Validate.assertEquals("Hello World", token.data());
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidCharacterReference() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('#');
 tokeniser.emit('x');
 tokeniser.emit(';');
 tokeniser.read(); // should not reach here
 }

 @Test
 public void testReadValidCharacterReference() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('#x');
 tokeniser.emit(';');
 tokeniser.read();
 Validate.assertEquals(Token.TokenType.Character, tokeniser.getLastToken().type);
 Validate.assertEquals('\u0003', tokeniser.getLastToken().charPending().codePointAt(0));
 }

 @Test
 public void testReadEndTag() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit(new Token.StartTag("body"));
 tokeniser.emitEndTag();
 tokeniser.emitEndTag();
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.EndTag, token.type);
 Validate.assertEquals("html", token.tagName());
 }

 @Test
 public void testReadUnexpectedEndTag() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit(new Token.StartTag("body"));
 tokeniser.emitEndTag();
 tokeniser.emit('X');
 tokeniser.emitEndTag();
 token = tokeniser.read();
 Assert.assertEquals(0, errors.size());
 Validate.assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.emit(new Token.StartTag("img", "src", "image.png", "selfClosing='true'"));
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.EndTag, token.type);
 Validate.assertEquals("img", token.tagName());
 }

 @Test
 public void testReadError() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.error("Unexpected character 'x' in input state [Data]");
 tokeniser.read();
 Assert.assertEquals(1, errors.size());
 Validate.assertEquals("Unexpected character 'x' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testReadEOF() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.read(); // consume start tag
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.eofError(TokeniserState.Data);
 token = tokeniser.read();
 Validate.assertEquals(Token.TokenType.EOF, token.type);
 }

 @Test
 public void testReadInvalidNamedCharacterReference() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('#foo');
 tokeniser.emit(';');
 tokeniser.read();
 Assert.assertEquals(1, errors.size());
 Validate.assertEquals("Invalid named referenece 'foo'", errors.get(0).getMessage());
 }

 @Test
 public void testReadValidNamedCharacterReference() {
 tokeniser.emit(new Token.StartTag("html"));
 tokeniser.emit('#amp;');
 tokeniser.emit(';');
 tokeniser.read();
 Validate.assertEquals(Token.TokenType.Character, tokeniser.getLastToken().type);
 Validate.assertEquals("\u0026", tokeniser.getLastToken().charPending().codePointAt(0));
 }
}