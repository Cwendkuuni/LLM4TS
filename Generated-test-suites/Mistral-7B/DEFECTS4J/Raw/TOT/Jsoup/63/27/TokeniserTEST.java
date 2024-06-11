import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private ParseErrorList errorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockCharacterReader = new MockCharacterReader();
 errorList = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, errorList);
 }

 @Test
 public void readTest() {
 // Test reading a single character
 mockCharacterReader.setup("<");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("<", token.getData());
 assertEquals(TokeniserState.TagStart, token.getState().name());

 // Test reading a string of characters
 mockCharacterReader.setup("Hello World");
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("Hello World", token.getData());
 assertEquals(TokeniserState.Data, token.getState().name());
 }

 @Test
 public void emitTest() {
 // Test emitting a single character
 tokeniser.emit(new Token.Character('A'));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("A", token.getData());

 // Test emitting a start tag
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.emit(new Token.StartTag("div"));
 token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.getData().getName());

 // Test emitting an end tag
 tokeniser.emit(new Token.EndTag("div"));
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("div", token.getData().getName());
 }

 @Test
 public void errorHandlingTest() {
 // Test error handling for unexpected character in TagStart state
 mockCharacterReader.setup("&");
 tokeniser.transition(TokeniserState.TagStart);
 tokeniser.read();
 tokeniser.error(TokeniserState.TagStart);
 assertTrue(errorList.hasErrors());
 assertEquals(1, errorList.getSize());
 assertEquals("Unexpected character '&' in input state [TagStart]", errorList.get(0).getMessage());

 // Test error handling for unexpected character in Data state
 mockCharacterReader.setup("&");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 assertTrue(errorList.hasErrors());
 assertEquals(1, errorList.getSize());
 assertEquals("Unexpected character '&' in input state [Data]", errorList.get(0).getMessage());
 }

 @Test
 public void consumeCharacterReferenceTest() {
 // Test consuming a character reference with a valid numeric value
 mockCharacterReader.setup("#x41");
 int[] codepoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, codepoints.length);
 assertEquals('A', (char) codepoints[0]);

 // Test consuming a character reference with an invalid numeric value
 mockCharacterReader.setup("#x0xFFFF");
 codepoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(0, codepoints.length);
 assertTrue(errorList.hasErrors());
 assertEquals(1, errorList.getSize());
 assertEquals("Invalid character reference: character outside of valid range", errorList.get(0).getMessage());

 // Test consuming a character reference with a valid named value
 mockCharacterReader.setup("#namedEntity");
 int[] codepoints2 = tokeniser.consumeCharacterReference(null, false);
 assertEquals(2, codepoints2.length);
 assertEquals('n', (char) codepoints2[0]);
 assertEquals('a', (char) codepoints2[1]);

 // Test consuming a character reference with an invalid named value
 mockCharacterReader.setup("#invalidNamedEntity");
 codepoints2 = tokeniser.consumeCharacterReference(null, false);
 assertEquals(0, codepoints2.length);
 assertTrue(errorList.hasErrors());
 assertEquals(1, errorList.getSize());
 assertEquals("Invalid character reference: invalid named reference 'invalidNamedEntity'", errorList.get(0).getMessage());
}

 @Test
 public void createTagPendingTest() {
 // Test creating a start tag pending
 tokeniser.createTagPending(true);
 Token tagPending = tokeniser.getTagPending();
 assertEquals(Token.TokenType.StartTag, tagPending.type);
 assertNull(tagPending.getData().getName());
 assertNull(tagPending.getData().getAttributes());

 // Test creating an end tag pending
 tokeniser.createTagPending(false);
 tagPending = tokeniser.getTagPending();
 assertEquals(Token.TokenType.EndTag, tagPending.type);
 assertNull(tagPending.getData().getName());
 assertNull(tagPending.getData().getAttributes());
 }

 @Test
 public void emitTagPendingTest() {
 // Test emitting a start tag pending
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token tagPending = tokeniser.getTagPending();
 assertEquals(Token.TokenType.StartTag, tagPending.type);
 assertEquals("div", tagPending.getData().getName());
 assertNull(tagPending.getData().getAttributes());

 // Test emitting an end tag pending
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tagPending = tokeniser.getTagPending();
 assertEquals(Token.TokenType.EndTag, tagPending.type);
 assertEquals("div", tagPending.getData().getName());
 assertNull(tagPending.getData().getAttributes());
 }

 @Test
 public void unescapeEntitiesTest() {
 // Test unescaping entities in a string with no entities
 String input = "Hello World";
 String output = tokeniser.unescapeEntities(false);
 assertEquals(input, output);

 // Test unescaping entities in a string with character references
 input = "Hello &nbsp; World";
 output = tokeniser.unescapeEntities(false);
 assertEquals("Hello   World", output);

 // Test unescaping entities in a string with named entities
 input = "Hello &copy; 2022 World";
 output = tokeniser.unescapeEntities(false);
 assertEquals("Hello © 2022 World", output);

 // Test unescaping entities in a string with incorrectly formatted named entities
 input = "Hello &copy 2022 World";
 output = tokeniser.unescapeEntities(false);
 assertEquals("Hello &copy; 2022 World", output);
 }
}

