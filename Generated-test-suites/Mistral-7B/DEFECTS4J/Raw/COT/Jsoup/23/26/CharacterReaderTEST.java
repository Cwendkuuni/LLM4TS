import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] inputChars = new char[]{'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r', '\n'};
 String input = new String(inputChars);
 characterReader = new CharacterReader(input);
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader(null);
 Assert.assertNull(reader);
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
 }

 @Test
 public void testConsumeString() {
 String expected = "test is a ";
 String actual = characterReader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToCharacter() {
 String expected = "a ";
 String actual = characterReader.consumeTo(' ').trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToString() {
 String expected = "CharacterReader reader ";
 String actual = characterReader.consumeTo(" reader ".getBytes()).trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 String expected = "CharacterReader";
 String actual = characterReader.consumeToAny('C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r').trim();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "reader.\n";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "test";
 String actual = characterReader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "Character";
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
 public void testMatchesCharacter() {
 Assert.assertTrue(characterReader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(characterReader.matches("test"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("Test"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(characterReader.matchesAny('a', ' ', 'r', 'e', 'd'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 String expected = "CharacterReader";
 characterReader.matchConsume("CharacterReader");
 Assert.assertEquals(expected.length(), characterReader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String expected = "CharacterReader";
 characterReader.matchConsumeIgnoreCase("characterreader");
 Assert.assertEquals(expected.length(), characterReader.pos());
 }

 @Test
 public void testContainsIgnoreCaseString() {
 Assert.assertTrue(characterReader.containsIgnoreCase("reader"));
 }

 @Test
 public void testToString() {
 String expected = "reader.\n";
 String actual = characterReader.toString();
 Assert.assertEquals(expected, actual);
 }

}