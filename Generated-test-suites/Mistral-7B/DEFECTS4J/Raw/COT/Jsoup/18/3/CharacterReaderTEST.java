import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] inputChars = new char[]{'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r'};
 characterReader = new CharacterReader(new String(inputChars));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(characterReader.isEmpty());
 characterReader.consume();
 Assert.assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals("is a character", consumed.length() == 5);
 Assert.assertEquals("character", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo(' ');
 Assert.assertEquals("is a ", consumed.length() == 3);
 Assert.assertEquals("is a", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("reader", consumed.length() == 6);
 Assert.assertEquals("reader", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("is a", consumed.length() == 3);
 Assert.assertEquals("is a", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("character", consumed.length() == 8);
 Assert.assertEquals("character", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("1", consumed.length() == 1);
 Assert.assertEquals("1", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 Assert.assertTrue(characterReader.matches("is"));
 characterReader.consume();
 Assert.assertFalse(characterReader.matches("is"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 Assert.assertTrue(characterReader.matchesIgnoreCase("is"));
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesIgnoreCase("is"));
 }

 @Test
 public void testMatchesAny() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 Assert.assertTrue(characterReader.matchesAny(' ', 'a'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesAny(' ', 'a'));
 }

 @Test
 public void testMatchesLetter() {
 characterReader.consume('t');
 Assert.assertTrue(characterReader.matchesLetter());
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 characterReader.consume('1');
 Assert.assertTrue(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 Assert.assertTrue(characterReader.matchConsume("is"));
 Assert.assertEquals(3, characterReader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 characterReader.consume('S');
 characterReader.consume('p', 'a', 'c', 'e');
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase("is"));
 Assert.assertEquals(8, characterReader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 characterReader.consume('t');
 characterReader.consume('e');
 characterReader.consume(' ');
 characterReader.consume('r');
 characterReader.consume('e');
 characterReader.consume('a');
 characterReader.consume('d');
 characterReader.consume(' ');
 Assert.assertTrue(characterReader.containsIgnoreCase("reader"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("character reader", characterReader.toString());
 characterReader.consumeToEnd();
 Assert.assertEquals("", characterReader.toString());
 }
}