import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        boolean empty1 = reader1.isEmpty();
        assertTrue(empty1);

        CharacterReader reader2 = new CharacterReader("test");
        boolean empty2 = reader2.isEmpty();
        assertFalse(empty2);
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        char current = reader.current();
        assertEquals('t', current);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char consumed = reader.consume();
        assertEquals('t', consumed);
        char current = reader.current();
        assertEquals('e', current);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        char consumed = reader.consume();
        reader.unconsume();
        char current = reader.current();
        assertEquals('t', current);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char current = reader.current();
        assertEquals('e', current);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeAsString();
        assertEquals("t", consumed);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        int index = reader.nextIndexOf('s');
        assertEquals(2, index);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("test");
        int index = reader.nextIndexOf("st");
        assertEquals(2, index);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeTo('s');
        assertEquals("te", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeToAny('e', 's');
        assertEquals("t", consumed);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeToAnySorted('e', 's');
        assertEquals("t", consumed);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeData();
        assertEquals("test", consumed);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeTagName();
        assertEquals("test", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeToEnd();
        assertEquals("test", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeLetterSequence();
        assertEquals("test", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("test123", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");
        String consumed = reader.consumeHexSequence();
        assertEquals("test123", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matches('t');
        assertTrue(matches);
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matches("test");
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesIgnoreCase("TeSt");
        assertTrue(matches);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesAny('t', 's');
        assertTrue(matches);
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesAnySorted('s', 't');
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matchesDigit();
        assertFalse(matches);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean matchConsume = reader.matchConsume("test");
        assertTrue(matchConsume);
        int pos = reader.pos();
        assertEquals(4, pos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean matchConsume = reader.matchConsumeIgnoreCase("TeSt");
        assertTrue(matchConsume);
        int pos = reader.pos();
        assertEquals(4, pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        boolean containsIgnoreCase = reader.containsIgnoreCase("TEs");
        assertTrue(containsIgnoreCase);
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        String string = reader.toString();
        assertEquals("test", string);
    }

    @Test
    public void testRangeEquals() {
        CharacterReader reader = new CharacterReader("test");
        boolean rangeEquals = reader.rangeEquals(0, 4, "test");
        assertTrue(rangeEquals);
    }

}