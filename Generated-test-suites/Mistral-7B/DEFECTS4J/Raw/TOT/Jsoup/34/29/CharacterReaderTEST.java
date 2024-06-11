import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testReaderWithOneCharacter() {
 CharacterReader reader = new CharacterReader("a");
 assertFalse(reader.isEmpty());
 assertEquals("a", reader.consumeAsString());
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");
 assertFalse(reader.isEmpty());
 assertEquals("abc", reader.consumeToEnd());
 }

 @Test
 public void testReaderMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume();
 reader.unconsume();
 assertEquals("abcd", reader.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReaderNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testReaderNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertEquals(1, reader.nextIndexOf('a'));
 assertEquals(4, reader.nextIndexOf('c'));
 assertEquals(-1, reader.nextIndexOf('e'));
 }

 @Test
 public void testReaderNextIndexOfString() {
 CharacterReader reader = new CharacterReader("aaabccccaaa");
 assertEquals(1, reader.nextIndexOf("aa"));
 assertEquals(6, reader.nextIndexOf("ccc"));
 assertEquals(-1, reader.nextIndexOf("ddd"));
 }

 @Test
 public void testReaderConsumeToChar() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertEquals("aa", reader.consumeTo('a').toString());
 assertEquals("ccc", reader.consumeTo('c').toString());
 }

 @Test
 public void testReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertEquals("aaa", reader.consumeTo("aaa").toString());
 }

 @Test
 public void testReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("1234567890ABCDEFabcdEfgh");
 assertEquals("123", reader.consumeToAny('1', '2', '3').toString());
 }

 @Test
 public void testReaderMatches() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches("aa"));
 assertFalse(reader.matches('c'));
 assertFalse(reader.matches("ccc"));
 }

 @Test
 public void testReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("AAABCCC");
 assertTrue(reader.matchesIgnoreCase("aa"));
 assertTrue(reader.matchesIgnoreCase("CCC"));
 assertFalse(reader.matchesIgnoreCase("ee"));
 }

 @Test
 public void testReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("1234567890ABCDEFabcdEfgh");
 assertTrue(reader.matchesAny('1', '2', '3'));
 assertFalse(reader.matchesAny('e', 'f'));
 }

 @Test
 public void testReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetterSequence().equals("123"));
 }

 @Test
 public void testReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890ABCDEFabcdEfgh");
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigitSequence().equals("abc"));
 }

 @Test
 public void testReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("aaabcccc");
 assertTrue(reader.matchConsume("aa"));
 assertFalse(reader.matchConsume("ccc"));
 }

 @Test
 public void testReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("AAABCCC");
 assertTrue(reader.matchConsumeIgnoreCase("aa"));
 assertFalse(reader.matchConsumeIgnoreCase("CCC"));
 }

 @Test
 public void testReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");
 assertTrue(reader.containsIgnoreCase("title"));
 assertFalse(reader.containsIgnoreCase("headhead"));
 }
}