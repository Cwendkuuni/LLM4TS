package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
// Test cases for isEmpty() method
@Test
public void testIsEmpty() {
CharacterReader reader = new CharacterReader("");
assertTrue(reader.isEmpty());

reader = new CharacterReader("abc");
assertFalse(reader.isEmpty());

reader.consume();
reader.consume();
reader.consume();
assertTrue(reader.isEmpty());
}

// Test cases for consume() method
@Test
public void testConsumer() {
CharacterReader reader = new CharacterReader("abcdef");
assertSame('a', reader.current());
reader.consume();
assertSame('b', reader.current());
reader.consume();
assertSame('c', reader.current());
reader.consume();
assertSame('d', reader.current());
reader.consume();
assertSame('e', reader.current());
reader.consume();
assertSame('f', reader.current());
reader.consume(); // consumes the last character, now at end of string
assertTrue(reader.isEmpty());
}

// Test cases for consumeTo() method
@Test
public void testConsumeTo() {
CharacterReader reader = new CharacterReader("abcdabcdabcd");
String consumed = reader.consumeTo('d');
assertEquals("abc", consumed);
assertEquals('e', reader.current());

consumed = reader.consumeTo('a');
assertEquals("bcdabcd", consumed);
assertEquals('b', reader.current());

consumed = reader.consumeToEnd();
assertEquals("abcdabcd", consumed);
assertTrue(reader.isEmpty());
}

// Test cases for consumeToAny() method
@Test
public void testConsumeToAny() {
CharacterReader reader = new CharacterReader("abcdefgABCDEFG12345");
String consumed = reader.consumeToAny('a', 'b', 'c');
assertEquals("abc", consumed);
assertEquals('d', reader.current());

consumed = reader.consumeToAny('A', 'B', 'C');
assertEquals("DEFG12345", consumed);
assertEquals('1', reader.current());

consumed = reader.consumeToAny('1', '2', '3');
assertEquals("2345", consumed);
assertTrue(reader.isEmpty());
}

// Test cases for consumeLetterSequence() method
@Test
public void testConsumeLetterSequence() {
CharacterReader reader = new CharacterReader("helloWorld");
String consumed = reader.consumeLetterSequence();
assertEquals("hello", consumed);
assertEquals('W', reader.current());

consumed = reader.consumeLetterSequence();
assertEquals("d", consumed);
assertTrue(reader.isEmpty());

reader = new CharacterReader("123helloWorld456");
consumed = reader.consumeLetterSequence();
assertEquals("hello", consumed);
assertEquals('W', reader.current());

reader.consume();
consumed = reader.consumeLetterSequence();
assertEquals("World", consumed);
assertEquals('4', reader.current());
}

// Test case for matches() method
@Test
public void testMatches() {
CharacterReader reader = new CharacterReader("abcabc");
assertTrue(reader.matches('a'));
assertTrue(reader.matches('b'));
assertFalse(reader.matches('c'));
assertFalse(reader.matches('d'));
}

// Test case for matchesIgnoreCase() method
@Test
public void testMatchesIgnoreCase() {
CharacterReader reader = new CharacterReader("AbCaB");
assertTrue(reader.matchesIgnoreCase("Ab"));
assertTrue(reader.matchesIgnoreCase("Ca"));
assertTrue(reader.matchesIgnoreCase("AbC"));
assertFalse(reader.matchesIgnoreCase("D"));
}

// Test case for matchConsume() method
@Test
public void testMatchConsume() {
CharacterReader reader = new CharacterReader("abcdabcdabcd");
assertTrue(reader.matchConsume("ab"));
assertTrue(reader.matchConsume("cd"));
assertFalse(reader.matchConsume("ef"));
}

// Test case for matchesAny() method
@Test
public void testMatchesAny() {
CharacterReader reader = new CharacterReader("abcdefgABCDEFG12345");
assertTrue(reader.matchesAny('a', 'b', 'c'));
assertTrue(reader.matchesAny('A', 'B', 'C'));
assertTrue(reader.matchesAny('1', '2', '3'));
assertFalse(reader.matchesAny('x', 'y', 'z'));
}

// Test case for containsIgnoreCase() method
@Test
public void testContainsIgnoreCase() {
CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");
assertTrue(reader.containsIgnoreCase("title"));
assertTrue(reader.containsIgnoreCase("head"));
assertFalse(reader.containsIgnoreCase("body"));
}
}

// Test cases for other methods (isEmpty(), pos(), current(), unconsume(), advance(), mark(), rewindToMark(), consumeAsString(), consumeTo(), consumeTo(), consumeToAny(), consumeToEnd()) can be added as needed