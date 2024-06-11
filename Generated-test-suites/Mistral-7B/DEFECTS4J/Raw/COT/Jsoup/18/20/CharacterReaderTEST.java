import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'c', 'l', 'a', 's', 's', ' ', 'f', 'o', 'r', ' ', 't', 'e', 's', 't', ' ', 'c', 'a', 's', 'e', ' ', 'o', 'f', ' ', 'f', 'i', 'n'};
 stringTest = new String(testInput);
 characterReader = new CharacterReader(stringTest);
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader("test input");
 assertTrue(characterReader != null);
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 assertEquals('t', characterReader.current());
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testUnconsume() {
 characterReader.unconsume();
 assertEquals('t', characterReader.current());
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testRewindToMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("test", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("is a", characterReader.consumeTo(' '));
 assertEquals("test class", characterReader.consumeTo(" class"));
 assertEquals("test", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("test cla", characterReader.consumeToAny('c', 's'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEnd_whenAtEnd() {
 characterReader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("test", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("test", characterReader.consumeHexSequence()); // this method doesn't consume characters
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('t'));
 assertFalse(characterReader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("is"));
 assertFalse(characterReader.matches("test class"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("is"));
 assertFalse(characterReader.matchesIgnoreCase("test class"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('t', 'e'));
 assertFalse(characterReader.matchesAny('a', 'z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 assertTrue(characterReader.matchConsume("is"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertTrue(characterReader.matchConsumeIgnoreCase("is"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("test"));
 assertTrue(characterReader.containsIgnoreCase("class"));
 }

 @Test
 public void testToString() {
 assertEquals(" test class", characterReader.toString());
 }
}