import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] inputChars = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
 String input = new String(inputChars);
 characterReader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consumeToEnd();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 Assert.assertEquals(1, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('h', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 char expectedChar = characterReader.current();
 characterReader.consume();
 Assert.assertEquals(expectedChar, characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('h', characterReader.current());
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
 Assert.assertEquals('h', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expectedString = "hello ";
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeAsString();
 Assert.assertEquals("world!", consumed);
 }

 @Test
 public void testConsumeTo() {
 String expectedString = "ello";
 String consumed = characterReader.consumeTo('l');
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeTo('l');
 Assert.assertEquals("ello", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String expectedString = "world!";
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeToEnd();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testConsumeToAny() {
 char[] seekChars = {'l', 'o'};
 String expectedString = "ello";
 String consumed = characterReader.consumeToAny(seekChars);
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeToAny(seekChars);
 Assert.assertEquals("ello", consumed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToAnyNull() {
 characterReader.consumeToAny((char[]) null);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expectedString = "hello";
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("world!", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String expectedString = "helloworld";
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals(expectedString, consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expectedString = "12345";
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals(expectedString, consumed);
 characterReader.consume();
 consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("5", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('h'));
 Assert.assertTrue(characterReader.matches("ello"));
 Assert.assertFalse(characterReader.matches('!'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("ello"));
 Assert.assertTrue(characterReader.matchesIgnoreCase("HELLO"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("world!"));
 }

 @Test
 public void testMatchesAny() {
 char[] sequence = {'l', 'o'};
 Assert.assertTrue(characterReader.matchesAny(sequence));
 Assert.assertFalse(characterReader.matchesAny(new char[] {'!'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String sequence = "ello";
 String expectedString = "world!";
 characterReader.matchConsume(sequence);
 Assert.assertEquals(expectedString, characterReader.consumeToEnd());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String sequence = "HELLO";
 String expectedString = "world!";
 characterReader.matchConsumeIgnoreCase(sequence);
 Assert.assertEquals(expectedString, characterReader.consumeToEnd());
 }

 @Test
 public void testContainsIgnoreCase() {
 String sequence = "title";
 Assert.assertTrue(characterReader.containsIgnoreCase(sequence));
 characterReader.consumeToEnd();
 Assert.assertFalse(characterReader.containsIgnoreCase(sequence));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("world!", characterReader.toString());
 characterReader.consumeToEnd();
 Assert.assertEquals("", characterReader.toString());
 }
}