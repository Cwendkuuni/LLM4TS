package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class CharacterReaderTest {

 @Test
 public void testCharacterReader() {
 CharacterReader reader = new CharacterReader("Hello World!");

 // test isEmpty()
 assertTrue(reader.isEmpty());

 // test pos()
 assertEquals(0, reader.pos());

 // test current() and consume()
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 reader.consume();
 assertEquals('o', reader.current());
 reader.consume();
 assertEquals(' ', reader.current());
 reader.consume();
 assertEquals('W', reader.current());
 reader.consume();
 assertEquals('o', reader.current());
 reader.consume();
 assertEquals('r', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 reader.consume();
 assertEquals('d', reader.current());
 reader.consume();
 assertEquals('!', reader.current());
 reader.consume(); // consume EOF
 assertFalse(reader.isEmpty());
 assertEquals(-1, reader.current());

 // test nextIndexOf() with char
 int indexOfChar = reader.nextIndexOf('l');
 assertEquals(8, indexOfChar);
 indexOfChar = reader.nextIndexOf('x');
 assertEquals(-1, indexOfChar);

 // test nextIndexOf() with CharSequence
 CharSequence charSequence = "World";
 int indexOfSequence = reader.nextIndexOf(charSequence);
 assertEquals(10, indexOfSequence);

 // test consumeAsString()
 assertEquals("Hello", reader.consumeAsString());
 assertEquals("World!", reader.toString());

 // test consumeTo() with char
 assertEquals("ello", reader.consumeTo(' '));
 assertEquals("World!", reader.toString());

 // test consumeTo() with CharSequence
 assertEquals("ello Worl", reader.consumeTo("ld!"));
 assertEquals("!", reader.current());
 assertEquals("World!", reader.toString());

 // test consumeToAny()
 assertEquals("Hello", reader.consumeToAny('o'));
 assertEquals("World!", reader.toString());

 // test consumeToAnySorted()
 assertEquals("Hello", reader.consumeToAnySorted(new char[]{'o', 'l'}));
 assertEquals("World!", reader.toString());

 // test consumeData()
 assertEquals("Hello&World!", reader.consumeData());
 assertEquals("Hello&World!", reader.toString());

 // test consumeTagName()
 assertEquals("title", reader.consumeTagName());
 assertEquals("", reader.consumeTagName());
 assertEquals("", reader.consumeTagName());

 // test consumeToEnd()
 assertEquals("Hello World!", reader.consumeToEnd());
 assertEquals("", reader.toString());

 // test consumeLetterSequence()
 assertEquals("llo", reader.consumeLetterSequence());
 assertEquals("", reader.consumeLetterSequence());

 // test consumeLetterThenDigitSequence()
 assertEquals("llo123", reader.consumeLetterThenDigitSequence());
 assertEquals("", reader.consumeLetterThenDigitSequence());

 // test consumeHexSequence()
 assertEquals("llo_world!", reader.consumeHexSequence());
 assertEquals("", reader.consumeHexSequence());

 // test consumeDigitSequence()
 assertEquals("123", reader.consumeDigitSequence());
 assertEquals("", reader.consumeDigitSequence());

 // test matches() with char
 assertTrue(reader.matches('l'));
 assertFalse(reader.matches('x'));

 // test matches() with CharSequence
 assertTrue(reader.matches("ello"));
 assertFalse(reader.matches("xello"));

 // test matchesIgnoreCase() with CharSequence
 assertTrue(reader.matchesIgnoreCase("world"));
 assertTrue(reader.matchesIgnoreCase("World"));

 // test matchesAny() with char
 assertTrue(reader.matchesAny('l', 'o'));
 assertFalse(reader.matchesAny('x', 'o'));

 // test matchesAnySorted() with char array
 assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
 assertFalse(reader.matchesAnySorted(new char[]{'x', 'o'}));

 // test matchesLetter()
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));

 // test matchesDigit()
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('2'));

 // test matchConsume() with CharSequence
 assertEquals("ld!", reader.matchConsume("ld!"));
 assertEquals("", reader.toString());

 // test matchConsumeIgnoreCase() with CharSequence
 assertEquals("ld!", reader.matchConsumeIgnoreCase("LD!"));
 assertEquals("", reader.toString());

 // test containsIgnoreCase() with CharSequence
 assertTrue(reader.containsIgnoreCase("world"));
 assertTrue(reader.containsIgnoreCase("World"));
 assertFalse(reader.containsIgnoreCase("hello"));
 }

}