import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setUp() {
        String input = "This is a test input string.";
        characterReader = new CharacterReader(input);
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
        assertEquals('i', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('h', characterReader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("T", characterReader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(12, characterReader.nextIndexOf('s'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }
    
    @Test
    public void testNextIndexOfWithCharSequence() {
        CharSequence seq = "is";
        assertEquals(2, characterReader.nextIndexOf(seq));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("This", characterReader.consumeTo(' '));
        assertEquals("is", characterReader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToWithSeq() {
        CharSequence seq = "test";
        assertEquals("This is a ", characterReader.consumeTo(seq));
        assertEquals("input string.", characterReader.consumeTo(seq));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("Th", characterReader.consumeToAny('s', 'i'));
        assertEquals("is ", characterReader.consumeToAny('i', 'n'));
    }
    
    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Th", characterReader.consumeToAnySorted('i', 'n', 's'));
        assertEquals("is ", characterReader.consumeToAnySorted('i', 'n', 's'));
    }
    
    @Test
    public void testConsumeData() {
        assertEquals("This", characterReader.consumeData());
        characterReader.advance();
        assertEquals(" ", characterReader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("This", characterReader.consumeTagName());
        characterReader.advance();
        assertEquals(" ", characterReader.consumeTagName());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test input string.", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", characterReader.consumeLetterSequence());
        characterReader.advance();
        assertEquals("is", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", characterReader.consumeLetterThenDigitSequence());
        characterReader.advance();
        assertEquals("is", characterReader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        assertEquals("", characterReader.consumeHexSequence());
        characterReader.advance();
        assertEquals("", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.advance();
        assertEquals("", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('h'));
    }
    
    @Test
    public void testMatchesWithSeq() {
        CharSequence seq = "Thi";
        assertTrue(characterReader.matches(seq));
        assertFalse(characterReader.matches("not"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("thIS"));
        assertFalse(characterReader.matchesIgnoreCase("not"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('T', 'x'));
        assertFalse(characterReader.matchesAny('x', 'y'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('T', 'x', 'z'));
        assertFalse(characterReader.matchesAnySorted('x', 'y', 'z'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        assertFalse(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("Th"));
        assertFalse(characterReader.matchConsume("is"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("th"));
        assertFalse(characterReader.matchConsumeIgnoreCase("is"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        assertTrue(characterReader.containsIgnoreCase("test"));
        assertFalse(characterReader.containsIgnoreCase("not"));
    }
}