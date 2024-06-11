import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.pos() == 0);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        assert(!reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.current() == 't');
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consume() == 't');
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assert(reader.pos() == 0);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assert(reader.pos() == 1);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        assert(reader.pos() == 0);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assert(reader.pos() == 1);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consumeAsString().equals("t"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consumeTo('e').equals("t"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consumeToAny('e').equals("t"));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consumeToEnd().equals("test"));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.consumeLetterSequence().equals("test"));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ff");
        assert(reader.consumeHexSequence().equals("ff"));
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assert(reader.consumeDigitSequence().equals("123"));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matches('t'));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matches("te"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matchesIgnoreCase("Te"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matchesAny('t', 'x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("t");
        assert(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1");
        assert(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.matchConsumeIgnoreCase("Te"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.containsIgnoreCase("Te"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assert(reader.toString().equals("test"));
    }
}