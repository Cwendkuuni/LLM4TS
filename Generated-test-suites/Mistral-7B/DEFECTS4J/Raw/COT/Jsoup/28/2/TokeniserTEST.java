import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 char[] input = {'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', '>', '\n'};
 CharacterReader reader = new CharacterReader(new String(input).toCharArray(), 0, input.length);
 this.tokeniser = new Tokeniser(reader, new ParseErrorList());
 this.errors = this.tokeniser.getErrors();
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.StartTag, token.type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadWithNullReader() {
 tokeniser = new Tokeniser(null, new ParseErrorList());
 tokeniser.read();
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithErrorListCannotAddErrors() {
 errors = new ParseErrorList(1);
 tokeniser = new Tokeniser(new CharacterReader(new char[0], 0, 0), errors);
 tokeniser.read();
 fail("IllegalStateException expected");
 }

 @Test
 public void testEmit() {
 Token startTag = new Token.StartTag("html", null, null);
 tokeniser.emit(startTag);
 Token token = tokeniser.read();
 assertEquals(startTag, token);
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit('a');
 Token token = tokeniser.read();
 assertEquals(Token.Character.class, token.getClass());
 assertEquals('a', ((Token.Character) token).text.charAt(0));
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("abc");
 Token token = tokeniser.read();
 assertEquals(Token.Character.class, token.getClass());
 assertEquals("abc", ((Token.Character) token).text);
 }

 @Test
 public void testEmitToken() {
 Token startTag = new Token.StartTag("html", null, null);
 tokeniser.emit(startTag);
 tokeniser.emit("abc");
 Token endTag = new Token.EndTag("html");
 tokeniser.emit(endTag);
 tokeniser.transition(TokeniserState.TAG_MISMATCH);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emit(endTag);

 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.EOF) {
 tokens.add(tokeniser.read());
 }

 assertEquals(3, tokens.size());
 assertEquals(startTag, tokens.get(0));
 assertEquals(Token.Character.class, tokens.get(1).getClass());
 assertEquals(endTag, tokens.get(2));
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitWithErrorListCannotAddErrors() {
 errors = new ParseErrorList(1);
 tokeniser = new Tokeniser(new CharacterReader(new char[0], 0, 0), errors);
 tokeniser.emit(new Token.StartTag("html", null, null));
 fail("IllegalStateException expected");
 }

 @Test
 public void testConsumeCharacterReference() throws Exception {
 Character c = tokeniser.consumeCharacterReference((char) 0, false);
 assertEquals(CharacterReference.replace(0), c);

 c = tokeniser.consumeCharacterReference('&', false);
 assertNull(c);

 c = tokeniser.consumeCharacterReference('&', true);
 assertNull(c);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithErrorListCannotAddErrors() throws Exception {
 errors = new ParseErrorList(1);
 tokeniser = new Tokeniser(new CharacterReader(new char[0], 0, 0), errors);
 tokeniser.consumeCharacterReference((char) 0, false);
 fail("IllegalStateException expected");
 }

 @Test
 public void testCreateTagPending() {
 Token tagPending = tokeniser.createTagPending(true);
 assertNotNull(tagPending);
 assertEquals(Token.StartTag.class, tagPending.getClass());
 }

 @Test
 public void testCreateEndTagPending() {
 Token endTagPending = tokeniser.createEndTagPending();
 assertNotNull(endTagPending);
 assertEquals(Token.EndTag.class, endTagPending.getClass());
 }

 @Test
 public void testCreateDoctypePending() {
 Token doctypePending = tokeniser.createDoctypePending();
 assertNotNull(doctypePending);
 assertEquals(Token.Doctype.class, doctypePending.getClass());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser = new Tokeniser(new CharacterReader("</html>".toCharArray()), new ParseErrorList());
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithNullLastStartTag() {
 tokeniser = new Tokeniser(new CharacterReader("</html>".toCharArray()), new ParseErrorList());
 tokeniser.lastStartTag = null;
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithMismatchedTags() {
 tokeniser = new Tokeniser(new CharacterReader("<html><body></foo>".toCharArray()), new ParseErrorList());
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithMatchedTags() {
 tokeniser = new Tokeniser(new CharacterReader("<html><body><foo></foo></body></html>".toCharArray()), new ParseErrorList());
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsAppropriateEndTagTokenWithErrorListCannotAddErrors() {
 errors = new ParseErrorList(1);
 tokeniser = new Tokeniser(new CharacterReader("</html>".toCharArray()), errors);
 tokeniser.isAppropriateEndTagToken();
 fail("IllegalStateException expected");
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.TAG_MISMATCH);
 assertTrue(errors.hasErrors());
 assertEquals(1, errors.size());
 assertEquals(TokeniserState.TAG_MISMATCH, errors.get(0).getState());
 }

 @Test
 public void testEOFError() {
 tokeniser.eofError(TokeniserState.TAG_MISMATCH);
 assertTrue(errors.hasErrors());
 assertEquals(1, errors.size());
 assertEquals(TokeniserState.TAG_MISMATCH, errors.get(0).getState());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError("Reference Error");
 assertTrue(errors.hasErrors());
 assertEquals(1, errors.size());
 assertEquals("Reference Error", errors.get(0).getMessage());
 }

 @Test
 public void testErrorWithEmptyMessage() {
 tokeniser.error(TokeniserState.TAG_MISMATCH);
 assertTrue(errors.hasErrors());
 assertEquals(1, errors.size());
 assertNotNull(errors.get(0).getMessage());
 }

 @Test
 public void testErrorWithNonEmptyMessage() {
 tokeniser.error(TokeniserState.TAG_MISMATCH, "Error Message");
 assertTrue(errors.hasErrors());
 assertEquals(1, errors.size());
 assertEquals("Error Message", errors.get(0).getMessage());
 }
}