package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

private String input;
private CharacterReader reader;

@Before
public void setUp() {
input = "This is a test <html><head><title>Test Title</title></head><body>";
reader = new CharacterReader(input);
}

@Test
public void testConstructor() {
assertNotNull(reader);
assertEquals(input, reader.input);
assertEquals(input.length(), reader.length);
}

@Test
public void testPos() {
assertEquals(0, reader.pos());
}

@Test
public void testIsEmpty() {
assertTrue(reader.isEmpty());
reader.consume();
assertFalse(reader.isEmpty());
}

@Test
public void testCurrent() {
assertEquals(input.charAt(0), reader.current());
assertEquals(EOF, reader.current()); // after consuming all characters
}

@Test
public void testConsume() {
assertEquals(input.charAt(0), reader.consume());
assertEquals(input.charAt(1), reader.current());
assertEquals(-1, reader.length() - reader.pos());
}

@Test
public void testUnconsume() {
reader.consume();
reader.consume();
reader.unconsume();
assertEquals(input.charAt(1), reader.current());
}

@Test
public void testAdvance() {
reader.advance();
assertEquals(input.charAt(1), reader.current());
}

@Test
public void testMark() {
reader.mark();
reader.consume();
reader.rewindToMark();
assertEquals(input.charAt(0), reader.current());
}

@Test
public void testConsumeAsString() {
String s = reader.consumeAsString();
assertEquals("is a test <", s);
}

@Test
public void testConsumeTo() {
String s = reader.consumeTo(' ');
assertEquals("is a test <", s);
s = reader.consumeTo('t');
assertEquals("is a test <html", s);
}

@Test
public void testConsumeToAny() {
String s = reader.consumeToAny('l', 't');
assertEquals("is a test <html", s);
s = reader.consumeToAny('h', 'l');
assertEquals("is a test <html<head", s);
}

@Test
public void testConsumeToEnd() {
String s = reader.consumeToEnd();
assertEquals("<html<head<title>Test Title</title></head><body>", s);
}

@Test
public void testConsumeLetterSequence() {
String s = reader.consumeLetterSequence();
assertEquals("title", s);
}

@Test
public void testConsumeHexSequence() {
String s = reader.consumeHexSequence();
assertEquals("htmlheadtitle", s);
}

@Test
public void testConsumeDigitSequence() {
String s = reader.consumeDigitSequence();
assertEquals("5", s);
}

@Test
public void testMatches() {
assertTrue(reader.matches('t'));
assertFalse(reader.matches('h'));
}

@Test
public void testMatchesString() {
assertTrue(reader.matches("is a test <html"));
assertFalse(reader.matches("</title>"));
}

@Test
public void testMatchesIgnoreCase() {
assertTrue(reader.matchesIgnoreCase("is a test <html"));
assertFalse(reader.matchesIgnoreCase("</title>"));
}

@Test
public void testMatchesAny() {
assertTrue(reader.matchesAny('h', 't'));
assertFalse(reader.matchesAny('z'));
}

@Test
public void testMatchesLetter() {
assertTrue(reader.matchesLetter());
assertFalse(reader.matchesLetter('5'));
}

@Test
public void testMatchesDigit() {
assertTrue(reader.matchesDigit());
assertFalse(reader.matchesDigit('a'));
}

@Test
public void testMatchConsumeString() {
assertTrue(reader.matchConsume("is a test <html"));
assertEquals("<html<head<title>Test Title</title>", reader.input.substring(reader.pos));
}

@Test
public void testMatchConsumeIgnoreCaseString() {
assertTrue(reader.matchConsumeIgnoreCase("is a test <html"));
assertEquals("<html<head<title>Test Title</title>", reader.input.substring(reader.pos));
}

@Test
public void testContainsIgnoreCase() {
assertTrue(reader.containsIgnoreCase("html"));
assertTrue(reader.containsIgnoreCase("HEAD"));
assertFalse(reader.containsIgnoreCase("FOOT"));
}

@Test
public void testToString() {
assertEquals("<html<head<title>Test Title</title>", reader.toString());
}

}