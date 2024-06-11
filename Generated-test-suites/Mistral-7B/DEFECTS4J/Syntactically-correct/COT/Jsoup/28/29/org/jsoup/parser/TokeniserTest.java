import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenizerTest;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private ParseErrorList parseErrors;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 char[] input = {'<', 'h', 't', 'm', 'l', ' ', 'd', 'i', 'v', '>', "\n"};
 characterReader = new MockCharacterReader(input);
 parseErrors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrors);
 }

 @Test
 public void testRead_DataState() {
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.tagName.tagName);
 }

 @Test(expected = ParseError.class)
 public void testRead_ErrorState() {
 tokeniser.transition(TokeniserState.Error);
 tokeniser.read();
 }

 @Test
 public void testEmit_Character() {
 char c = 'a';
 tokeniser.emit(c);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("a", token.text.getText());
 }

 @Test
 public void testEmit_Tag() {
 Token tagPending = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals(tagPending.tagName.tagName, token.tagName.tagName);
 }

 @Test
 public void testConsumeCharacterReference_ValidReference() {
 Character character = tokeniser.consumeCharacterReference('\u0061', false);
 Assert.assertEquals('\u0061', character.toChars()[0]);
 }

 @Test(expected = ParseError.class)
 public void testConsumeCharacterReference_InvalidReference() {
 tokeniser.consumeCharacterReference('&', false);
 }

 @Test
 public void testTransition_DataState() {
 tokeniser.transition(TokeniserState.Script);
 }

 @Test
 public void testTransition_ScriptState() {
 tokeniser.transition(TokeniserState.Data);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test(expected = ParseError.class)
 public void testError_ErrorState() {
 tokeniser.error(TokeniserState.Error);
 }

 @Test(expected = ParseError.class)
 public void testEofError_DataState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_UnexpectedReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("Unexpected error message");
 }

 @Test
 public void testIsAppropriateEndTagToken_AppropriateEndTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_InappropriateEndTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(false);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testRead_ScriptState() {
 tokeniser.transition(TokeniserState.Script);
 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Script, token.type);
 }
}

 class MockCharacterReader implements CharacterReader {
 private char[] buffer;
 private int index;

 public MockCharacterReader(char[] buffer) {
 this.buffer = buffer;
 }

 @Override
 public char current() {
 return buffer[index];
 }

 @Override
 public boolean isEmpty() {
 return index == buffer.length;
 }

 @Override
 public void mark(int readAheadLimit) {
 // do nothing
 }

 @Override
 public int read() throws IOException {
 if (index < buffer.length) {
 index++;
 return buffer[index-1];
 } else {
 return -1;
 }
 }

 @Override
 public boolean matches(char expected) {
 return current() == expected;
 }

 @Override
 public boolean matchesAny(char... expected) {
 for (char c : expected) {
 if (current() == c) {
 return true;
 }
 }
 return false;
 }

 @Override
 public boolean matchesIgnoreCase(char expected) {
 return Character.isUpperCase(expected) ? Character.isUpperCase(current()) && Character.toLowerCase(current()) == Character.toLowerCase(expected) : current() == expected;
 }

 @Override
 public boolean matchesLetter() {
 return Character.isLetter(current());
 }

 @Override
 public boolean matchesDigit() {
 return Character.isDigit(current());
 }

 @Override
 public boolean matchesWhitespace() {
 return Character.isWhitespace(current());
 }

 @Override
 public boolean matchesAny(CharSequence expected) {
 return expected.toString().contains(String.valueOf(current()));
 }

 @Override
 public void consume() throws IOException {
 index++;
 }

 @Override
 public void consumeHexSequence() {
 // do nothing
 }

 @Override
 public void consumeLetterThenDigitSequence() {
 // do nothing
 }

 @Override
 public void consumeDigitSequence() {
 // do nothing
 }
 }