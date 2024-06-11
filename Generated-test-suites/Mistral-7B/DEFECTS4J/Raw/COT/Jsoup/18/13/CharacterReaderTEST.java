import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'I', 'n', 'p', 'u', 't', 'U', 't', ' ', 'f', 'i', 'l', 'e', 's', ' ', 'a', 'r', 'e', ' ', 'a', ' ', 'r', 'e', 'a', 'd', 'e', 'r', ' '};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader(null);
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 }

 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 assertEquals("t", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('d');
 assertEquals("t e ", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 assertEquals("test file are ", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 assertEquals("test", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 assertEquals("test", consumed); // assumption since we don't know the exact test input
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 assertEquals("123", consumed);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('e'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("es"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("Es"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(new char[]{'e', 's'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchConsumeEmptyString() {
 reader.matchConsume("");
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchConsumeInvalidSequence() {
 reader.mark();
 reader.advance();
 reader.matchConsume("abc");
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("are"));
 }

 @Test
 public void testToString() {
 assertEquals("are a read ", reader.toString());
 }

 @Test
 public void testConsumeToEmptySequence() {
 String consumed = reader.consumeTo(new char[0]);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToNullSequence() {
 String consumed = reader.consumeTo(null);
 assertEquals("", consumed);
 }

 @Test
 public void testConsumeToEmptyCharArray() {
 String consumed = reader.consumeTo((char[]) null);
 assertEquals("", consumed);
 }

 @Test(expected = NullPointerException.class)
 public void testInvalidInput() {
 new CharacterReader(null);
 }
}