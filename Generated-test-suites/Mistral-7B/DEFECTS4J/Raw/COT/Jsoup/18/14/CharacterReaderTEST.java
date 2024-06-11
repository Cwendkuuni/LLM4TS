import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] input = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader(null);
 Assert.assertNull(reader);
 reader = new CharacterReader("");
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
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
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals(reader.current(), 'h');
 reader.consume();
 Assert.assertEquals(reader.current(), 'e');
 }

 @Test
 public void testConsume() {
 reader.consume();
 reader.consume();
 Assert.assertEquals(reader.current(), 'l');
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals(reader.current(), 'l');
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals(reader.pos(), 2);
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals(reader.current(), 'h');
 }

 @Test
 public void testConsumeAsString() {
 String result = reader.consumeAsString();
 Assert.assertEquals(result, "he");
 }

 @Test
 public void testConsumeTo() {
 String result = reader.consumeTo('l');
 Assert.assertEquals(result, "he");
 result = reader.consumeTo('o');
 Assert.assertEquals(result, "hello");
 }

 @Test
 public void testConsumeToEnd() {
 String result = reader.consumeToEnd();
 Assert.assertEquals(result, "world");
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 Assert.assertEquals(result, "hello");
 }

 @Test
 public void testConsumeHexSequence() {
 String result = reader.consumeHexSequence();
 Assert.assertEquals(result, "world");
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = reader.consumeDigitSequence();
 Assert.assertEquals(result, "012345");
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('h'));
 reader.consume();
 Assert.assertFalse(reader.matches('h'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("he"));
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matches("he"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("He"));
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesIgnoreCase("He"));
 }

 @Test
 public void testMatchesAny() {
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
 reader.consume();
 Assert.assertFalse(reader.matchesAny(new char[]{'l', 'o'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String result = reader.consumeTo("ll");
 Assert.assertEquals(result, "hell");
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String result = reader.consumeToIgnoreCase("HeLl");
 Assert.assertEquals(result, "hello");
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("HELLO"));
 Assert.assertTrue(reader.containsIgnoreCase("worLd"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithEmptySeq() {
 reader.consumeTo("");
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToWithSeqLongerThanRemainingInput() {
 reader.consumeTo("worldl");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToAnyWithEmptySeq() {
 reader.consumeToAny(new char[]{});
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumeToAnyWithSeqLongerThanRemainingInput() {
 reader.consumeToAny(new char[]{'l', 'o', 'r', 'd'});
 }
}