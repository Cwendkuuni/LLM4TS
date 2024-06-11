import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Test");
        boolean empty = reader.isEmpty();
        assertFalse(empty);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        char current = reader.current();
        assertEquals('T', current);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        char consumed = reader.consume();
        assertEquals('T', consumed);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        char current = reader.current();
        assertEquals('T', current);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        char current = reader.current();
        assertEquals('e', current);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        reader.mark();
        int pos = reader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeAsString();
        assertEquals("T", consumed);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeTo('s');
        assertEquals("Te", consumed);
    }

    @Test
    public void testConsumeToWithNonExistingChar() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeTo('z');
        assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeToWithSeq() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeTo("es");
        assertEquals("T", consumed);
    }

    @Test
    public void testConsumeToWithNonExistingSeq() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeTo("xyz");
        assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeToAny('s', 'z');
        assertEquals("Te", consumed);
    }

    @Test
    public void testConsumeToAnyWithNonExistingChars() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeToAny('x', 'y', 'z');
        assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeToEnd();
        assertEquals("Test", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test");
        String consumed = reader.consumeLetterSequence();
        assertEquals("T", consumed);
    }

    @Test
    public void testConsumeLetterSequenceWithNonLetter() {
        CharacterReader reader = new CharacterReader("1234");
        String consumed = reader.consumeLetterSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        String consumed = reader.consumeHexSequence();
        assertEquals("ABC123", consumed);
    }

    @Test
    public void testConsumeHexSequenceWithNonHex() {
        CharacterReader reader = new CharacterReader("XYZ");
        String consumed = reader.consumeHexSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234");
        String consumed = reader.consumeDigitSequence();
        assertEquals("1234", consumed);
    }

    @Test
    public void testConsumeDigitSequenceWithNonDigit() {
        CharacterReader reader = new CharacterReader("XYZ");
        String consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matches('T');
        assertTrue(matches);
    }

    @Test
    public void testMatchesWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matches('X');
        assertFalse(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesIgnoreCase("teSt");
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCaseWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesIgnoreCase("ABC");
        assertFalse(matches);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesAny('T', 'e');
        assertTrue(matches);
    }

    @Test
    public void testMatchesAnyWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesAny('X', 'Y');
        assertFalse(matches);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetterWithNonMatch() {
        CharacterReader reader = new CharacterReader("1234");
        boolean matches = reader.matchesLetter();
        assertFalse(matches);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        boolean matches = reader.matchesDigit();
        assertTrue(matches);
    }

    @Test
    public void testMatchesDigitWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesDigit();
        assertFalse(matches);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matchConsume = reader.matchConsume("Te");
        assertTrue(matchConsume);
    }

    @Test
    public void testMatchConsumeWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matchConsume = reader.matchConsume("ABC");
        assertFalse(matchConsume);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matchConsume = reader.matchConsumeIgnoreCase("teSt");
        assertTrue(matchConsume);
    }

    @Test
    public void testMatchConsumeIgnoreCaseWithNonMatch() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matchConsume = reader.matchConsumeIgnoreCase("ABC");
        assertFalse(matchConsume);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        boolean contains = reader.containsIgnoreCase("IS");
        assertTrue(contains);
    }

    @Test
    public void testContainsIgnoreCaseWithNonMatch() {
        CharacterReader reader = new CharacterReader("This is a test");
        boolean contains = reader.containsIgnoreCase("XYZ");
        assertFalse(contains);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        String str = reader.toString();
        assertEquals("Test", str);
    }

}