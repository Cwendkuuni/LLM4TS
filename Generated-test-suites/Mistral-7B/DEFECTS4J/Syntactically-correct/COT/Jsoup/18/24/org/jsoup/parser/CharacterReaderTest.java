import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = new char[]{'t', 'e', 's', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("test");
 Assert.assertNotNull(reader);
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 char c = reader.current();
 reader.consume();
 Assert.assertEquals(c, reader.current());
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('t', reader.current());
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('e', reader.current());
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('t', reader.current());
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 Assert.assertEquals("tes", str);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo(' ');
 Assert.assertEquals("test", str);
 Assert.assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 Assert.assertEquals("CharacterReader ", str);
 Assert.assertEquals(21, reader.pos());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
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
 Assert.assertFalse(reader.isEmpty()); // should be empty after consuming all characters
 reader.consume(); // should throw an exception
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("test", str);
 Assert.assertEquals(10, reader.pos());
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 Assert.assertEquals("CharacterReader", str);
 Assert.assertEquals(21, reader.pos());
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("21", str);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("test"));
 Assert.assertFalse(reader.matches("Char"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("test"));
 Assert.assertTrue(reader.matchesIgnoreCase("TEST"));
 Assert.assertFalse(reader.matchesIgnoreCase("Char"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'t', 's'};
 Assert.assertTrue(reader.matchesAny(seq));
 Assert.assertFalse(reader.matchesAny(new char[]{'r'}));
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertFalse(reader.matchesLetter());
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String str = reader.matchConsume("test");
 Assert.assertEquals("test", str);
 Assert.assertEquals(10, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String str = reader.matchConsumeIgnoreCase("TEST");
 Assert.assertEquals("test", str);
 Assert.assertEquals(10, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("character"));
 Assert.assertFalse(reader.containsIgnoreCase("reader"));
 }

 @Test
 public void testToString() {
 Assert.assertEquals(" CharacterReader  ", reader.toString());
 }
}