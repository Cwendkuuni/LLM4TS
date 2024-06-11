import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] input = {'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 this.characterReader = new CharacterReader(new String(input));
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
 Assert.assertEquals(1, characterReader.pos());
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
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "test ";
 String actual = characterReader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "C";
 String actual = characterReader.consumeTo(expected.charAt(0)).trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "Reader";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "Character";
 String actual = characterReader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "RED";
 String actual = characterReader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String actual = characterReader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertFalse(characterReader.matches('E'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("te"));
 Assert.assertFalse(characterReader.matches("de"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("te"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("De"));
 }

 @Test
 public void testMatchesAny() {
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesAny('e', 'r'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "tes";
 characterReader.consumeLetterSequence();
 characterReader.consumeLetter();
 characterReader.consumeLetter();
 String actual = characterReader.matchConsume("tes").trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "tes";
 characterReader.consumeLetterSequence();
 characterReader.consumeLetter();
 characterReader.consumeLetter();
 characterReader.unconsume();
 characterReader.consume('T');
 characterReader.consume('e');
 characterReader.consume('s');
 String actual = characterReader.matchConsumeIgnoreCase("tes").trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("red"));
 Assert.assertFalse(characterReader.containsIgnoreCase("blue"));
 }

 @Test
 public void testToString() {
 String expected = "Reader";
 String actual = characterReader.toString();
 Assert.assertEquals(expected, actual);
 }
}