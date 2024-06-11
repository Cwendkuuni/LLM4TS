import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f {[} ]}".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(reader.isEmpty());
 reader.consume();
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 char c;
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 char c;
 reader.consume();
 c = reader.current();
 Assert.assertEquals('b', c);
 reader.consume();
 c = reader.current();
 Assert.assertEquals('c', c);
 }

 @Test
 public void testUnconsume() {
 char c;
 reader.consume();
 reader.unconsume();
 c = reader.current();
 Assert.assertEquals('a', c);
 }

 @Test
 public void testAdvance() {
 reader.advance();
 char c = reader.current();
 Assert.assertEquals('b', c);
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 char c = reader.current();
 Assert.assertEquals('a', c);
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 Assert.assertEquals("a", str);
 reader.consume();
 str = reader.consumeAsString();
 Assert.assertEquals("b", str);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfNegativeChar() {
 reader.nextIndexOf('z');
 }

 @Test
 public void testNextIndexOfChar() {
 int index = reader.nextIndexOf('a');
 Assert.assertEquals(0, index);
 index = reader.nextIndexOf('b');
 Assert.assertEquals(1, index);
 index = reader.nextIndexOf('c');
 Assert.assertEquals(2, index);
 }

 @Test
 public void testNextIndexOfString() {
 int index = reader.nextIndexOf("ab");
 Assert.assertEquals(1, index);
 index = reader.nextIndexOf("bc");
 Assert.assertEquals(3, index);
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo('b');
 Assert.assertEquals("a", str);
 str = reader.consumeTo('c');
 Assert.assertEquals("a", str);
 str = reader.consumeTo('d');
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f {[} ]}", str);
 }

 @Test
 public void testConsumeToAny() {
 String str = reader.consumeToAny('a', 'b');
 Assert.assertEquals("a", str);
 str = reader.consumeToAny('b', 'c');
 Assert.assertEquals("b", str);
 str = reader.consumeToAny('x', 'z');
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeToAnySorted() {
 String str = reader.consumeToAnySorted('z', 'x');
 Assert.assertEquals("", str);
 str = reader.consumeToAnySorted('a', 'c');
 Assert.assertEquals("a", str);
 }

 @Test
 public void testConsumeData() {
 String str = reader.consumeData();
 Assert.assertEquals("&<>", str);
 }

 @Test
 public void testConsumeTagName() {
 String str = reader.consumeTagName();
 Assert.assertEquals("/", str);
 str = reader.consumeTagName();
 Assert.assertEquals(">", str);
 str = reader.consumeTagName();
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeToEndWithMark() {
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 String str = reader.consumeToEnd();
 Assert.assertEquals("bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f {[} ]}", str);
 }

 @Test(expected = NullPointerException.class)
 public void testConsumeLetterSequenceNullInput() {
 reader = new CharacterReader(null);
 reader.consumeLetterSequence();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeLetterThenDigitSequence() {
 reader.consumeLetterThenDigitSequence();
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 Assert.assertEquals("2f", str);
 str = reader.consumeHexSequence();
 Assert.assertEquals("65", str);
 str = reader.consumeHexSequence();
 Assert.assertEquals("", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", str);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assert.assertFalse(reader.matchesIgnoreCase("cd"));
 }

 @Test
 public void testMatchesAny() {
 Assert.assertTrue(reader.matchesAny('a', 'b'));
 Assert.assertFalse(reader.matchesAny('x', 'z'));
 }

 @Test
 public void testMatchesAnySorted() {
 Assert.assertTrue(reader.matchesAnySorted('z', 'x'));
 Assert.assertFalse(reader.matchesAnySorted('a', 'c'));
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
 public void testMatchConsumeString() {
 String str = reader.matchConsume("ab");
 Assert.assertEquals("ab", str);
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String str = reader.matchConsumeIgnoreCase("Ab");
 Assert.assertEquals("ab", str);
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/style"));
 }

 @Test
 public void testToString() {
 String str = reader.toString();
 Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&<>\t\n\r\f {[} ]", str);
 }

}