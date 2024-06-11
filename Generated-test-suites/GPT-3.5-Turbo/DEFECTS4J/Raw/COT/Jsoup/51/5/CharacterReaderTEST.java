import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    private static CharacterReader characterReader;
    
    @BeforeClass
    public static void setUp() {
        characterReader = new CharacterReader("test input");
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
        assertEquals('t', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('t', characterReader.consume());
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        assertEquals(1, characterReader.pos());
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        assertEquals("t", characterReader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf() {
        assertEquals(2, characterReader.nextIndexOf('s'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        assertEquals(1, characterReader.nextIndexOf("est"));
        assertEquals(-1, characterReader.nextIndexOf("xyz"));
    }
    
    @Test
    public void testConsumeTo() {
        assertEquals("test", characterReader.consumeTo(' '));
        assertEquals(" input", characterReader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeTo_String() {
        assertEquals("test in", characterReader.consumeTo("put"));
        assertEquals("put", characterReader.consumeTo("xyz"));
    }
    
    @Test
    public void testConsumeToAny() {
        assertEquals("test", characterReader.consumeToAny(' ', 'i'));
    }
    
    @Test
    public void testConsumeToAnySorted() {
        assertEquals("test", characterReader.consumeToAnySorted(' ', 'i'));
    }
    
    @Test
    public void testConsumeData() {
        assertEquals("test", characterReader.consumeData());
        assertEquals(" input", characterReader.consumeData());
    }
    
    @Test
    public void testConsumeTagName() {
        assertEquals("test", characterReader.consumeTagName());
        assertEquals(" input", characterReader.consumeTagName());
    }
    
    @Test
    public void testConsumeToEnd() {
        assertEquals("test input", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        characterReader = new CharacterReader("abc123");
        assertEquals("abc", characterReader.consumeLetterSequence());
        assertEquals("123", characterReader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        characterReader = new CharacterReader("abc123xyz");
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
        assertEquals("xyz", characterReader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        characterReader = new CharacterReader("ABC123xyz");
        assertEquals("ABC123", characterReader.consumeHexSequence());
        assertEquals("xyz", characterReader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        characterReader = new CharacterReader("123abc");
        assertEquals("123", characterReader.consumeDigitSequence());
        assertEquals("abc", characterReader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        assertTrue(characterReader.matches('t'));
        assertFalse(characterReader.matches('x'));
    }
    
    @Test
    public void testMatches_String() {
        assertTrue(characterReader.matches("test"));
        assertFalse(characterReader.matches("xyz"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(characterReader.matchesIgnoreCase("Test"));
        assertTrue(characterReader.matchesIgnoreCase("TEST"));
        assertFalse(characterReader.matchesIgnoreCase("XYZ"));
    }
    
    @Test
    public void testMatchesAny() {
        assertTrue(characterReader.matchesAny('t', 'e', 's'));
        assertFalse(characterReader.matchesAny('x', 'y', 'z'));
    }
    
    @Test
    public void testMatchesAnySorted() {
        assertTrue(characterReader.matchesAnySorted('e', 's', 't'));
        assertFalse(characterReader.matchesAnySorted('x', 'y', 'z'));
    }
    
    @Test
    public void testMatchesLetter() {
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertFalse(characterReader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        characterReader.consume();
        assertFalse(characterReader.matchesDigit());
        characterReader.consumeTo("1");
        assertTrue(characterReader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        assertTrue(characterReader.matchConsume("test"));
        assertFalse(characterReader.matchConsume("xyz"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(characterReader.matchConsumeIgnoreCase("Test"));
        assertFalse(characterReader.matchConsumeIgnoreCase("XYZ"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        characterReader = new CharacterReader("test title");
        assertTrue(characterReader.containsIgnoreCase("</title>"));
        assertFalse(characterReader.containsIgnoreCase("</style>"));
    }
    
    @Test
    public void testToString() {
        characterReader = new CharacterReader("test input");
        assertEquals("test input", characterReader.toString());
    }
    
    @Test
    public void testRangeEquals() {
        characterReader = new CharacterReader("test input");
        assertFalse(characterReader.rangeEquals(0, 4, "tst"));
        assertTrue(characterReader.rangeEquals(0, 4, "test"));
    }
}