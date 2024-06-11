import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = {'t', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n', 'g', ' ', 'o', 'f', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("test string");
 assertNotNull(reader.input);
 assertNotNull(reader.length);
 assertNotNull(reader.pos);
 assertNotNull(reader.mark);
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
 assertEquals('s', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('s', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('s', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 assertEquals("test", str);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndWithEmptyString() {
 String str = reader.consumeToEnd();
 fail("Expected IndexOutOfBoundsException, but no exception was thrown.");
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 assertEquals("string of Characters", str);
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 assertEquals("test", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 assertEquals("ofCharacters", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 assertEquals("123", str);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 }

 @Test
 public void testMatchesString() {
 String seq = "est";
 assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 String seq = "EST";
 assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'s', 'r', 'i'};
 assertTrue(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 String seq = "string";
 assertTrue(reader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String seq = "String";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCaseString() {
 String seq = "ofCharacters";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String str = reader.toString();
 assertEquals("string of Characters", str);
 }

}