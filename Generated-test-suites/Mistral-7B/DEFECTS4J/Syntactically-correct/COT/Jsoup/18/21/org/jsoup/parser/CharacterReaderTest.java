import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setup() {
 input = "abcdef123ABC456<title>Test</title>";
 reader = new CharacterReader(input);
 }

 // Test all methods in CharacterReader class

 // Test isEmpty() method
 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 // Test pos(), current(), and consume() methods
 @Test
 public void testPosCurrentConsume() {
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(1, reader.pos());
 }

 // Test unconsume() method
 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 // Test advance() method
 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 // Test mark() and rewindToMark() methods
 @Test
 public void testMarkRewindToMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 // Test consumeAsString() method
 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 Assert.assertEquals("a", consumed);
 Assert.assertEquals('b', reader.current());
 }

 // Test consumeTo() method with char
 @Test
 public void testConsumeToChar() {
 String consumed = reader.consumeTo((char) 0);
 Assert.assertEquals("abcdef1", consumed);
 }

 // Test consumeTo() method with String
 @Test
 public void testConsumeToString() {
 String consumed = reader.consumeTo("ef");
 Assert.assertEquals("abcd", consumed);
 }

 // Test consumeToAny() method with char array
 @Test
 public void testConsumeToAny() {
 String consumed = reader.consumeToAny('d', 'f');
 Assert.assertEquals("ef", consumed);
 }

 // Test consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("<title>Test</title>", consumed);
 }

 // Test consumeLetterSequence() method
 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("title", consumed);
 }

 // Test consumeHexSequence() method
 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("abcdef", consumed);
 }

 // Test consumeDigitSequence() method
 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("123", consumed);
 }

 // Test matches() method with char
 @Test
 public void testMatchesChar() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('b'));
 }

 // Test matches() method with String
 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("def"));
 Assert.assertFalse(reader.matches("efg"));
 }

 // Test matchesIgnoreCase() method with String
 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("title"));
 Assert.assertFalse(reader.matchesIgnoreCase("efg"));
 }

 // Test matchesAny() method with char array
 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('d', 'f'));
 Assert.assertFalse(reader.matchesAny('g', 'h'));
 }

 // Test matchesLetter() method
 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 }

 // Test matchesDigit() method
 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(reader.matchesLetter());
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test matchConsume() method with String
 @Test
 public void testMatchConsumeString() {
 String consumed = reader.matchConsume("ef");
 Assert.assertEquals("ef", consumed);
 }

 // Test matchConsumeIgnoreCase() method with String
 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("Title");
 Assert.assertEquals("title", consumed);
 }

 // Test containsIgnoreCase() method with String
 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertFalse(reader.containsIgnoreCase("efg"));
 }

 // Edge cases and exception handling testing

 // Test consume() method with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testConsumeEmptyInput() {
 CharacterReader emptyReader = new CharacterReader("");
 emptyReader.consume();
 }

 // Test consumeTo() method with empty sequence
 @Test
 public void testConsumeToEmptySequence() {
 String consumed = reader.consumeTo("");
 Assert.assertEquals("", consumed);
 }

 // Test consumeTo() method with non-existent char
 @Test
 public void testConsumeToNonExistentChar() {
 String consumed = reader.consumeTo((char) 0xff);
 Assert.assertEquals(reader.consumeToEnd(), consumed);
 }

 // Test consumeTo() method with non-existent String
 @Test
 public void testConsumeToNonExistentString() {
 String consumed = reader.consumeTo("xyz");
 Assert.assertEquals(reader.consumeToEnd(), consumed);
 }

 // Test consumeToAny() method with non-existent char array
 @Test
 public void testConsumeToAnyNonExistentCharArray() {
 String consumed = reader.consumeToAny((char) 0xff, 'z');
 Assert.assertEquals(reader.consumeToEnd(), consumed);
 }
}