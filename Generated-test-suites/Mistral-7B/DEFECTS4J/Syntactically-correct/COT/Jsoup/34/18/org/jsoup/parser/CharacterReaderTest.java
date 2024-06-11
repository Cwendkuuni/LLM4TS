import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
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
 reader.consume();
 reader.consume();
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 char c = reader.current();
 reader.consume();
 Assert.assertEquals(c, reader.current());
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
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 Assert.assertEquals("ab", str);
 }

 @Test
 public void testNextIndexOfChar() {
 Assert.assertEquals(1, reader.nextIndexOf('b'));
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfCharSequence() {
 CharSequence seq = "cd";
 Assert.assertEquals(2, reader.nextIndexOf(seq));
 }

 @Test
 public void testConsumeToChar() {
 String consumed = reader.consumeTo('d');
 Assert.assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeToCharSequence() {
 CharSequence seq = "cd";
 String consumed = reader.consumeTo(seq);
 Assert.assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeToAny() {
 char[] chars = new char[]{'e', 'c'};
 String consumed = reader.consumeToAny(chars);
 Assert.assertEquals("d", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("de", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("bc", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("bc5", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("cd", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("5", consumed);
 }

 @Test
 public void testMatchesChar() {
 Assert.assertTrue(reader.matches('b'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 String seq = "cd";
 Assert.assertTrue(reader.matches(seq));
 Assert.assertFalse(reader.matches("ef"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 String seq = "CD";
 Assert.assertTrue(reader.matchesIgnoreCase(seq));
 Assert.assertFalse(reader.matchesIgnoreCase("ef"));
 }

 @Test
 public void testMatchesAny() {
 char[] chars = new char[]{'b', 'c'};
 Assert.assertTrue(reader.matchesAny(chars));
 Assert.assertFalse(reader.matchesAny(new char[]{'z'}));
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
 public void testMatchConsumeString() {
 String seq = "cd";
 reader.matchConsume(seq);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String seq = "CD";
 reader.matchConsumeIgnoreCase(seq);
 Assert.assertEquals(3, reader.pos());
 }

 @Test(expected = AssertionError.class)
 public void testContainsIgnoreCaseNull() {
 reader.containsIgnoreCase(null);
 }

 @Test
 public void testContainsIgnoreCaseEmptyString() {
 Assert.assertFalse(reader.containsIgnoreCase(""));
 }

 @Test
 public void testContainsIgnoreCaseEmptySequence() {
 Assert.assertTrue(reader.containsIgnoreCase(Character.toString(new char[0])));
 }

 @Test
 public void testContainsIgnoreCaseSingleChar() {
 reader.mark();
 String seq = "d";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseMultipleChars() {
 reader.mark();
 String seq = "cd";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseSubsequence() {
 reader.mark();
 String seq = "cd";
 String subSeq = "d";
 Assert.assertTrue(reader.containsIgnoreCase(seq));
 Assert.assertTrue(reader.containsIgnoreCase(subSeq));
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseSubsequenceMismatch() {
 reader.mark();
 String seq = "cd";
 String subSeq = "e";
 Assert.assertFalse(reader.containsIgnoreCase(seq));
 Assert.assertFalse(reader.containsIgnoreCase(subSeq));
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseSubsequenceMismatchStart() {
 reader.mark();
 String seq = "cd";
 String subSeq = "dc";
 Assert.assertFalse(reader.containsIgnoreCase(seq));
 Assert.assertFalse(reader.containsIgnoreCase(subSeq));
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("de", reader.toString());
 }
}