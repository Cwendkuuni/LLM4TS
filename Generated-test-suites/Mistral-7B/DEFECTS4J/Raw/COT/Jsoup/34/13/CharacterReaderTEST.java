import org.junit.Assert;import org.junit.Test;import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = "abcDEF123ABCdef456".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testConstructor() {
 Assert.assertNotNull(reader);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals(-1, reader.nextIndexOf('\0'));
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testConsume() {
 char c;
 for (int i = 0; i < reader.length(); i++) {
 c = reader.consume();
 Assert.assertEquals((char) (i + 'a'), c);
 Assert.assertEquals(i + 1, reader.pos());
 }
 Assert.assertEquals(CharacterReader.EOF, reader.consume());
 Assert.assertEquals(reader.length(), reader.pos());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 Assert.assertEquals((char) 'a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals((char) 'b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals((char) 'a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 Assert.assertEquals("a", str);
 reader.consume();
 Assert.assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 Assert.assertEquals(0, reader.nextIndexOf('a'));
 Assert.assertEquals(1, reader.nextIndexOf('b'));
 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 Assert.assertEquals(-1, reader.nextIndexOf('\0'));
 }

 @Test
 public void testConsumeTo() {
 String str;
 str = reader.consumeTo('e');
 Assert.assertEquals("a", str);
 reader.consume();
 Assert.assertEquals("ab", str = reader.consumeTo('e'));
 Assert.assertEquals("cDEF", str = reader.consumeTo('F'));
 Assert.assertEquals("ABCdef", str = reader.consumeToEnd());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEndWhenAtEnd() {
 reader.consumeToEnd();
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 Assert.assertEquals("abc", str);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String str = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("ab123", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 Assert.assertEquals("ABCdef", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 Assert.assertEquals("123456", str);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 String str = "abc";
 Assert.assertTrue(reader.matches(str));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String str = "ABC";
 Assert.assertTrue(reader.matchesIgnoreCase(str));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'a', 'b', 'c'};
 Assert.assertTrue(reader.matchesAny(seq));
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
 String str = "abc";
 reader.matchConsume(str);
 Assert.assertEquals("def123ABCdef", reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String str = "ABC";
 reader.matchConsumeIgnoreCase(str);
 Assert.assertEquals("abcdef123ABCdef", reader.toString());
 }

 @Test
 public void testContainsIgnoreCase() {
 String str = "</title>";
 Assert.assertTrue(reader.containsIgnoreCase(str));
 }

 @Test
 public void testToString() {
 String str = reader.toString();
 Assert.assertEquals("abcDEF123ABCdef456", str);
 }

}