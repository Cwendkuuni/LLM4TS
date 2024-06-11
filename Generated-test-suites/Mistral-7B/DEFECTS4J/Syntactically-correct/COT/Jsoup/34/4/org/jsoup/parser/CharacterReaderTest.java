import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;
 private final char[] input = new char[]{'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};

 @Before
 public void setUp() {
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(characterReader.isEmpty());
 characterReader.consume();
 Assert.assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 char consumed = characterReader.consume();
 Assert.assertEquals('t', consumed);
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 Assert.assertEquals('s', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 Assert.assertEquals('t', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals("ts", consumed);
 }

 @Test
 public void testNextIndexOf() {
 Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
 Assert.assertEquals(3, characterReader.nextIndexOf('s'));
 }

 @Test
 public void testNextIndexOf_CharSequence() {
 CharSequence seq = new String("is a");
 Assert.assertEquals(-1, characterReader.nextIndexOf(seq));
 Assert.assertEquals(4, characterReader.nextIndexOf(" is "));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeTo_EmptyInput() {
 characterReader.consumeTo('a');
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('s');
 Assert.assertEquals("tes", consumed);
 }

 @Test
 public void testConsumeTo_NotPresent() {
 String consumed = characterReader.consumeTo('x');
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testConsumeToAny() {
 String consumed = characterReader.consumeToAny(' ', 'r');
 Assert.assertEquals(" is ", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("CharacterReaderReader", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String consumed = characterReader.consumeLetterThenDigitSequence();
 Assert.assertEquals("test1", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("CharacterReaderReader", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("80001234", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('s'));
 Assert.assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatches_String() {
 String seq = "is";
 Assert.assertTrue(characterReader.matches(seq));
 Assert.assertFalse(characterReader.matches("is not"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "is";
 Assert.assertTrue(characterReader.matchesIgnoreCase(seq));
 Assert.assertTrue(characterReader.matchesIgnoreCase("Is"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("is not"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'s', 'r'};
 Assert.assertTrue(characterReader.matchesAny(seq));
 Assert.assertFalse(characterReader.matchesAny('z', 'x'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(characterReader.matchesDigit());
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "is";
 characterReader.matchConsume(seq);
 Assert.assertEquals(3, characterReader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "is";
 characterReader.matchConsumeIgnoreCase(seq);
 Assert.assertEquals(3, characterReader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "is";
 Assert.assertTrue(characterReader.containsIgnoreCase(seq));
 }

}