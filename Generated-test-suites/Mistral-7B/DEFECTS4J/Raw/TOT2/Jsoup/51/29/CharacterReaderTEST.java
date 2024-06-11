import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testBasicFunctionality() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals('a', reader.current());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.consume());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("hello", reader.consumeAsString());
 assertEquals("world", reader.consumeAsString());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondEnd() {
 CharacterReader reader = new CharacterReader("abc");
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // should throw an exception
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("ababcaba");
 assertEquals(1, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('x'));
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("ababcaba");
 assertEquals("aba", reader.consumeTo('b'));
 assertEquals("abcaba", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdefghijk");
 assertEquals("abcdefghijk", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abracadabra");
 assertEquals("abra", reader.consumeToAny('a'));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("cabbbacba");
 assertEquals("ba", reader.consumeToAnySorted('b'));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;abc&amp;def&gt;");
 assertEquals("<abc&def>", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Hello World</title>");
 assertEquals("title", reader.consumeTagName());
 }

 @Test
 public void testCacheString() {
 CharacterReader reader = new CharacterReader("ababababa");
 String str = reader.cacheString(0, 5);
 assertEquals("ababa", str);
 }
}