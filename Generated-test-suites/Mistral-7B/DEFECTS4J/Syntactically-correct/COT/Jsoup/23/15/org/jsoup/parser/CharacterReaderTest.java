import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] inputChars = new char[]{'t', 'e', 's', 't', ' ', 'c', 'o', 'n', 's', 'u', 'm', 'e', ' ', 'o', 'f', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', ' ', 'f', 'o', 'r', ' ', 'm', 'y', ' ', 't', 'e', 's', 't'};
 characterReader = new CharacterReader(new String(inputChars));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.consume();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 characterReader.consume();
 characterReader.consume();
 assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', characterReader.current());
 characterReader.consume();
 assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnconsumePastEnd() {
 characterReader.unconsume();
 characterReader.unconsume();
 characterReader.unconsume();
 characterReader.unconsume();
 characterReader.unconsume();
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("es", characterReader.consumeAsString());
 characterReader.consume();
 characterReader.consume();
 assertEquals("of", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("consume", characterReader.consumeTo(' '));
 assertEquals("test", characterReader.consumeTo(' '));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNegative() {
 characterReader.consumeTo(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToNull() {
 characterReader.consumeTo(null);
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("read for character reader.", characterReader.consumeToEnd());
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 assertEquals("m y   tests", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("test", characterReader.consumeLetterSequence());
 characterReader.consume();
 characterReader.consume();
 assertEquals("of", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("for", characterReader.consumeHexSequence());
 characterReader.consume();
 characterReader.consume();
 assertEquals("my", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("4", characterReader.consumeDigitSequence());
 characterReader.consume();
 characterReader.consume();
 assertEquals("1", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches(' '));
 characterReader.consume();
 assertFalse(characterReader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("es"));
 characterReader.consume();
 characterReader.consume();
 assertFalse(characterReader.matches("es"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("es"));
 characterReader.consume();
 characterReader.consume();
 assertFalse(characterReader.matchesIgnoreCase("es"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny(' ', 'o'));
 characterReader.consume();
 characterReader.consume();
 assertFalse(characterReader.matchesAny(' ', 'o'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 characterReader.consume();
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 characterReader.consume();
 characterReader.consume();
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 assertEquals("of", characterReader.consume(2), "Expected string 'of'");
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("of", characterReader.consumeIgnoreCase("Of"), "Expected string 'of'");
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("test"));
 characterReader.consume();
 characterReader.consume();
 assertFalse(characterReader.containsIgnoreCase("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsIgnoreCaseNull() {
 characterReader.containsIgnoreCase(null);
 }

}