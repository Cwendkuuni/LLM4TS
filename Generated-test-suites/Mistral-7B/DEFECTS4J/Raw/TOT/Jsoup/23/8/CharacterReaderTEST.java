import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // Test isEmpty method
 Assert.assertTrue(reader.isEmpty());
 // Test current and consume methods
 Assert.assertEquals(-1, reader.current());
 Assert.assertEquals(-1, reader.consume());
 }

 @Test
 public void testSimpleConsume() {
 CharacterReader reader = new CharacterReader("Hello World!");
 // Test pos and isEmpty methods
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 // Test current and consume methods
 Assert.assertEquals('H', reader.current());
 Assert.assertEquals('H', reader.consume());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcdef");
 // Test consumeAsString method
 Assert.assertEquals("abc", reader.consumeAsString());
 // Test consumeTo method with a single character
 Assert.assertEquals("def", reader.consumeTo('f').trim());
 // Test consumeTo method with a string
 Assert.assertEquals("abc", reader.consumeTo("cd".toCharArray()).trim());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("12345");
 // Test consumeToEnd method
 Assert.assertEquals("5", reader.consumeToEnd().trim());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("12345");
 // Test consume past the end of the input
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abCdefGhIjKl");
 // Test consumeLetterSequence method
 Assert.assertEquals("abC", reader.consumeLetterSequence().trim());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 // Test consumeHexSequence method
 Assert.assertEquals("0123", reader.consumeHexSequence().trim());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("0123456789");
 // Test consumeDigitSequence method
 Assert.assertEquals("0123", reader.consumeDigitSequence().trim());
 }

 @Test
 public void testMatch() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 // Test matches method with a single character
 Assert.assertTrue(reader.matches('d'));
 // Test matches method with a string
 Assert.assertTrue(reader.matches("abcd".toCharArray()));
 }

 @Test
 public void testMatchIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCdEfGhIjKl");
 // Test matchesIgnoreCase method with a single character
 Assert.assertTrue(reader.matchesIgnoreCase('d'));
 // Test matchesIgnoreCase method with a string
 Assert.assertTrue(reader.matchesIgnoreCase("abcd".toCharArray()));
 }

 @Test
 public void testMatchAny() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 // Test matchesAny method with a single character
 Assert.assertTrue(reader.matchesAny('1'.toCharArray()));
 // Test matchesAny method with an array of characters
 Assert.assertTrue(reader.matchesAny(new char[]{'1', '5'}));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(" <html><head><title>Test Title</title></head><body>");
 // Test containsIgnoreCase method
 Assert.assertTrue(reader.containsIgnoreCase("<title>".toCharArray()));
 }
}