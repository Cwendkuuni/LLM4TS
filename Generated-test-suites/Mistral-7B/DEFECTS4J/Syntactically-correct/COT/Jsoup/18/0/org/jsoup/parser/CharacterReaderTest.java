import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @SuppressWarnings("static-access")
 @Before
 public void setUp() {
 char[] testInput = new char[]{'a', 'b', 'c', 'd'};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 Assert.assertEquals('a', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "ab";
 String actual = characterReader.consumeAsString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithNoChar() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String expected = "";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithSingleChar() {
 characterReader.consume();
 String expected = "";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "bc";
 String actual = characterReader.consumeTo('c');
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToWithCharNotInString() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String expected = "";
 String actual = characterReader.consumeTo('x');
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 char[] expectedSeq = new char[]{'b', 'c'};
 String actual = characterReader.consumeToAny(expectedSeq[0], expectedSeq[1]);
 String expected = new String(expectedSeq);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAnyWithOnlyOneChar() {
 char[] expectedSeq = new char[]{'a'};
 String actual = characterReader.consumeToAny(expectedSeq[0]);
 String expected = new String(expectedSeq);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToAnyWithNoMatchingChar() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 String actual = characterReader.consumeToAny('x');
 Assert.assertEquals("", actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "dc";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "ab";
 String actual = characterReader.consumeLetterSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "ab";
 String actual = characterReader.consumeHexSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "12";
 String actual = characterReader.consumeDigitSequence();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 characterReader.consume();
 boolean actual = characterReader.matches('b');
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesWithNonMatchingChar() {
 characterReader.consume();
 boolean actual = characterReader.matches('x');
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesString() {
 characterReader.consume();
 characterReader.consume();
 String expected = "ab";
 String actual = characterReader.consumeAsString();
 boolean result = characterReader.matches(expected);
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader.consume();
 characterReader.consume();
 String expected = "Ab";
 boolean actual = characterReader.matchesIgnoreCase(expected);
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, actual);
 }

 @Test
 public void testMatchesAny() {
 characterReader.consume();
 boolean actual = characterReader.matchesAny('a', 'b');
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesLetter() {
 characterReader.consume();
 boolean actual = characterReader.matchesLetter();
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchesDigit() {
 characterReader.consume();
 boolean actual = characterReader.matchesDigit();
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMatchConsume() {
 String expected = "ab";
 boolean actual = characterReader.matchConsume("ab");
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, actual);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "Ab";
 boolean actual = characterReader.matchConsumeIgnoreCase("Ab");
 boolean expectedResult = true;
 Assert.assertEquals(expectedResult, actual);
 }

 @Test
 public void testContainsIgnoreCase() {
 String input = "abcdefghijk";
 CharacterReader reader = new CharacterReader(input);
 boolean actual = reader.containsIgnoreCase("ef");
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "dc";
 String actual = characterReader.toString();
 Assert.assertEquals(expected, actual);
 }
}