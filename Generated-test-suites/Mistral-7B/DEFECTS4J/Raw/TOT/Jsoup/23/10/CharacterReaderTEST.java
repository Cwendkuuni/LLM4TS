import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");

 // Test isEmpty method
 Assert.assertTrue(reader.isEmpty());

 // Test current, consume, and pos methods
 Assert.assertEquals(-1, reader.current());
 Assert.assertEquals(-1, reader.consume());
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testSimpleCharacterConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");

 // Test matches method with single character
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('b'));

 // Test consume method
 reader.consume();
 Assert.assertTrue(reader.matches('b'));

 // Test consumeTo method with single character
 String consumed = reader.consumeTo('c');
 Assert.assertEquals("ab", consumed);
 Assert.assertEquals('c', reader.current());

 // Test consumeToEnd method
 consumed = reader.consumeToEnd();
 Assert.assertEquals("def", consumed);
 }

 @Test
 public void testComplexCharacterConsumption() {
 CharacterReader reader = new CharacterReader("abc123ABC!@#$%");

 // Test consumeLetterSequence method
 String sequence = reader.consumeLetterSequence();
 Assert.assertEquals("abc", sequence);

 // Test consumeHexSequence method
 sequence = reader.consumeHexSequence();
 Assert.assertEquals("12", sequence);

 // Test consumeDigitSequence method
 sequence = reader.consumeDigitSequence();
 Assert.assertEquals("123", sequence);

 // Test consumeToAny method with multiple characters
 sequence = reader.consumeToAny('A', 'B');
 Assert.assertEquals("ABC", sequence);

 // Test consumeToEnd method
 sequence = reader.consumeToEnd();
 Assert.assertEquals("!@#$%", sequence);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 // Test constructor with null input
 CharacterReader reader = new CharacterReader(null);
 }
}