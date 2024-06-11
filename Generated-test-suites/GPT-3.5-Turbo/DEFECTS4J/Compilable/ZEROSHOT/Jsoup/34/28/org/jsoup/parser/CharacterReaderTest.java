package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
	private CharacterReader reader;

	@Before
	public void setUp() {
		reader = new CharacterReader("testString");
	}

	@Test
	public void testPos() {
		assertEquals(0, reader.pos());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(reader.isEmpty());
	}

	@Test
	public void testCurrent() {
		assertEquals('t', reader.current());
	}

	@Test
	public void testConsume() {
		assertEquals('t', reader.consume());
		assertEquals('e', reader.consume());
		assertEquals('s', reader.consume());
		assertEquals('t', reader.consume());
		assertEquals('S', reader.consume());
		assertEquals('t', reader.consume());
		assertEquals('r', reader.consume());
		assertEquals('i', reader.consume());
		assertEquals('n', reader.consume());
		assertEquals('g', reader.consume());
		assertEquals(CharacterReader.EOF, reader.consume());
	}

	@Test
	public void testUnconsume() {
		reader.unconsume();
		assertEquals('g', reader.current());
	}

	@Test
	public void testAdvance() {
		reader.advance();
		assertEquals('e', reader.current());
	}

	@Test
	public void testMark() {
		reader.mark();
		assertEquals(0, reader.pos());
	}

	@Test
	public void testRewindToMark() {
		reader.advance();
		reader.mark();
		reader.rewindToMark();
		assertEquals('t', reader.current());
	}

	@Test
	public void testConsumeAsString() {
		assertEquals("t", reader.consumeAsString());
		assertEquals("e", reader.consumeAsString());
		assertEquals("s", reader.consumeAsString());
		assertEquals("t", reader.consumeAsString());
		assertEquals("S", reader.consumeAsString());
		assertEquals("t", reader.consumeAsString());
		assertEquals("r", reader.consumeAsString());
		assertEquals("i", reader.consumeAsString());
		assertEquals("n", reader.consumeAsString());
		assertEquals("g", reader.consumeAsString());
		assertEquals("\uFFFF", reader.consumeAsString());
	}

	@Test
	public void testNextIndexOfChar() {
		assertEquals(0, reader.nextIndexOf('t'));
		assertEquals(1, reader.nextIndexOf('e'));
		assertEquals(2, reader.nextIndexOf('s'));
		assertEquals(3, reader.nextIndexOf('t'));
		assertEquals(-1, reader.nextIndexOf('S'));
		assertEquals(-1, reader.nextIndexOf('g'));
		assertEquals(-1, reader.nextIndexOf('x'));
	}

	@Test
	public void testNextIndexOfCharSequence() {
		assertEquals(0, reader.nextIndexOf("testString"));
		assertEquals(1, reader.nextIndexOf("estString"));
		assertEquals(2, reader.nextIndexOf("stString"));
		assertEquals(3, reader.nextIndexOf("tString"));
		assertEquals(-1, reader.nextIndexOf("S"));
		assertEquals(-1, reader.nextIndexOf("tring"));
		assertEquals(-1, reader.nextIndexOf("x"));
	}

	@Test
	public void testConsumeToChar() {
		assertEquals("testS", reader.consumeTo('S'));
		assertEquals("tring", reader.consumeTo('x'));
	}

	@Test
	public void testConsumeToCharSequence() {
		assertEquals("testS", reader.consumeTo("Str"));
		assertEquals("ing", reader.consumeTo("outside"));
	}

	@Test
	public void testConsumeToAny() {
		assertEquals("test", reader.consumeToAny('S', 't'));
		assertEquals("S", reader.consumeToAny('t'));
		assertEquals("", reader.consumeToAny('x'));
	}

	@Test
	public void testConsumeToEnd() {
		assertEquals("testString", reader.consumeToEnd());
		assertEquals("", reader.consumeToEnd());
	}

	@Test
	public void testConsumeLetterSequence() {
		assertEquals("test", reader.consumeLetterSequence());
		assertEquals("S", reader.consumeLetterSequence());
		assertEquals("t", reader.consumeLetterSequence());
		assertEquals("ring", reader.consumeLetterSequence());
		assertEquals("", reader.consumeLetterSequence());
	}

	@Test
	public void testConsumeLetterThenDigitSequence() {
		assertEquals("testS", reader.consumeLetterThenDigitSequence());
		assertEquals("tring", reader.consumeLetterThenDigitSequence());
		assertEquals("", reader.consumeLetterThenDigitSequence());
	}

	@Test
	public void testConsumeHexSequence() {
		assertEquals("t", reader.consumeHexSequence());
		assertEquals("est", reader.consumeHexSequence());
		assertEquals("", reader.consumeHexSequence());
	}

	@Test
	public void testConsumeDigitSequence() {
		assertEquals("t", reader.consumeDigitSequence());
		assertEquals("es", reader.consumeDigitSequence());
		assertEquals("t", reader.consumeDigitSequence());
		assertEquals("S", reader.consumeDigitSequence());
		assertEquals("t", reader.consumeDigitSequence());
		assertEquals("ring", reader.consumeDigitSequence());
		assertEquals("", reader.consumeDigitSequence());
	}

	@Test
	public void testMatches() {
		assertTrue(reader.matches('t'));
		assertTrue(reader.matches("testString"));
		assertFalse(reader.matches('e'));
		assertFalse(reader.matches("test"));
	}

	@Test
	public void testMatchesIgnoreCase() {
		assertTrue(reader.matchesIgnoreCase("TESTSTRING"));
		assertTrue(reader.matchesIgnoreCase("tESTsTRING"));
		assertFalse(reader.matchesIgnoreCase("test"));
		assertFalse(reader.matchesIgnoreCase("string"));
	}

	@Test
	public void testMatchesAny() {
		assertTrue(reader.matchesAny('t', 'e', 's'));
		assertTrue(reader.matchesAny('S'));
		assertFalse(reader.matchesAny('x'));
	}

	@Test
	public void testMatchesLetter() {
		assertTrue(reader.matchesLetter());
		reader.advance();
		assertFalse(reader.matchesLetter());
	}

	@Test
	public void testMatchesDigit() {
		assertTrue(reader.matchesDigit());
		reader.advance();
		assertFalse(reader.matchesDigit());
	}

	@Test
	public void testMatchConsume() {
		assertTrue(reader.matchConsume("test"));
		reader.advance();
		assertTrue(reader.matchConsume("String"));
		assertFalse(reader.matchConsume("x"));
	}

	@Test
	public void testMatchConsumeIgnoreCase() {
		assertTrue(reader.matchConsumeIgnoreCase("TEST"));
		assertTrue(reader.matchConsumeIgnoreCase("tEST"));
		reader.advance();
		assertFalse(reader.matchConsumeIgnoreCase("S"));
		assertFalse(reader.matchConsumeIgnoreCase("x"));
	}

	@Test
	public void testContainsIgnoreCase() {
		assertTrue(reader.containsIgnoreCase("test"));
		assertTrue(reader.containsIgnoreCase("sTRIng"));
		assertFalse(reader.containsIgnoreCase("x"));
	}

	@Test
	public void testToString() {
		assertEquals("testString", reader.toString());
	}
}