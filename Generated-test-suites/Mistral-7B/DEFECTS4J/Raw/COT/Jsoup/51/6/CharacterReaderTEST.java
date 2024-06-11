package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = "abcdABCD1234EFG".toCharArray();
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 assertEquals('b', reader.current());
 reader.consume();
 assertEquals('c', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("a", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(1, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOf_CharSequence() {
 CharSequence seq = "bc";
 assertEquals(1, reader.nextIndexOf(seq));
 assertEquals(-1, reader.nextIndexOf(new CharSequence() {
 @Override
 public int length() {
 return 5;
 }

 @Override
 public char charAt(int index) {
 return 'z';
 }
 }));
 }

 @Test
 public void testConsumeTo() {
 assertEquals("b", reader.consumeTo('c').trim());
 assertEquals("abcd", reader.consumeToEnd().trim());
 }

 @Test
 public void testConsumeTo_String() {
 assertEquals("bc", reader.consumeTo("cd").trim());
 assertEquals("abcdefg", reader.consumeToEnd().trim());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("123", reader.consumeToAny('1', '3').trim());
 assertEquals("", reader.consumeToAny('z').trim());
 }

 @Test
 public void testConsumeToAnySorted() {
 assertEquals("23", reader.consumeToAnySorted(new char[]{'1', '3'}).trim());
 assertEquals("", reader.consumeToAnySorted(new char[]{'z'}).trim());
 }

 @Test
 public void testConsumeData() {
 assertEquals("abcd", reader.consumeData().trim());
 }

 @Test
 public void testConsumeTagName() {
 assertEquals("abcd", reader.consumeTagName().trim());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("abcdefg", reader.consumeToEnd().trim());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("d", reader.consumeLetterSequence().trim());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 assertEquals("EFG", reader.consumeLetterThenDigitSequence().trim());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('d'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatches_String() {
 String seq = "bc";
 assertTrue(reader.matches(seq));
 assertFalse(reader.matches("ab"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "bc";
 assertTrue(reader.matchesIgnoreCase(seq));
 assertFalse(reader.matchesIgnoreCase("Ab"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'b', 'c'};
 assertTrue(reader.matchesAny(seq));
 assertFalse(reader.matchesAny(new char[]{'z'}));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] seq = {'b', 'c'};
 assertTrue(reader.matchesAnySorted(seq));
 assertFalse(reader.matchesAnySorted(new char[]{'z'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesLetter());
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "bc";
 assertTrue(reader.matchConsume(seq));
 assertFalse(reader.matchConsume("ab"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "bc";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 assertFalse(reader.matchConsumeIgnoreCase("Ab"));
 }

 @Test(expected = AssertionError.class)
 public void testContainsIgnoreCase_Empty() {
 reader.containsIgnoreCase("");
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("</title>"));
 assertFalse(reader.containsIgnoreCase("</style>"));
 }

 @Test
 public void testToString() {
 assertEquals("abcd", reader.toString().trim());
 }

 @Test
 public void testCacheString() {
 String str1 = reader.cacheString(0, 4);
 String str2 = reader.cacheString(0, 4);
 assertEquals(str1, str2);
 }

 @Test
 public void testRangeEquals() {
 int start = 0;
 int count = 4;
 String cached = "abcd";
 assertTrue(reader.rangeEquals(start, count, cached));
 }
}