import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setup() {
 char[] inputChars = new char[]{'t', 'e', 's', 't', ' ', 'c', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'r', 'e', 'a', 'd', 'e', 'r'};
 characterReader = new CharacterReader(new String(inputChars));
 }

 @Test
 public void testConstructor() {
 CharacterReader characterReader = new CharacterReader(null);
 Assert.assertNull(characterReader);
 characterReader = new CharacterReader("");
 Assert.assertEquals(0, characterReader.pos());
 characterReader = new CharacterReader("test");
 Assert.assertEquals(0, characterReader.pos());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 Assert.assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', characterReader.current());
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());
 }

 @Test
 public void testConsume() {
 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals('s', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 Assert.assertEquals('t', characterReader.current());
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
 Assert.assertEquals("tes", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('d');
 Assert.assertEquals("test red", characterReader.toString());
 Assert.assertEquals("ter", consumed);
 characterReader.consume();
 characterReader.consume();
 consumed = characterReader.consumeTo('r');
 Assert.assertEquals("test re", characterReader.toString());
 Assert.assertEquals("ad", consumed);
 }

 @Test
 public void testConsumeToAny() {
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 String consumed = characterReader.consumeToAny('a', 'b');
 Assert.assertEquals("d", consumed);
 characterReader.consume();
 consumed = characterReader.consumeToAny('e', 'f');
 Assert.assertEquals("", consumed);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("red", consumed);
 characterReader.consume();
 Assert.assertEquals("", characterReader.consumeToEnd().trim());
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("tes", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("test red", characterReader.toString());
 Assert.assertEquals("re", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("test re", characterReader.toString());
 Assert.assertEquals("4", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(characterReader.matches('t'));
 Assert.assertTrue(characterReader.matches("es"));
 Assert.assertFalse(characterReader.matches('a'));
 characterReader.consume();
 Assert.assertTrue(characterReader.matches('r'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(characterReader.matchesIgnoreCase("es"));
 Assert.assertTrue(characterReader.matchesIgnoreCase("Red"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("A"));
 characterReader.consume();
 Assert.assertTrue(characterReader.matchesIgnoreCase("r"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(characterReader.matchesAny('t', 's'));
 Assert.assertFalse(characterReader.matchesAny('a'));
 characterReader.consumeLetterSequence();
 Assert.assertTrue(characterReader.matchesAny('a', 'd'));
 }

 @Test
 public void testMatchConsume() {
 String consumed = characterReader.matchConsume("es");
 Assert.assertEquals("test red", characterReader.toString());
 Assert.assertEquals("re", consumed);
 characterReader.consume();
 consumed = characterReader.matchConsume("r");
 Assert.assertEquals("test re", characterReader.toString());
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = characterReader.matchConsumeIgnoreCase("es");
 Assert.assertEquals("test red", characterReader.toString());
 Assert.assertEquals("re", consumed);
 characterReader.consume();
 consumed = characterReader.matchConsumeIgnoreCase("r");
 Assert.assertEquals("test re", characterReader.toString());
 Assert.assertEquals("", consumed);
 }

 @Test
 public void testContainIgnoreCase() {
 Assert.assertTrue(characterReader.containsIgnoreCase("es"));
 Assert.assertTrue(characterReader.containsIgnoreCase("RED"));
 Assert.assertFalse(characterReader.containsIgnoreCase("AB"));
 characterReader.consumeLetterSequence();
 Assert.assertTrue(characterReader.containsIgnoreCase("ad"));
 }

 @Test(expected = AssertionError.class)
 public void testInvalidConsumeTo() {
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 String consumed = characterReader.consumeTo('z');
 }

 @Test(expected = AssertionError.class)
 public void testInvalidConsumeToAny() {
 characterReader.consumeLetterSequence();
 characterReader.consumeLetterSequence();
 String consumed = characterReader.consumeToAny('z');
 }
}