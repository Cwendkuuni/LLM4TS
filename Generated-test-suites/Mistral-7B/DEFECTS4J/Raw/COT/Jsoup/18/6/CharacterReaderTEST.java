import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] input = {'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'J', 'a', 'v', 'a', ' ', 'S', 'c', 'h', 'e', 'm', 'a', ' ', 'f', 'o', 'r', ' ', 't', 'e', 's', 't', '.'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader(null);
 Assert.assertTrue("Input should not be null", characterReader instanceof CharacterReader);
 characterReader = new CharacterReader("");
 Assert.assertEquals("Initial position should be 0", 0, characterReader.pos());
 characterReader = new CharacterReader("abc");
 Assert.assertEquals("Input length should match the length of the input string", 3, characterReader.length());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse("Initial state should not be empty", characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue("State should be empty after consuming 3 characters", characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals("Initial current character should be 't'", 't', characterReader.current());
 characterReader.consume();
 Assert.assertEquals("Current character after consuming 'h' should be 'i'", 'i', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondEndOfInput() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume(); // should throw an exception
 }

 @Test
 public void testConsumeAsString() {
 String expected = "his";
 String actual = characterReader.consumeAsString();
 Assert.assertEquals("ConsumerAsString should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "is ";
 characterReader.advance(); // skip 't'
 String actual = characterReader.consumeTo(' ');
 Assert.assertEquals("ConsumerTo should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeToStringSequence() {
 String expected = "is";
 String actual = characterReader.consumeTo(" is ");
 Assert.assertEquals("ConsumerTo should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeToAny() {
 char[] expected = {'a', ' ', 'J', 'a', 'v', 'a'};
 char[] actual = characterReader.consumeToAny('a', ' ', 'J', 'a', 'v', 'a').toCharArray();
 Assert.assertArrayEquals("ConsumerToAny should return expected substring array", expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "for tests.";
 String actual = characterReader.consumeToEnd();
 Assert.assertEquals("ConsumerToEnd should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "scheme";
 String actual = characterReader.consumeLetterSequence();
 Assert.assertEquals("ConsumerLetterSequence should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "Java";
 String actual = characterReader.consumeHexSequence();
 Assert.assertEquals("ConsumerHexSequence should return expected substring", expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "4";
 String actual = characterReader.consumeDigitSequence();
 Assert.assertEquals("ConsumerDigitSequence should return expected substring", expected, actual);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue("Matcher should match 's' at position 5", characterReader.matches('s'));
 Assert.assertFalse("Matcher should not match 'z'", characterReader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue("Matcher should match 'is'", characterReader.matches("is"));
 Assert.assertFalse("Matcher should not match 'is not'", characterReader.matches("is not"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue("Matcher should match 'Is'", characterReader.matchesIgnoreCase("is"));
 Assert.assertFalse("Matcher should not match 'is not'", characterReader.matchesIgnoreCase("is not"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue("Matcher should match 'J'", characterReader.matchesAny('J'));
 Assert.assertFalse("Matcher should not match 'z'", characterReader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue("Matcher should match 'i'", characterReader.matchesLetter());
 Assert.assertFalse("Matcher should not match '4'", characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse("Matcher should not match 'i'", characterReader.matchesDigit());
 Assert.assertTrue("Matcher should match '4'", characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "Java Scheme for tests.";
 String actual = characterReader.matchConsume("Java Scheme for tests.");
 Assert.assertEquals("Matcher should match and consume expected substring", expected, actual);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "Java Scheme for tests.";
 String actual = characterReader.matchConsumeIgnoreCase("Java Scheme for tests.");
 Assert.assertEquals("Matcher should match and consume expected substring", expected, actual);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue("Input should contain 'for tests'.", characterReader.containsIgnoreCase("for tests"));
 Assert.assertFalse("Input should not contain 'is not for tests'.", characterReader.containsIgnoreCase("is not for tests"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMarkBeforeStart() {
 characterReader.mark();
 characterReader.rewindToMark();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 characterReader.advance();
 characterReader.advance();
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
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 }

}