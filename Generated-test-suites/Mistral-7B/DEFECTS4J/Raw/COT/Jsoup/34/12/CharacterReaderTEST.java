import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(reader.isEmpty());
 reader.consume();
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 char c = reader.current();
 reader.consume();
 Assert.assertEquals(c, reader.current());
 reader.consume();
 Assert.assertEquals(EOF, reader.current());
 }

 @Test
 public void testUnconsume() {
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
 String str = reader.consumeAsString();
 Assert.assertEquals("a", str);
 reader.consume();
 reader.consume();
 str = reader.consumeAsString();
 Assert.assertEquals("bc", str);
 }

 @Test
 public void testNextIndexOf() {
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertEquals(4, reader.nextIndexOf('d'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 CharSequence cs = "efg";
 Assert.assertEquals(-1, reader.nextIndexOf(cs));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeHexSequence();
 Assert.assertEquals(7, reader.nextIndexOf(cs));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfThrowsException() {
 char c = reader.current();
 reader.consume();
 reader.nextIndexOf(c); // should be at position 1, but consume() already moved to position 2
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo('e');
 Assert.assertEquals("ab", str);
 reader.consume();
 str = reader.consumeTo('e');
 Assert.assertEquals("bc", str);
 str = reader.consumeTo('z');
 Assert.assertEquals(new String(reader.input, pos, reader.length-pos), "");
 }

 @Test
 public void testConsumeToWithCharSequence() {
 String str = reader.consumeTo("efg");
 Assert.assertEquals("abcdefg", str);
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeHexSequence();
 reader.consumeDigitSequence();
 str = reader.consumeTo("efg");
 Assert.assertEquals("hijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789efg", str);
 }

 @Test
 public void testConsumeToAny() {
 String str = reader.consumeToAny('a', 'b', 'c');
 Assert.assertEquals("abca", str);
 str = reader.consumeToAny('x', 'y', 'z');
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 Assert.assertEquals("ijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", str);
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", str);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String str = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("ab1cd", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 Assert.assertEquals("abcdef", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", str);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 reader.consume();
 Assert.assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesWithString() {
 Assert.assertTrue(reader.matches("ab"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 Assert.assertFalse(reader.matches("ab"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('a', 'b'));
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.matchesAny('a', 'b'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeWithString() {
 String str = reader.consumeTo("efg");
 reader.matchConsume("efg");
 Assert.assertEquals("", str);
 }

 @Test
 public void testMatchConsumeIgnoreCaseWithString() {
 String str = reader.consumeTo("EFG");
 reader.matchConsumeIgnoreCase("efg");
 Assert.assertEquals("", str);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("efg"));
 reader.consumeLetterSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeLetterThenDigitSequence();
 reader.consumeHexSequence();
 reader.consumeDigitSequence();
 Assert.assertTrue(reader.containsIgnoreCase("EFG"));
 }

 @Test
 public void testToString() {
 String str = reader.toString();
 Assert.assertEquals("ijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", str);
 }

}