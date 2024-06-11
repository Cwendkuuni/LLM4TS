import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @SuppressWarnings("static-access")
 @Before
 public void setup() {
 char[] inputArray = new char[]{'a', 'b', 'c', 'd', 'e'};
 String input = new String(inputArray);
 characterReader = new CharacterReader(input);
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader(null);
 Assert.assertTrue("Expected an exception when input is null", characterReader instanceof IllegalStateException);
 characterReader = new CharacterReader("");
 Assert.assertFalse("Expected an empty reader when input is empty", characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals("Initial position should be 0", 0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse("Expected false when reader is not empty", characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue("Expected true when reader is empty after consuming two characters", characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals("Expected 'a' as the first character", 'a', characterReader.current());
 }

 @Test
 public void testConsume() {
 char expectedChar = 'a';
 Assert.assertEquals("Expected the current character to be consumed", expectedChar, characterReader.consume());
 Assert.assertEquals("Expected the position to be incremented", 1, characterReader.pos());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals("Expected an exception when trying to consume a character past the end", characterReader.consume(), CharacterReader.EOF);
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals("Expected the position to be decremented", 1, characterReader.pos() - 1);
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals("Expected the position to be incremented", 2, characterReader.pos());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 Assert.assertEquals("Expected the position to be the same after marking and consuming a character", characterReader.pos(), characterReader.mark);
 }

 @Test
 public void testRewindToMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals("Expected the position to be the same after rewinding to the mark", characterReader.pos(), characterReader.mark);
 }

 @Test
 public void testConsumeAsString() {
 String expected = "ab";
 Assert.assertEquals("Expected the consumed string to be 'ab'", expected, characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 String expected = "bc";
 Assert.assertEquals("Expected the consumed string to be 'bc'", expected, characterReader.consumeTo('c'));
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "def";
 Assert.assertEquals("Expected the consumed string to be 'def'", expected, characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abc";
 Assert.assertEquals("Expected the consumed letter sequence to be 'abc'", expected, characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "123";
 Assert.assertEquals("Expected the consumed hex sequence to be '123'", expected, characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 Assert.assertEquals("Expected the consumed digit sequence to be '123'", expected, characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue("Expected true when current character matches 'a'", characterReader.matches('a'));
 Assert.assertFalse("Expected false when current character does not match 'b'", characterReader.matches('b'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader characterReader2 = new CharacterReader("ABC");
 Assert.assertTrue("Expected true when current character matches 'a' ignoring case", characterReader2.matchesIgnoreCase("a"));
 Assert.assertFalse("Expected false when current character does not match 'z' ignoring case", characterReader2.matchesIgnoreCase("z"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader characterReader2 = new CharacterReader("abcd");
 Assert.assertTrue("Expected true when current character matches 'a'", characterReader2.matchesAny('a'));
 Assert.assertTrue("Expected true when current character matches 'd'", characterReader2.matchesAny('d'));
 Assert.assertFalse("Expected false when current character does not match 'e'", characterReader2.matchesAny('e'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue("Expected true when current character is a letter", characterReader.matchesLetter());
 characterReader.consume();
 Assert.assertFalse("Expected false when current character is not a letter", characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue("Expected true when current character is a digit", characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertFalse("Expected false when current character is not a digit", characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "abc";
 Assert.assertTrue("Expected true when sequence 'abc' is matched and consumed", characterReader.matchConsume("abc"));
 Assert.assertEquals("Expected the position to be the end of the sequence after matching and consuming 'abc'", 6, characterReader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "ABC";
 Assert.assertTrue("Expected true when sequence 'ABC' is matched and consumed ignoring case", characterReader.matchConsumeIgnoreCase("abc"));
 Assert.assertEquals("Expected the position to be the end of the sequence after matching and consuming 'ABC'", 6, characterReader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader characterReader2 = new CharacterReader("abcdef<title>Test<title>");
 Assert.assertTrue("Expected true when sequence '<title>' is present ignoring case", characterReader2.containsIgnoreCase("<title>"));
 Assert.assertFalse("Expected false when sequence 'Test' is not present ignoring case", characterReader2.containsIgnoreCase("Test123"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("Expected the remaining input to be 'def'", "def", characterReader.toString());
 }
}