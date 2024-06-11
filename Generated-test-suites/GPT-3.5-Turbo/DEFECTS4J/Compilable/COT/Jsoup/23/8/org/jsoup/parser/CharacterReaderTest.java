package org.jsoup.parser;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the pos() method
        assert reader.pos() == 0;
    }

    @Test
    public void testIsEmpty() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("");

        // Test the isEmpty() method
        assert reader.isEmpty();

        // Create another instance of CharacterReader
        CharacterReader reader2 = new CharacterReader("example");

        // Test the isEmpty() method
        assert !reader2.isEmpty();
    }

    @Test
    public void testCurrent() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the current() method
        assert reader.current() == 'e';
    }

    @Test
    public void testConsume() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consume() method
        assert reader.consume() == 'e';
        assert reader.pos() == 1;
    }

    @Test
    public void testUnconsume() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the unconsume() method
        reader.consume();
        reader.unconsume();
        assert reader.pos() == 0;
    }

    @Test
    public void testAdvance() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the advance() method
        reader.advance();
        assert reader.pos() == 1;
    }

    @Test
    public void testMark() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the mark() method
        reader.mark();
        assert reader.pos() == 0;
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assert reader.pos() == 0;
    }

    @Test
    public void testConsumeAsString() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeAsString() method
        assert reader.consumeAsString().equals("e");
    }

    @Test
    public void testConsumeTo() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeTo() method
        assert reader.consumeTo('x').equals("e");
    }

    @Test
    public void testConsumeToAny() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeToAny() method
        assert reader.consumeToAny('x', 'a').equals("ex");
    }

    @Test
    public void testConsumeToEnd() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeToEnd() method
        assert reader.consumeToEnd().equals("example");
    }

    @Test
    public void testConsumeLetterSequence() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeLetterSequence() method
        assert reader.consumeLetterSequence().equals("example");
    }

    @Test
    public void testConsumeHexSequence() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeHexSequence() method
        assert reader.consumeHexSequence().equals("");
    }

    @Test
    public void testConsumeDigitSequence() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the consumeDigitSequence() method
        assert reader.consumeDigitSequence().equals("");
    }

    @Test
    public void testMatches() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matches() method
        assert reader.matches('e');
        assert !reader.matches('a');
    }

    @Test
    public void testMatchesIgnoreCase() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchesIgnoreCase() method
        assert reader.matchesIgnoreCase("EXaMple");
    }

    @Test
    public void testMatchesAny() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchesAny() method
        assert reader.matchesAny('e', 'x');
        assert !reader.matchesAny('a');
    }

    @Test
    public void testMatchesLetter() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchesLetter() method
        assert reader.matchesLetter();
    }

    @Test
    public void testMatchesDigit() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchesDigit() method
        assert !reader.matchesDigit();
    }

    @Test
    public void testMatchConsume() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchConsume() method
        assert reader.matchConsume("exa");
        assert reader.pos() == 3;
        assert !reader.matchConsume("exa");
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the matchConsumeIgnoreCase() method
        assert reader.matchConsumeIgnoreCase("Exa");
        assert reader.pos() == 3;
        assert !reader.matchConsumeIgnoreCase("Exa");
    }

    @Test
    public void testContainsIgnoreCase() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the containsIgnoreCase() method
        assert reader.containsIgnoreCase("ex");
    }

    @Test
    public void testToString() {
        // Create an instance of CharacterReader
        CharacterReader reader = new CharacterReader("example");

        // Test the toString() method
        assert reader.toString().equals("example");
    }
}