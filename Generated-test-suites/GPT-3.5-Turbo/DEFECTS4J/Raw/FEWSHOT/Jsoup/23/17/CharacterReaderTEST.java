
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        
        reader.consume();
        assertFalse(reader.isEmpty());

        reader.consumeToEnd();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('a', reader.current());
        
        reader.consume();
        assertEquals('b', reader.current());
        
        reader.consume();
        assertEquals('c', reader.current());
        
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
        
        reader.consume();
        assertEquals('c', reader.current());
        
        reader.unconsume();
        assertEquals('b', reader.current());
        
        reader.consume();
        assertEquals('c', reader.current());
        
        reader.unconsume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        reader.advance();
        assertEquals('b', reader.current());
        
        reader.advance();
        assertEquals('c', reader.current());
        
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        reader.mark();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());

        reader.advance();
        assertEquals('b', reader.current());
        
        reader.advance();
        assertEquals('c', reader.current());
        
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("abc", reader.consumeTo('1'));
        assertEquals("123", reader.consumeTo('4'));
        assertEquals("", reader.consumeTo('5'));
    }

    @Test
    public void testConsumeToAny() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("", reader.consumeToAny('a'));
        assertEquals("a", reader.consumeToAny('b'));
        assertEquals("b", reader.consumeToAny('c'));
        assertEquals("c", reader.consumeToAny('1'));
        assertEquals("1", reader.consumeToAny('2', '3'));
        assertEquals("23", reader.consumeToAny('4'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("abc123", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("", reader.consumeHexSequence());
        
        input = "0123ABC";
        reader = new CharacterReader(input);
        assertEquals("0123ABC", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals("", reader.consumeDigitSequence());
        
        input = "0123";
        reader = new CharacterReader(input);
        assertEquals("0123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
        
        reader.consume();
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('a'));
        
        reader.consume();
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('b'));
        
        reader.consume();
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertFalse(reader.matchesIgnoreCase("dEf"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("aBc"));
        assertTrue(reader.matchesIgnoreCase("bCd"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("bCd"));
        assertTrue(reader.matchesIgnoreCase("cDE"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("cDE"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        
        reader.consume();
        assertFalse(reader.matchesAny('a', 'b'));
        
        reader.consume();
        assertFalse(reader.matchesAny('b'));
        
        reader.consume();
        assertFalse(reader.matchesAny('c'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "abc123";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertTrue(reader.matchesDigit());

        reader.consumeToEnd();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("abc"));
        
        reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("abc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(input);
        
        assertFalse(reader.containsIgnoreCase("def"));
        assertTrue(reader.containsIgnoreCase("abc"));
        
        input = "ABC";
        reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("def"));
    }
}
