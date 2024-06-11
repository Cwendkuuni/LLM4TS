import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Test
 public void testCharacterReaderConstructor() {
 char[] expectedInput = {'t', 'e', 'st', '\n'};
 characterReader = new CharacterReader(new String(expectedInput));
 Assert.assertArrayEquals(expectedInput, characterReader.input.toCharArray());
 Assert.assertEquals(4, characterReader.length());
 }

 @Test
 public void testPos() {
 characterReader = new CharacterReader("test");
 Assert.assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 characterReader = new CharacterReader("");
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 characterReader = new CharacterReader("test");
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader = new CharacterReader("test");
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals('s', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader = new CharacterReader("test");
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader = new CharacterReader("test");
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader = new CharacterReader("test");
 characterReader.mark();
 characterReader.consume();
 Assert.assertEquals(0, characterReader.pos());
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 characterReader = new CharacterReader("test");
 Assert.assertEquals("st", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 characterReader = new CharacterReader("test123");
 Assert.assertEquals("123", characterReader.consumeTo('3'));
 }

 @Test
 public void testConsumeToString() {
 characterReader = new CharacterReader("test123");
 Assert.assertEquals("test", characterReader.consumeTo("est"));
 }

 @Test
 public void testConsumeToAny() {
 characterReader = new CharacterReader("test123");
 Assert.assertEquals("est", characterReader.consumeToAny('s', 't'));
 }

 @Test
 public void testConsumeToEnd() {
 characterReader = new CharacterReader("test");
 Assert.assertEquals("test", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 characterReader = new CharacterReader("test");
 Assert.assertEquals("est", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 characterReader = new CharacterReader("test#0x1a");
 Assert.assertEquals("0x1a", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 characterReader = new CharacterReader("test123");
 Assert.assertEquals("123", characterReader.consumeDigitSequence());
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithEmptyInput() {
 characterReader = new CharacterReader("");
 Assert.assertFalse(characterReader.matches('a'));
 }

 @Test
 public void testMatches() {
 characterReader = new CharacterReader("test");
 Assert.assertTrue(characterReader.matches('t'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 characterReader = new CharacterReader("Test");
 Assert.assertTrue(characterReader.matchesIgnoreCase("test"));
 }

 @Test
 public void testMatchesAny() {
 characterReader = new CharacterReader("test");
 Assert.assertTrue(characterReader.matchesAny('s', 't'));
 }

 @Test
 public void testMatchesLetter() {
 characterReader = new CharacterReader("test");
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 characterReader = new CharacterReader("test123");
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 characterReader = new CharacterReader("test");
 Assert.assertTrue(characterReader.matchConsume("est"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 characterReader = new CharacterReader("Test");
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase("test"));
 }

 @Test
 public void testContainsIgnoreCase() {
 characterReader = new CharacterReader("test<title>Title</title>");
 Assert.assertTrue(characterReader.containsIgnoreCase("title"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConsumePastEndOfInput() {
 characterReader = new CharacterReader("test");
 characterReader.consumeToEnd();
 characterReader.consume();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEndWithNegativeLength() {
 characterReader = new CharacterReader("test");
 characterReader.consumeToEnd(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToWithNegativeOffset() {
 characterReader = new CharacterReader("test123");
 characterReader.consumeTo(-1, '3');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeToWithOffsetGreaterThanLength() {
 characterReader = new CharacterReader("test");
 characterReader.consumeTo(10, 't');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToWithNullSequence() {
 characterReader = new CharacterReader("test");
 characterReader.consumeTo(null);
 }
}