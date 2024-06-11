import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstructors() {
 CharacterReader characterReader = new CharacterReader("testString");
 Assert.assertNotNull(characterReader);
 Assert.assertEquals("testString", characterReader.input);
 }

 @Test
 public void testIsEmpty() {
 reader = new CharacterReader("testString");
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 reader = new CharacterReader("testString");
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 reader = new CharacterReader("testString");
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 reader = new CharacterReader("testString");
 char c = reader.current();
 reader.consume();
 Assert.assertEquals(c, reader.current());
 reader.consume();
 Assert.assertEquals(EOF, reader.current());
 }

 @Test
 public void testUnconsume() {
 reader = new CharacterReader("testString");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader = new CharacterReader("testString");
 reader.advance();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader = new CharacterReader("testString");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 reader = new CharacterReader("testStringtestString");
 Assert.assertEquals("estString", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 reader = new CharacterReader("testStringtestString");
 Assert.assertEquals("test", reader.consumeTo('t'));
 Assert.assertEquals("String", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 reader = new CharacterReader("testString123testString");
 Assert.assertEquals("123", reader.consumeToAny('1', '3'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToEndException() {
 reader = new CharacterReader("testString");
 reader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 reader = new CharacterReader("testStringThisIsATest");
 Assert.assertEquals("ThisIsA", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 reader = new CharacterReader("testString#0x1a#0x2b#");
 Assert.assertEquals("1a", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 reader = new CharacterReader("testString12345678");
 Assert.assertEquals("12345", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 reader = new CharacterReader("testString");
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 reader = new CharacterReader("testStringtestString");
 Assert.assertTrue(reader.matches("estString"));
 Assert.assertFalse(reader.matches("testOtherString"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader = new CharacterReader("testStringTestString");
 Assert.assertTrue(reader.matchesIgnoreCase("teststring"));
 Assert.assertFalse(reader.matchesIgnoreCase("testOtherString"));
 }

 @Test
 public void testMatchesAny() {
 reader = new CharacterReader("testString123testString");
 Assert.assertTrue(reader.matchesAny('1', '3'));
 Assert.assertFalse(reader.matchesAny('4'));
 }

 @Test
 public void testMatchesLetter() {
 reader = new CharacterReader("testStringThisIsATest");
 Assert.assertTrue(reader.matchesLetter());
 reader.consume();
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 reader = new CharacterReader("testString123testString");
 Assert.assertTrue(reader.matchesDigit());
 reader.consume();
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 reader = new CharacterReader("test<html><head><title>Test</title></head>");
 Assert.assertTrue(reader.matchConsume("test"));
 Assert.assertFalse(reader.matchConsume("head"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader = new CharacterReader("test<html><HEAD><TITLE>Test</TITLE></HEAD>");
 Assert.assertTrue(reader.matchConsumeIgnoreCase("test"));
 Assert.assertFalse(reader.matchConsumeIgnoreCase("head"));
 }

 @Test
 public void testContainsIgnoreCase() {
 reader = new CharacterReader("test<html><HEAD><TITLE>Test</TITLE></HEAD>");
 Assert.assertTrue(reader.containsIgnoreCase("test"));
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertFalse(reader.containsIgnoreCase("head"));
 }

 @Test
 public void testToString() {
 reader = new CharacterReader("testString");
 Assert.assertEquals("testString", reader.toString());
 reader.consume();
 Assert.assertEquals("testString", reader.toString());
 }
}