class MockCharacterReader {
 private String readBuffer;
 private int readPosition;
 private int readLimit;
 private char currentCharacter;

 public MockCharacterReader() {
 readBuffer = "";
 readPosition = 0;
 readLimit = 0;
 currentCharacter = '\u0000';
 }

 public void setup(String input) {
 readBuffer = input;
 readLimit = readBuffer.length();
 }

 public boolean isEmpty() {
 return readPosition >= readLimit;
 }

 public char consume() {
 if (readPosition >= readLimit)
 throw new IllegalStateException("Cannot consume from empty reader");
 currentCharacter = readBuffer.charAt(readPosition++);
 return currentCharacter;
 }

 public boolean match(char expected) {
 if (isEmpty())
 return false;
 if (currentCharacter == expected) {
 consume();
 return true;
 }
 return false;
 }

 public boolean matchConsume(char expected) {
 if (match(expected))
 return true;
 consume();
 return false;
 }

 public boolean matches(char... expectedChars) {
 for (char expectedChar : expectedChars) {
 if (!match(expectedChar))
 return false;
 }
 consumeMultiple(expectedChars.length);
 return true;
 }

 public boolean matchesAny(char[] expectedChars) {
 for (char expectedChar : expectedChars) {
 if (match(expectedChar))
 return true;
 }
 return false;
 }

 public boolean matchesAnySorted(char[] expectedChars) {
 Arrays.sort(expectedChars);
 for (char expectedChar : expectedChars) {
 if (match(expectedChar))
 return true;
 }
 return false;
 }

 public boolean matchesLetter() {
 if (!isAlpha(currentCharacter))
 return false;
 consume();
 return true;
 }

 public boolean matchesDigit() {
 if (!isDigit(currentCharacter))
 return false;
 consume();
 return true;
 }

 public boolean matches(String expected) {
 if (isEmpty())
 return false;
 int i = 0;
 for (char expectedChar : expected.toCharArray()) {
 if (!match(expectedChar))
 return false;
 i++;
 }
 consumeMultiple(expected.length());
 return true;
 }

 public boolean consumeLetterThenDigitSequence() {
 if (!matchesLetter())
 return false;
 if (!matchesDigit())
 return false;
 consume();
 consume();
 return true;
 }

 public void consumeDigitSequence() {
 while (matchesDigit()) {
 consume();
 }
 }

 public void consumeLetterSequence() {
 while (matchesLetter()) {
 consume();
 }
 }

 public void consumeCodePointSequence(int length) {
 consumeMultiple(length * 3);
 }

 public void consumeMultiple(int length) {
 for (int i = 0; i < length; i++) {
 consume();
 }
 }

 public void mark() {
 readPosition--;
 }

 public void rewindToMark() {
 consumeMultiple(readPosition - readPosition--);
 }
}