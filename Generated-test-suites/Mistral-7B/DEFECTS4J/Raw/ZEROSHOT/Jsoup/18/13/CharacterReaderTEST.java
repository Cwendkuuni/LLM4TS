import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testsInput = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
 reader = new CharacterReader(new String(testsInput));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
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
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 String consumed = reader.consumeAsString();
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("ab", consumed);
 Assert.assertEquals('c', reader.current());

 consumed = reader.consumeTo('e');
 Assert.assertEquals("abcde", consumed);
 Assert.assertEquals('f', reader.current());
 }

 @Test
 public void testConsumeToAny() {
 char[] seq = new char[]{'a', 'b', 'c'};
 String consumed = reader.consumeToAny(seq);
 Assert.assertEquals("a", consumed);
 Assert.assertEquals('b', reader.current());

 consumed = reader.consumeToAny(new char[]{'d', 'e'});
 Assert.assertEquals("bcde", consumed);
 Assert.assertEquals('f', reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcd", consumed);
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("1234", consumed);
 Assert.assertEquals('f', reader.current());
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("123", consumed);
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("cd"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'a', 'b', 'c'};
 Assert.assertTrue(reader.matchesAny(seq));
 Assert.assertFalse(reader.matchesAny(new char[]{'d', 'e'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter());
 reader.consume();
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit());
 reader.consume();
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume("ab");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("Ab");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("ab"));
 Assert.assertFalse(reader.containsIgnoreCase("cd"));
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals(0, reader.pos());
 }
}