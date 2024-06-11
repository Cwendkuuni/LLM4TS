import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenizerTest;
import org.junit.Assert;

public class TokeniserTest extends TokenizerTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setup() {
 charArrayInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <html>\n" +
 " <head>\n" +
 " <title>Test</title>\n" +
 " </head>\n" +
 " <body>\n" +
 " &nbsp;\n" +
 " <foo bar=\"&baz;\" >\n" +
 " <script src=\"test.js\"></script>\n" +
 " <script type=\"text/javascript\">\n" +
 " function test() {\n" +
 " }\n" +
 " </script>\n" +
 " </foo>\n" +
 " </body>\n" +
 " </html>\n";
 tokeniser = new Tokeniser(new CharacterReader(charArrayInput), new ParseErrorList());
 errors = tokeniser.errors;
 }

 @Test
 public void testRead() {
 Token token;
 while ((token = tokeniser.read()) != null) {
 System.out.println(token);
 }
 errors.assertNoErrors();
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('t'));

 Token token = tokeniser.read();
 Assert.assertEquals("tes", token.text());
 tokeniser.emit(new Token.Character(' '));
 token = tokeniser.read();
 Assert.assertEquals("test", token.text());
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 Assert.assertEquals(TokeniserState.Comment, tokeniser.getState());
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 Assert.assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference('#', false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference("X", false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference("#x21", false);
 Assert.assertEquals('\u0021', character);
 character = tokeniser.consumeCharacterReference("#x41", false);
 Assert.assertEquals('A', character);
 character = tokeniser.consumeCharacterReference("#x1234", false);
 Assert.assertEquals('\u1234', character);
 character = tokeniser.consumeCharacterReference("#x12FF", false);
 Assert.assertEquals('\u12FF', character);
 character = tokeniser.consumeCharacterReference("#x10000", false);
 Assert.assertEquals('\uD800', character);
 character = tokeniser.consumeCharacterReference("#x10FFFF", false);
 Assert.assertEquals('\uDFFF', character);
 character = tokeniser.consumeCharacterReference("#x110000", false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference("#x200000", false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference("#1234", false);
 Assert.assertEquals(replacementChar, character);
 character = tokeniser.consumeCharacterReference("#12345", false);
 Assert.assertEquals(replacementChar, character);
 character = tokeniser.consumeCharacterReference("&nbsp;", false);
 Assert.assertEquals('\u00A0', character);
 character = tokeniser.consumeCharacterReference("&quot;", false);
 Assert.assertEquals('"', character);
 character = tokeniser.consumeCharacterReference("&amp;", false);
 Assert.assertEquals('&', character);
 character = tokeniser.consumeCharacterReference("&lt;", false);
 Assert.assertEquals('<', character);
 character = tokeniser.consumeCharacterReference("&gt;", false);
 Assert.assertEquals('>', character);
 character = tokeniser.consumeCharacterReference("&apos;", false);
 Assert.assertEquals("'", character);
 }

 @Test
 public void testErrorHandling() {
 tokeniser.error(TokeniserState.StartTag);
 errors.assertErrorCount(1);
 errors.assertErrorMessage("Unexpected character ' ' in input state [StartTag]");
 tokeniser.error(TokeniserState.EndTag);
 errors.assertErrorCount(2);
 errors.assertErrorMessage("Unexpected character ' ' in input state [EndTag]");
 tokeniser.eofError(TokeniserState.StartTag);
 errors.assertErrorCount(3);
 errors.assertErrorMessage("Unexpectedly reached end of file (EOF) in input state [StartTag]");
 tokeniser.characterReferenceError("numeric reference with no numerals");
 errors.assertErrorCount(4);
 errors.assertErrorMessage("Invalid character reference: invalid named referenece ' '");
 tokeniser.characterReferenceError("missing semicolon");
 errors.assertErrorCount(5);
 errors.assertErrorMessage("Invalid character reference: missing semicolon");
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitPendingTokenWhileReading() {
 tokeniser.emit(new Token.Character('t'));
 tokeniser.read();
 }

 @Test
 public void testAppropriateEndTagToken() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.emit("test");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.consumeCharacterReference("#script");
 tokeniser.emit("test2");
 Assert.assertEquals("testtest2", tokeniser.dataBuffer.toString());
 }

}