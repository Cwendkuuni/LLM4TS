import org.jsoup.parser.CharacterReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testData = new char[]{'t', 'e', 's', 't', '\u0001'}; // EOF marker
 String testInput = new String(testData);
 reader = new CharacterReader(testInput);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('e', reader.consume());
 assertFalse(reader.isEmpty());
 assertEquals(3, reader.pos());
 }

 @Test
 public void testConsumeEnd() {
 String consumed = reader.consumeToEnd();
 assertEquals(new String(new char[]{'s', '\u0001'}), consumed);
 assertEquals(5, reader.pos());
 assertEquals(new String(""), reader.consumeToString());
 }

 @Test
 public void testCurrent() {
 assertEquals('s', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
 reader.consume();
 reader.consume(); // consume all characters
 reader.consume(); // should throw an IllegalStateException
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertSame(reader.current(), 's');
 }

 @Test
 public void testConsumeSequence() {
 String seq = "est";
 String consumed = reader.consumeAsString();
 assertEquals(seq, consumed);
 }

 @Test
 public void testConsumeToChar() {
 char c = 't';
 String consumed = reader.consumeTo(c);
 assertEquals(new String(new char[]{'t'}), consumed);
 assertEquals(2, reader.pos());
 }

 @Test
 public void testConsumeToString() {
 String consumed = reader.consumeToString();
 assertEquals(new String(""), consumed);
 }

 @Test
 public void testConsumeToIgnoreCase() {
 String seq = "es";
 String consumed = reader.consumeToIgnoreCase(seq);
 assertEquals(new String("es"), consumed);
 assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeToAny() {
 char[] seq = new char[]{'s', 't'};
 String consumed = reader.consumeToAny(seq);
 assertEquals(new String("st"), consumed);
 assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 assertEquals(new String("test"), consumed);
 assertEquals(5, reader.pos());
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 assertEquals(new String("\\u0001"), consumed);
 assertEquals(5, reader.pos());
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 assertEquals(new String("3"), consumed);
 assertEquals(3, reader.pos());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('s'));
 assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesSequence() {
 String seq = "es";
 assertTrue(reader.matches(seq));
 assertFalse(reader.matches("est"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "es";
 assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "es";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testMatchConsumeLetterSequence() {
 String seq = "est";
 reader.matchConsume(seq);
 assertEquals(new String(""), reader.consumeToString());
 assertEquals(8, reader.pos());
 }

 @Test
 public void testMatchConsumeLetterSequenceIgnoreCase() {
 String seq = "es";
 reader.matchConsumeIgnoreCase(seq);
 assertEquals(new String(""), reader.consumeToString());
 assertEquals(8, reader.pos());
 }
}