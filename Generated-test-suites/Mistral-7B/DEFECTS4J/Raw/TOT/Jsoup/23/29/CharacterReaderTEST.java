import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("A");
 reader.consume();
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("ABC");
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithEmptySequence() {
 CharacterReader reader = new CharacterReader("ABC");
 reader.consumeLetterSequence();
 String sequence = reader.consumeToAny();
 Assert.assertEquals("", sequence);
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithSingleCharacterSequence() {
 CharacterReader reader = new CharacterReader("ABC");
 reader.consumeLetterSequence();
 String sequence = reader.consumeTo('C');
 Assert.assertEquals("BC", sequence);
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithMultipleCharacterSequence() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 String sequence = reader.consumeTo("EF");
 Assert.assertEquals("AB", sequence);
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithEndOfInput() {
 CharacterReader reader = new CharacterReader("ABCDEFG");
 reader.consumeToEnd();
 String endOfInput = reader.consumeToEnd();
 Assert.assertEquals("", endOfInput);
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithErrorInput() {
 CharacterReader reader = new CharacterReader("ABCDEFG\n");
 reader.consumeToEnd(); // consume newline character
 String endOfInput = reader.consumeToEnd();
 Assert.assertEquals("", endOfInput);
 TestHelper.assertReaderBehavior(reader);

 reader.mark();
 reader.unconsume();
 reader.advance(); // position should remain at mark
 Assert.assertEquals(0, reader.pos());
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithMixedInput() {
 CharacterReader reader = new CharacterReader("ABC123DEF456GHI");
 String sequence1 = reader.consumeToDigitSequence();
 Assert.assertEquals("ABC123", sequence1);
 String sequence2 = reader.consumeTo('F');
 Assert.assertEquals("DEF456GHI", sequence2);
 TestHelper.assertReaderBehavior(reader);
 }

 @Test
 public void testReaderWithIgnoreCaseSequence() {
 CharacterReader reader = new CharacterReader("ABCabcDEF456GHI");
 String sequence = reader.consumeToIgnoreCase("ef");
 Assert.assertEquals("ABCabcDEF456GHI", reader.toString());
 Assert.assertEquals("ABCabc", sequence);
 TestHelper.assertReaderBehavior(reader);
 }

 private static void TestHelper_assertReaderBehavior(CharacterReader reader) {
 Assert.assertFalse(reader.isEmpty());
 Assert.assertFalse(reader.matches('Z'));
 Assert.assertTrue(reader.matches('A'));
 Assert.assertTrue(reader.matches("AB"));
 Assert.assertTrue(reader.matchesIgnoreCase("ab"));
 Assert.assertTrue(reader.matchesAny('A', 'B'));
 Assert.assertEquals(reader.pos(), reader.mark());
 }

}