import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] inputArray = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r', '.'};
 String input = new String(inputArray);
 characterReader = new CharacterReader(input);
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader(null);
 Assert.assertNull(characterReader.input);
 characterReader = new CharacterReader("");
 Assert.assertEquals("", characterReader.input);
 characterReader = new CharacterReader("test");
 Assert.assertEquals("test", characterReader.input);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.advance();
 characterReader.advance();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.advance();
 characterReader.advance();
 Assert.assertEquals(2, characterReader.pos());
 characterReader.rewindToMark();
 Assert.assertEquals(0, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 characterReader.advance();
 Assert.assertEquals(' ', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 }

 @Test
 public void testConsume() {
 Assert.assertEquals('t', characterReader.consume());
 Assert.assertEquals('e', characterReader.current());
 characterReader.consume();
 Assert.assertEquals(' ', characterReader.current());
 }

 @Test
 public void testUnconsume() {
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
 characterReader.advance();
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals("st", consumed);
 characterReader.advance();
 consumed = characterReader.consumeAsString();
 Assert.assertEquals("C", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo(' ');
 Assert.assertEquals("test ", consumed);
 consumed = characterReader.consumeTo('r');
 Assert.assertEquals("test Re", consumed);
 consumed = characterReader.consumeTo("ead.");
 Assert.assertEquals("test Read.", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToInvalidArgument() {
 characterReader.consumeTo('0');
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("CharacterReader@7852e932", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("Read", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("123", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertFalse(characterReader.matches(' '));
 characterReader.advance();
 Assert.assertTrue(characterReader.matches('e'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("te"));
 Assert.assertFalse(characterReader.matchesIgnoreCase(" "));
 characterReader.advance();
 Assert.assertTrue(characterReader.matchesIgnoreCase("e"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(characterReader.matchesAny('t', 'e'));
 Assert.assertFalse(characterReader.matchesAny(' '));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesLetterWhenEmpty() {
 characterReader.matchesLetter();
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesDigitWhenEmpty() {
 characterReader.matchesDigit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchConsumeInvalidArgument() {
 characterReader.matchConsume(null);
 }

 @Test
 public void testMatchConsume() {
 String consumed = characterReader.matchConsume("es");
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = characterReader.matchConsumeIgnoreCase("Es");
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("es"));
 Assert.assertTrue(characterReader.containsIgnoreCase("REaD."));
 Assert.assertFalse(characterReader.containsIgnoreCase("ABC"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("CharacterReader@7852e932", characterReader.toString());
 }
}