import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello World!");
    }

    @Test
    public void testPos() {
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        boolean empty = reader.isEmpty();
        assertFalse(empty);
    }

    @Test
    public void testCurrent() {
        char current = reader.current();
        assertEquals('H', current);
    }

    @Test
    public void testConsume() {
        char consumed = reader.consume();
        assertEquals('H', consumed);
        int pos = reader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        int pos = reader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testMark() {
        reader.mark();
        int mark = reader.mark;
        assertEquals(0, mark);
    }

    @Test
    public void testRewindToMark() {
        reader.rewindToMark();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        assertEquals("H", consumed);
        int pos = reader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('o');
        assertEquals("Hell", consumed);
        int pos = reader.pos();
        assertEquals(4, pos);
    }

    @Test
    public void testConsumeToWhenCharacterNotFound() {
        String consumed = reader.consumeTo('!');
        assertEquals("Hello World", consumed);
        int pos = reader.pos();
        assertEquals(11, pos);
    }

    @Test
    public void testConsumeToWithSequence() {
        String consumed = reader.consumeTo(" Wor");
        assertEquals("Hello", consumed);
        int pos = reader.pos();
        assertEquals(5, pos);
    }

    @Test
    public void testConsumeToWithSequenceWhenNotFound() {
        String consumed = reader.consumeTo("Foo");
        assertEquals("Hello World", consumed);
        int pos = reader.pos();
        assertEquals(11, pos);
    }

    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny('o', 'W');
        assertEquals("Hell", consumed);
        int pos = reader.pos();
        assertEquals(4, pos);
    }

    @Test
    public void testConsumeToAnyWhenNoneFound() {
        String consumed = reader.consumeToAny('!', '?');
        assertEquals("Hello World", consumed);
        int pos = reader.pos();
        assertEquals(11, pos);
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        assertEquals("Hello World!", consumed);
        int pos = reader.pos();
        assertEquals(12, pos);
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        assertEquals("Hello", consumed);
        int pos = reader.pos();
        assertEquals(5, pos);
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("Hello123");
        String consumed = reader.consumeHexSequence();
        assertEquals("123", consumed);
        int pos = reader.pos();
        assertEquals(9, pos);
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("Hello123");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        int pos = reader.pos();
        assertEquals(9, pos);
    }

    @Test
    public void testMatches() {
        boolean matches = reader.matches('H');
        assertTrue(matches);
    }

    @Test
    public void testMatchesWhenFalse() {
        boolean matches = reader.matches('W');
        assertFalse(matches);
    }

    @Test
    public void testMatchesWithSequence() {
        boolean matches = reader.matches("Hello");
        assertTrue(matches);
    }
  
    @Test
    public void testMatchesWithSequenceWhenFalse() {
        boolean matches = reader.matches("World");
        assertFalse(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean matches = reader.matchesIgnoreCase("hello");
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCaseWhenFalse() {
        boolean matches = reader.matchesIgnoreCase("world");
        assertFalse(matches);
    }

    @Test
    public void testMatchesAny() {
        boolean matches = reader.matchesAny('H', 'W');
        assertTrue(matches);
    }

    @Test
    public void testMatchesAnyWhenFalse() {
        boolean matches = reader.matchesAny('A', 'B');
        assertFalse(matches);
    }

    @Test
    public void testMatchesLetter() {
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetterWhenFalse() {
        reader.consume(); // consume 'H'
        boolean matches = reader.matchesLetter();
        assertFalse(matches);
    }

    @Test
    public void testMatchesDigit() {
        boolean matches = reader.matchesDigit();
        assertFalse(matches);
    }

    @Test
    public void testMatchesDigitWhenFalse() {
        reader.consume(); // consume 'H'
        boolean matches = reader.matchesDigit();
        assertFalse(matches);
    }

    @Test
    public void testMatchConsume() {
        boolean matchConsume = reader.matchConsume("Hello");
        assertTrue(matchConsume);
        int pos = reader.pos();
        assertEquals(5, pos);
    }

    @Test
    public void testMatchConsumeWhenFalse() {
        boolean matchConsume = reader.matchConsume("World");
        assertFalse(matchConsume);
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean matchConsumeIgnoreCase = reader.matchConsumeIgnoreCase("hello");
        assertTrue(matchConsumeIgnoreCase);
        int pos = reader.pos();
        assertEquals(5, pos);
    }

    @Test
    public void testMatchConsumeIgnoreCaseWhenFalse() {
        boolean matchConsumeIgnoreCase = reader.matchConsumeIgnoreCase("world");
        assertFalse(matchConsumeIgnoreCase);
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean containsIgnoreCase = reader.containsIgnoreCase("worl");
        assertTrue(containsIgnoreCase);
    }

    @Test
    public void testContainsIgnoreCaseWhenFalse() {
        boolean containsIgnoreCase = reader.containsIgnoreCase("foo");
        assertFalse(containsIgnoreCase);
    }

    @Test
    public void testToString() {
        String str = reader.toString();
        assertEquals("Hello World!", str);
    }
}