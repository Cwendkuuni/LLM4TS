package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import org.junit.Test;

public class CharacterReaderTest {

CharacterReader reader;

@Before
public void setup() {
this.reader = new CharacterReader("abc123def");
}

@Test
public void testIsEmpty() {
assertTrue(reader.isEmpty());
reader.advance();
assertFalse(reader.isEmpty());
}

@Test
public void testPos() {
assertEquals(0, reader.pos());
reader.consume();
assertEquals(1, reader.pos());
}

@Test
public void testCurrent() {
assertEquals('a', reader.current());
reader.consume();
assertEquals('b', reader.current());
}

@Test
public void testConsume() {
assertEquals('a', reader.consume());
assertEquals('b', reader.current());
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
reader.consume();
assertEquals('b', reader.current());
reader.rewindToMark();
assertEquals('a', reader.current());
}

@Test
public void testConsumeAsString() {
assertEquals("ab", reader.consumeAsString());
assertEquals("cd", reader.consumeAsString());
assertEquals("", reader.consumeAsString());
}

@Test
public void testConsumeTo() {
assertEquals("bc", reader.consumeTo('c'));
assertEquals("def", reader.consumeTo('f'));
assertEquals("", reader.consumeTo('z'));
assertEquals("123", reader.consumeToEnd());
}

@Test
public void testConsumeToAny() {
assertEquals("12", reader.consumeToAny('1', '2'));
assertEquals("def", reader.consumeToAny('d', 'f'));
assertEquals("", reader.consumeToAny('z'));
}

@Test
public void testConsumeToEnd() {
assertEquals("123def", reader.consumeToEnd());
}

@Test
public void testConsumeLetterSequence() {
assertEquals("abc", reader.consumeLetterSequence());
assertEquals("", reader.consumeLetterSequence());
}

@Test
public void testConsumeHexSequence() {
assertEquals("12", reader.consumeHexSequence());
assertEquals("", reader.consumeHexSequence());
}

@Test
public void testConsumeDigitSequence() {
assertEquals("123", reader.consumeDigitSequence());
assertEquals("", reader.consumeDigitSequence());
}

@Test
public void testMatches() {
assertTrue(reader.matches('a'));
assertTrue(reader.matches('b'));
assertFalse(reader.matches('c'));
}

@Test
public void testMatchesString() {
assertTrue(reader.matches("ab"));
assertFalse(reader.matches("cd"));
}

@Test
public void testMatchesIgnoreCase() {
assertTrue(reader.matchesIgnoreCase("AB"));
assertFalse(reader.matchesIgnoreCase("cd"));
}

@Test
public void testMatchesAny() {
assertTrue(reader.matchesAny('a', 'b'));
assertFalse(reader.matchesAny('c'));
}

@Test
public void testMatchesLetter() {
assertTrue(reader.matchesLetter());
assertFalse(reader.matchesLetter());
}

@Test
public void testMatchesDigit() {
assertTrue(reader.matchesDigit());
assertFalse(reader.matchesDigit());
}

@Test
public void testMatchConsume() {
assertTrue(reader.matchConsume("ab"));
assertEquals("cd", reader.current());
}

@Test
public void testMatchConsumeIgnoreCase() {
assertTrue(reader.matchConsumeIgnoreCase("Ab"));
assertEquals("cd", reader.current());
}

@Test
public void testContainsIgnoreCase() {
assertTrue(reader.containsIgnoreCase("AB"));
assertTrue(reader.containsIgnoreCase("cd"));
assertFalse(reader.containsIgnoreCase("ef"));
}

@Test
public void testToString() {
assertEquals("123def", reader.toString());
}
}