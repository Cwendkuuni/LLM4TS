import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @SuppressWarnings("static-access")
 @Before
 public void setup() {
 char[] inputChars = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 characterReader = new CharacterReader(new String(inputChars));
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondEnd() {
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
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals("tes", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('C');
 Assert.assertEquals("te", consumed);
 consumed = characterReader.consumeTo('c');
 Assert.assertEquals("tes", consumed);
 consumed = characterReader.consumeToEnd();
 Assert.assertEquals("tes Chart Reader", consumed);
 }

 @Test
 public void testConsumeToAny() {
 characterReader.consumeToAny('c', 'r');
 Assert.assertEquals("tes", characterReader.consumeAsString());
 characterReader.consumeToAny('a', 'b');
 Assert.assertEquals("tes Chart Reader", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("tes Chart Reader", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("test", consumed);
 characterReader.consumeLetterSequence();
 Assert.assertEquals("Chart", characterReader.consumeLetterSequence());
 characterReader.consumeLetterSequence();
 Assert.assertEquals("Reader", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("Chart", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("3", consumed);
 characterReader.consumeDigitSequence();
 Assert.assertEquals("5", characterReader.consumeDigitSequence());
 characterReader.consumeDigitSequence();
 Assert.assertEquals("7", characterReader.consumeDigitSequence());
 characterReader.consumeDigitSequence();
 Assert.assertEquals("", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("es"));
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 Assert.assertFalse(characterReader.matches("es"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 Assert.assertTrue(characterReader.matchesIgnoreCase("es"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(characterReader.matchesAny('t', 'e'));
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesAny('t', 'e'));
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 Assert.assertTrue(characterReader.matchesAny('r', 'e'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consumeLetterSequence();
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(characterReader.matchesDigit());
 characterReader.consumeDigitSequence();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String consumed = characterReader.matchConsume("es");
 Assert.assertEquals("es", consumed);
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 consumed = characterReader.matchConsume("es");
 Assert.assertEquals("es", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 String consumed = characterReader.matchConsumeIgnoreCase("es");
 Assert.assertEquals("es", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("tes"));
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 Assert.assertFalse(characterReader.containsIgnoreCase("tes"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeWithNegativeMark() {
 characterReader.mark(-1);
 characterReader.rewindToMark();
 }
}