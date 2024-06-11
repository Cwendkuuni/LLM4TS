import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = {'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', '.'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 char c = reader.current();
 Assert.assertEquals('t', c);
 reader.consume();
 Assert.assertEquals('h', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEndOfInput() {
 reader.consume();
 reader.consume(); // this should throw an exception
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('h', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 Assert.assertEquals("is a ", str);
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo('C');
 Assert.assertEquals("is a ", str);
 str = reader.consumeTo('r');
 Assert.assertEquals("is a Character", str);
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 Assert.assertEquals("acter reader .", str);
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("is", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 Assert.assertEquals("is", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("1s", str);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('s'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("is"));
 Assert.assertFalse(reader.matches("isn't"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 Assert.assertTrue(reader.matchesIgnoreCase("is"));
 Assert.assertTrue(reader.matchesIgnoreCase("Is"));
 Assert.assertFalse(reader.matchesIgnoreCase("isn't"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny(' ', 's'));
 Assert.assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsumeString() {
 String str = reader.matchConsume("is");
 Assert.assertEquals("is", str);
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String str = reader.matchConsumeIgnoreCase("Is");
 Assert.assertEquals("is", str);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("is"));
 Assert.assertTrue(reader.containsIgnoreCase("Is"));
 Assert.assertFalse(reader.containsIgnoreCase("isn't"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullInput() {
 new CharacterReader(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEndOfInput() {
 reader.consumeToEnd();
 reader.consume(); // this should throw an exception
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndAfterMark() {
 reader.mark();
 reader.advance();
 reader.consumeToEnd(); // this should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEndNegativeOffset() {
 reader.consumeToEnd();
 reader.consumeTo(-1); // this should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNullSeq() {
 reader.consumeTo(null); // this should throw an exception
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndEmptySeq() {
 reader.consumeTo(""); // this should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToAnyNullSeq() {
 reader.consumeToAny(null); // this should throw an exception
 }

 @Test
 public void testConsumeToAnyEmptySeq() {
 String str = reader.consumeToAny();
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeToAnySingleCharSeq() {
 String str = reader.consumeToAny('s');
 Assert.assertEquals("s", str);
 }

 @Test
 public void testConsumeToAnyMultipleCharsSeq() {
 String str = reader.consumeToAny('s', 'p');
 Assert.assertEquals("sp", str);
 }

 @Test
 public void testConsumeToAnyCharNotInSeq() {
 String str = reader.consumeToAny('z');
 Assert.assertEquals("", str);
 }
}