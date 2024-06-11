package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

	@Test
	public void testCharacterReader() {
		String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		CharacterReader reader = new CharacterReader(input);
		assertEquals(0, reader.pos());
		assertEquals(26, reader.length());
		assertFalse(reader.isEmpty());
		assertSame('a', reader.current());
		assertSame('a', reader.consume());
		assertEquals(1, reader.pos());
		assertFalse(reader.isEmpty());
		assertEquals('b', reader.current());
		// test mark and rewind
		reader.mark();
		assertEquals(1, reader.pos());
		reader.rewindToMark();
		assertEquals(1, reader.pos());
		assertSame('a', reader.current());
		// test advance
		reader.advance();
		assertEquals(2, reader.pos());
		assertSame('b', reader.current());
		// test consumeAsString
		assertEquals("ab", reader.consumeAsString());
		assertEquals(3, reader.pos());
		assertFalse(reader.isEmpty());
		// test consumeTo
		assertEquals("def", reader.consumeTo('f'));
		assertEquals(6, reader.pos());
		assertEquals(12, reader.length());
		// test consumeToEnd
		assertEquals("ijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", reader.consumeToEnd());
		assertEquals(26, reader.pos());
		assertTrue(reader.isEmpty());
		// test consumeLetterSequence
		assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
		assertEquals(26, reader.pos());
		assertTrue(reader.isEmpty());
		// test consumeHexSequence
		assertEquals("0123456789ABCDEF", reader.consumeHexSequence());
		assertEquals(11, reader.pos());
		assertTrue(reader.isEmpty());
		// test consumeDigitSequence
		assertEquals("0123456789", reader.consumeDigitSequence());
		assertEquals(10, reader.pos());
		assertTrue(reader.isEmpty());
		// test matches
		assertTrue(reader.matches('a'));
		assertFalse(reader.matches('z'));
		// test matchesIgnoreCase
		assertTrue(reader.matchesIgnoreCase("A"));
		assertFalse(reader.matchesIgnoreCase("z"));
		// test matchesAny
		assertTrue(reader.matchesAny('a', 'b'));
		assertFalse(reader.matchesAny('x', 'b'));
		// test matchesLetter
		assertTrue(reader.matchesLetter());
		assertFalse(reader.matchesLetter('9'));
		// test matchesDigit
		assertTrue(reader.matchesDigit());
		assertFalse(reader.matchesDigit('a'));
		// test matchConsume
		assertEquals("def", reader.matchConsume("def"));
		assertEquals(15, reader.pos());
		assertEquals("", reader.matchConsume("xyz"));
		assertEquals(15, reader.pos());
		// test matchConsumeIgnoreCase
		assertEquals("DEF", reader.matchConsumeIgnoreCase("def"));
		assertEquals("", reader.matchConsumeIgnoreCase("XYz"));
		assertEquals(15, reader.pos());
		// test containsIgnoreCase
		assertTrue(reader.containsIgnoreCase("ABC"));
		assertTrue(reader.containsIgnoreCase("def"));
		assertFalse(reader.containsIgnoreCase("xyz"));
		// test toString
		assertEquals("", reader.toString());
		// test consumeToAny
		assertEquals("0123456789ABCDEF", reader.consumeToAny('0', 'F'));
		assertEquals("", reader.consumeToAny('X'));
	}

	@Test(expected = NullPointerException.class)
	public void testCharacterReaderNullInput() {
		CharacterReader reader = new CharacterReader(null);
	}
}