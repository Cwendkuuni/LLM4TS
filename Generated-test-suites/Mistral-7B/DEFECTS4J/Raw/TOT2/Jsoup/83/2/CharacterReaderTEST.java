import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assertions.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assertions.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assertions.assertEquals(4, reader.nextIndexOf('l'));
 Assertions.assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assertions.assertEquals(4, reader.nextIndexOf("ll"));
 Assertions.assertEquals(-1, reader.nextIndexOf("zz"));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeTo('l');
 Assertions.assertEquals("Hello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeToEnd();
 Assertions.assertEquals("World", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("Hello World<html><body>Test</body></html>");
 String consumed = reader.consumeToAny('>');
 Assertions.assertEquals("Hello World<html><body>", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("Hello World<html><body>Test</body></html>");
 String consumed = reader.consumeToAnySorted(new char[]{'<', '>', 'W'});
 Assertions.assertEquals("Hello W", consumed);
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;html&gt;&amp;<script>alert('Hello World')&lt;/script&gt;");
 String consumed = reader.consumeData();
 Assertions.assertEquals("&lt;html&amp;<script>alert('Hello World')&lt;/script&gt;", consumed);
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html><body><h1 class=\"test\">Test</h1></body></html>");
 String consumed = reader.consumeTagName();
 Assertions.assertEquals("html", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 String consumed = reader.consumeLetterSequence();
 Assertions.assertEquals("Hello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello123World456");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assertions.assertEquals("Hello123", consumed);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&#x61;&#x6C;&#x65;&#x6D;");
 String consumed = reader.consumeHexSequence();
 Assertions.assertEquals("abc", consumed);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 Assertions.assertEquals("1234567890", consumed);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matches('a'));
 Assertions.assertTrue(reader.matches("ab"));
 Assertions.assertFalse(reader.matches('z'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchesIgnoreCase("Ab"));
 Assertions.assertFalse(reader.matchesIgnoreCase("z"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchesAny('a', 'b'));
 Assertions.assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchesAnySorted(new char[]{'a', 'd'}));
 Assertions.assertFalse(reader.matchesAnySorted(new char[]{'z'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertFalse(reader.matchesLetter('z'));
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234");
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testCharacterReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchConsume("ab"));
 }

 @Test
 public void testCharacterReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 Assertions.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
 }

 @Test
 public void testCharacterReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><body><h1 class=\"test\">Test</h1></body></html>");
 Assertions.assertTrue(reader.containsIgnoreCase("/html"));
 Assertions.assertTrue(reader.containsIgnoreCase("/body"));
 Assertions.assertTrue(reader.containsIgnoreCase("h1"));
 Assertions.assertTrue(reader.containsIgnoreCase("test"));
 Assertions.assertTrue(reader.containsIgnoreCase("test"));
 Assertions.assertFalse(reader.containsIgnoreCase("/foobar"));
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 reader.bufferUp();
 reader.reader.close();
 reader.bufferUp();
 }
}