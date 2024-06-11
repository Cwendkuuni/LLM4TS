import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @SuppressWarnings("static-access")
 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 String inputStr = new String(input);
 this.reader = new CharacterReader(inputStr);
 }

 @Test
 public void testisEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String result = reader.consumeAsString();
 Assert.assertEquals("ab", result);
 }

 @Test
 public void testConsumeTo() {
 String result = reader.consumeTo('c');
 Assert.assertEquals("ab", reader.toString());
 Assert.assertEquals("c", reader.current());
 Assert.assertEquals("d", reader.consume());
 Assert.assertEquals("e", reader.consume());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeToEnd() {
 String result = reader.consumeToEnd();
 Assert.assertEquals("abcd", result);
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("abc", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = reader.consumeHexSequence();
 Assert.assertEquals("abc", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("123", result);
 }

 @Test
 public void testMatches() {
 char c = 'b';
 Assert.assertTrue(reader.matches(c));
 }

 @Test
 public void testMatchesString() {
 String seq = "ab";
 Assert.assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "Ab";
 Assert.assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'a', 'b'};
 Assert.assertTrue(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "ab";
 Assert.assertEquals("cd", reader.toString());
 Assert.assertTrue(reader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "Ab";
 Assert.assertEquals("cd", reader.toString());
 Assert.assertTrue(reader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "AB";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 new CharacterReader(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 new CharacterReader("");
 }
}