import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.pos() == 0);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("hello");
        assert(!reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.current() == 'h');
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consume() == 'h');
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("hello");
        reader.consume();
        assert(reader.pos() == 1);
        reader.unconsume();
        assert(reader.pos() == 0);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("hello");
        reader.advance();
        assert(reader.pos() == 1);
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.mark();
        assert(reader.pos() == 0);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("hello");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assert(reader.pos() == 0);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeAsString().equals("h"));
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.nextIndexOf('o') == 4);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.nextIndexOf("lo") == 2);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeTo('o').equals("hell"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeToAny('e', 'o').equals("h"));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeToAnySorted('e', 'o').equals(""));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeData().equals("hello"));
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeTagName().equals("hello"));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeToEnd().equals("hello"));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.consumeLetterSequence().equals("hello"));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("hello123");
        assert(reader.consumeLetterThenDigitSequence().equals("hello123"));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("FF");
        assert(reader.consumeHexSequence().equals("FF"));
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        assert(reader.consumeDigitSequence().equals("123"));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matches('h'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchesIgnoreCase("HELLO"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchesAny('h', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchesAnySorted('a', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assert(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchConsume("he"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.matchConsumeIgnoreCase("hE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.containsIgnoreCase("HELLO"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.toString().equals("hello"));
    }

    @Test
    public void testCacheString() {
        CharacterReader reader = new CharacterReader("hello");
        assert(reader.rangeEquals(0, 5, "hello"));
    }
    
}