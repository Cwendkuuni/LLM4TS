import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Test
 public void testCharacterReaderConstructor() {
 char[] input = {'t', 'e', 's', 't', '\u0000'}; // checking null input with a zero length input and an input with an invalid character
 characterReader = new CharacterReader(new String(input));

 Assert.assertNotNull(characterReader);
 Assert.assertEquals(5, characterReader.length());
 Assert.assertEquals(0, characterReader.pos());
 Assert.assertFalse(characterReader.isEmpty());
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testIsEmpty() {
 characterReader = new CharacterReader("test");

 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();

 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testCurrentEOF() {
 characterReader = new CharacterReader("test");
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();

 Assert.assertEquals(EOF, characterReader.current()); // test for consuming beyond the end of the input
 }

 @Test
 public void testConsume() {
 characterReader = new CharacterReader("test");

 Assert.assertEquals('t', characterReader.consume());
 Assert.assertEquals('e', characterReader.consume());
 Assert.assertEquals('s', characterReader.consume());
 Assert.assertEquals('t', characterReader.consume());
 Assert.assertEquals(EOF, characterReader.consume());
 }

 @Test
 public void testConsumeString() {
 characterReader = new CharacterReader("test");

 Assert.assertEquals("test", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 characterReader = new CharacterReader("test123");

 Assert.assertEquals("123", characterReader.consumeTo('3'));
 }

 @Test
 public void testConsumeToEnd() {
 characterReader = new CharacterReader("test123");

 Assert.assertEquals("test123", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 characterReader = new CharacterReader("test123AbCdEf");

 Assert.assertEquals("test", characterReader.consumeLetterSequence());
 Assert.assertEquals("AbC", characterReader.consumeLetterSequence());
 Assert.assertEquals("dEf", characterReader.consumeLetterSequence());
 Assert.assertEquals(EOF, characterReader.consumeLetterSequence().charAt(0));
 }

 @Test
 public void testConsumeHexSequence() {
 characterReader = new CharacterReader("test123AbCdEf1234");

 Assert.assertEquals("1234", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 characterReader = new CharacterReader("test123AbCdEf1234");

 Assert.assertEquals("123", characterReader.consumeDigitSequence());
 Assert.assertEquals("4", characterReader.consumeDigitSequence().charAt(0));
 }

 @Test(expected = AssertionError.class)
 public void testConsumeInvalidSequence1() {
 characterReader = new CharacterReader("test123AbCdEf1234");

 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeHexSequence();
 characterReader.consumeHexSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();

 Assert.assertEquals(EOF, characterReader.current()); // test for consuming more than available sequences
 }

 @Test(expected = AssertionError.class)
 public void testConsumeInvalidSequence2() {
 characterReader = new CharacterReader("test123AbCdEf1234");

 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 characterReader.consumeHexSequence();
 characterReader.consumeHexSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeDigitSequence();
 characterReader.consumeLetter(); // test for consuming a letter when not available
 }

 @Test
 public void testMatches() {
 characterReader = new CharacterReader("testtest");

 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertTrue(characterReader.matches('e'));
 Assert.assertTrue(characterReader.matches('s'));
 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertFalse(characterReader.matches('x'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader = new CharacterReader("testTEST");

 Assert.assertTrue(characterReader.matchesIgnoreCase("test"));
 Assert.assertTrue(characterReader.matchesIgnoreCase("TEST"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("XYZ"));
 }

 @Test
 public void testMatchesAny() {
 characterReader = new CharacterReader("testAbCdE");

 Assert.assertTrue(characterReader.matchesAny('a', 'b', 'c', 'd', 'e'));
 Assert.assertFalse(characterReader.matchesAny('f'));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesAnyInvalidSequence() {
 characterReader = new CharacterReader("testAbCdE");

 characterReader.matchesAny('a', 'b', 'c', 'd', 'e', 'f', 'g'); // test for consuming more characters than available
 }

 @Test
 public void testContainsIgnoreCase() {
 characterReader = new CharacterReader("test<title>Hello</title>World");

 Assert.assertTrue(characterReader.containsIgnoreCase("<title>"));
 Assert.assertTrue(characterReader.containsIgnoreCase("world"));
 Assert.assertFalse(characterReader.containsIgnoreCase("XYZ"));
 }
}