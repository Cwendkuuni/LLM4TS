import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertTrue(reader.pos() == 0);
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Test String");
 assertTrue(reader.pos() == 0);
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderConstructorWithReaderAndSize() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"), 10);
 assertTrue(reader.charBuf.length == 10);
 }

 @Test
 public void testBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.bufferUp();
 assertEquals(reader.charBuf[0], 'T');
 }

 @Test(expected = UncheckedIOException.class)
 public void testBufferUpWithIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String")) {
 @Override
 public int read(char[] c, int off, int len) throws IOException {
 throw new IOException("Failed to read");
 }
 };
 reader.bufferUp();
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.advance();
 reader.advance();
 assertEquals(reader.pos(), 2);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals(reader.isEmpty(), false);
 reader.consume();
 assertEquals(reader.isEmpty(), true);
 }

 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals(reader.current(), 'T');
 reader.advance();
 assertEquals(reader.current(), 'e');
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.consume();
 assertEquals(reader.current(), 'e');
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.consume();
 reader.unconsume();
 assertEquals(reader.current(), 'T');
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.advance();
 reader.advance();
 assertEquals(reader.pos(), 2);
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals(reader.pos(), 0);
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.nextIndexOf('t'), 1);
 assertEquals(reader.nextIndexOf('s'), -1);
 }

 @Test
 public void testNextIndexOfWithString() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.nextIndexOf("st"), 2);
 assertEquals(reader.nextIndexOf("ing"), -1);
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.consumeTo('t').toString(), "Test");
 }

 @Test
 public void testConsumeToWithString() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.consumeTo("est").toString(), "TestString");
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.consumeToAny(',').toString(), "Test");
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.consumeToAnySorted(new char[]{'a', 'e', 'i'}).toString(), "Test");
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&lt;Test&gt;"));
 assertEquals(reader.consumeData().toString(), "&lt;Test&gt;");
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<test>"));
 assertEquals(reader.consumeTagName().toString(), "<test>");
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertEquals(reader.consumeToEnd().toString(), "TestString");
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Test"));
 assertEquals(reader.consumeLetterSequence().toString(), "Test");
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Test123"));
 assertEquals(reader.consumeLetterThenDigitSequence().toString(), "Test123");
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Test&#x41;x"));
 assertEquals(reader.consumeHexSequence().toString(), "Test&#x41;x");
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Test123"));
 assertEquals(reader.consumeDigitSequence().toString(), "123");
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matches('T'));
 assertTrue(reader.matches("String"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchesIgnoreCase("string"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchesAny('s', 't'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchesAnySorted(new char[]{'s', 't'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("Test123"));
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchConsume("est"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("TestString"));
 assertTrue(reader.matchConsumeIgnoreCase("EST"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testHeuristicBinaryContent() {
 CharacterReader reader = new CharacterReader(new StringReader("\u0000Test\u0000"));
 assertTrue(reader.heuristicBinaryContent());
 }
}