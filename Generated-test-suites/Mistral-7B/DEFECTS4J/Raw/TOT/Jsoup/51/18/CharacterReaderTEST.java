package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertEquals(0, reader.pos());
 assertFalse(reader.matches('a'));
 assertFalse(reader.matchesIgnoreCase("AbC"));
 assertFalse(reader.matchesAny('b'));
 assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
 assertFalse(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 assertFalse(reader.matchesLetterThenDigitSequence());
 assertFalse(reader.matchesIgnoreCase("title"));
 assertFalse(reader.matchesIgnoreCase("Style"));
 assertFalse(reader.containsIgnoreCase("title"));
 assertFalse(reader.containsIgnoreCase("Style"));
 assertNull(reader.consumeAsString());
 assertNull(reader.consumeTo('a'));
 assertNull(reader.consumeTo("AbC"));
 assertNull(reader.consumeToAny(new char[]{'b'}));
 assertNull(reader.consumeToAnySorted(new char[]{'a', 'b'}));
 assertNull(reader.consumeData());
 assertNull(reader.consumeTagName());
 assertNull(reader.consumeToEnd());
 assertNull(reader.consumeLetterSequence());
 assertNull(reader.consumeLetterThenDigitSequence());
 assertNull(reader.consumeHexSequence());
 assertNull(reader.consumeDigitSequence());
 assertNull(reader.toString());
 }

 @Test
 public void testSimpleReader() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals(0, reader.pos());
 assertFalse(reader.matches('g'));
 assertFalse(reader.matchesIgnoreCase("AbC"));
 assertFalse(reader.matchesAny('g'));
 assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
 assertFalse(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 assertFalse(reader.matchesLetterThenDigitSequence());
 assertFalse(reader.matchesIgnoreCase("title"));
 assertFalse(reader.matchesIgnoreCase("Style"));
 assertFalse(reader.containsIgnoreCase("title"));
 assertFalse(reader.containsIgnoreCase("Style"));
 assertEquals("", reader.consumeAsString());
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 assertEquals("a", reader.consumeAsString());
 assertEquals('b', reader.current());
 assertEquals('b', reader.consume());
 assertEquals("b", reader.consumeAsString());
 assertEquals('c', reader.current());
 assertEquals('c', reader.consume());
 assertEquals("c", reader.consumeAsString());
 assertEquals('d', reader.current());
 assertEquals('d', reader.consume());
 assertEquals("d", reader.consumeAsString());
 assertEquals('e', reader.current());
 assertEquals('e', reader.consume());
 assertEquals("e", reader.consumeAsString());
 assertEquals('f', reader.current());
 assertEquals('f', reader.consume());
 assertEquals("f", reader.consumeAsString());
 assertNull(reader.consumeTo('g'));
 assertNull(reader.consumeTo("AbC"));
 assertNull(reader.consumeToAny(new char[]{'g'}));
 assertNull(reader.consumeToAnySorted(new char[]{'a', 'b'}));
 assertEquals("", reader.consumeData());
 assertEquals("", reader.consumeTagName());
 assertEquals("abcdef", reader.consumeToEnd());
 assertEquals("a", reader.consumeLetterSequence());
 assertEquals("ab", reader.consumeLetterThenDigitSequence());
 assertEquals("012345", reader.consumeDigitSequence());
 assertEquals("&lt;title&gt;", reader.consumeTo("<title>"));
 assertEquals("</style>", reader.consumeTo("</style>"));
 assertNull(reader.consumeToAny(new char[]{'g', 'h'}));
 assertNull(reader.consumeToAnySorted(new char[]{'g', 'h'}));
 assertNull(reader.consumeToAny(new char[]{'h', 'g'}));
 assertNull(reader.consumeToAnySorted(new char[]{'h', 'g'}));
 assertNull(reader.consumeToAny(new char[]{'a', 'b', 'c'}));
 assertNull(reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}));
 assertNull(reader.consumeData());
 assertNull(reader.consumeTagName());
 assertNull(reader.consumeToEnd());
 }

 @Test
 public void testErrorReader() {
 CharacterReader reader = new CharacterReader("abc\u0001def");
 assertEquals(0, reader.pos());
 assertFalse(reader.matches('g'));
 assertFalse(reader.matchesIgnoreCase("AbC"));
 assertFalse(reader.matchesAny('g'));
 assertFalse(reader.matchesAnySorted(new char[]{'a', 'b'}));
 assertFalse(reader.matchesLetter());
 assertFalse(reader.matchesDigit());
 assertFalse(reader.matchesLetterThenDigitSequence());
 assertFalse(reader.matchesIgnoreCase("title"));
 assertFalse(reader.matchesIgnoreCase("Style"));
 assertFalse(reader.containsIgnoreCase("title"));
 assertFalse(reader.containsIgnoreCase("Style"));
 assertEquals("", reader.consumeAsString());
 assertNull(reader.consumeAsString());
 assertNull(reader.consumeTo('g'));
 assertNull(reader.consumeTo("AbC"));
 assertNull(reader.consumeToAny(new char[]{'g'}));
 assertNull(reader.consumeToAnySorted(new char[]{'a', 'b'}));
 assertEquals("", reader.consumeData());
 assertEquals("", reader.consumeTagName());
 assertNull(reader.consumeToEnd());
 assertNull(reader.consumeLetterSequence());
 assertNull(reader.consumeLetterThenDigitSequence());
 assertNull(reader.consumeHexSequence());
 assertNull(reader.consumeDigitSequence());
 assertNull(reader.toString());
 }
}