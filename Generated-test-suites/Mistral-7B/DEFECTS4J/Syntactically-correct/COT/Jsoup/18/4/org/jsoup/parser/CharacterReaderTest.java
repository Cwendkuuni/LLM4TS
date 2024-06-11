import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = {'t', 'e', 's', 't', ' ', 'c', 'o', 'n', 's', 'u', 'm', 'e', ' ', 'o', 'f', ' ', 't', 'e', 's', 't', ' ', 'c', 'l', 'a', 's', 's', ' ', 'w', 'i', 't', 'h', ' ', 'b', 'o', 'd', 'y', '.', ' ', 'C', 'h', 'e', 'c', 'k', 'i', 'n', 'g', ' ', 'f', 'o', 'r', ' ', 't', 'h', 'e', ' ', 'e', 'n', 'd', '.'};
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testIsEmptyWhenEmpty() {
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 char c;
 String consumed;
 for (int i = 0; i < reader.length(); i++) {
 c = reader.current();
 consumed = reader.consumeAsString();
 Assert.assertEquals(1, consumed.length());
 Assert.assertEquals(c, consumed.charAt(0));
 }
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsumeTo() {
 consumed = reader.consumeTo('s');
 Assert.assertEquals("tes", consumed);
 consumed = reader.consumeTo('z');
 Assert.assertEquals(reader.consumeToEnd(), "");
 }

 @Test
 public void testConsumeToString() {
 consumed = reader.consumeTo("of");
 Assert.assertEquals("test clas", consumed);
 }

 @Test
 public void testConsumeToAny() {
 consumed = reader.consumeToAny('s', 'd');
 Assert.assertEquals("test", consumed);
 consumed = reader.consumeToAny('d');
 Assert.assertEquals(reader.consumeToEnd(), "checking for body.");
 }

 @Test
 public void testConsumeToEnd() {
 consumed = reader.consumeToEnd();
 Assert.assertEquals("checking for body. Chetan for the end.", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 consumed = reader.consumeLetterSequence();
 Assert.assertEquals("test", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 consumed = reader.consumeHexSequence();
 Assert.assertEquals("check", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 consumed = reader.consumeDigitSequence();
 Assert.assertEquals("123", consumed);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches('x'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("test"));
 Assert.assertFalse(reader.matches("test1"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("test"));
 Assert.assertTrue(reader.matchesIgnoreCase("Test"));
 Assert.assertFalse(reader.matchesIgnoreCase("test1"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('t', 's'));
 Assert.assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 consumed = reader.matchConsume("of");
 Assert.assertEquals("testing for body.", consumed);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 consumed = reader.matchConsumeIgnoreCase("For The End.");
 Assert.assertEquals("checking for body. Chetan for the end.", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("for the end"));
 Assert.assertFalse(reader.containsIgnoreCase("class"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals("of testing for body. Chetan for the end.", reader.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderNullInput() {
 new CharacterReader(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCharacterReaderEndOfInput() {
 reader.consumeToEnd();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 Assert.assertTrue(reader.isEmpty());
 }
}