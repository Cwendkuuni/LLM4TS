import org.jsoup.helper.Validate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharacterReaderTest {

    @Test
    void testPos() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals(0, reader.pos());
    }

    @Test
    void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        Assertions.assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("test");
        Assertions.assertFalse(reader2.isEmpty());
    }

    @Test
    void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        Assertions.assertEquals((char)-1, reader1.current());

        CharacterReader reader2 = new CharacterReader("test");
        Assertions.assertEquals('t', reader2.current());
    }

    @Test
    void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals('t', reader.consume());
        Assertions.assertEquals('e', reader.consume());
        Assertions.assertEquals('s', reader.consume());
        Assertions.assertEquals('t', reader.consume());
        Assertions.assertEquals((char)-1, reader.consume());
    }

    @Test
    void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        Assertions.assertEquals('t', reader.consume());
    }

    @Test
    void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        Assertions.assertEquals('e', reader.consume());
    }

    @Test
    void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        Assertions.assertEquals('s', reader.consume());
        reader.rewindToMark();
        Assertions.assertEquals('e', reader.consume());
    }

    @Test
    void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("t", reader.consumeAsString());
    }

    @Test
    void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals(1, reader.nextIndexOf('e'));

        reader.consume();
        Assertions.assertEquals(-1, reader.nextIndexOf('e'));
    }

    @Test
    void testNextIndexOf_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals(0, reader.nextIndexOf("test"));

        reader.consume();
        Assertions.assertEquals(-1, reader.nextIndexOf("test"));
    }

    @Test
    void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("t", reader.consumeTo('e'));

        reader.consume();
        Assertions.assertEquals("st", reader.consumeTo('e'));
    }

    @Test
    void testConsumeTo_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("", reader.consumeTo("test"));

        reader.consume();
        Assertions.assertEquals("", reader.consumeTo("test"));
    }

    @Test
    void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("", reader.consumeToAny('t'));

        reader.consume();
        Assertions.assertEquals("es", reader.consumeToAny('t'));
    }

    @Test
    void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("test", reader.consumeToEnd());

        reader.consume();
        Assertions.assertEquals("est", reader.consumeToEnd());
    }

    @Test
    void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertEquals("test", reader.consumeLetterSequence());

        CharacterReader reader2 = new CharacterReader("123test");
        Assertions.assertEquals("", reader2.consumeLetterSequence());
    }

    @Test
    void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        Assertions.assertEquals("test123", reader.consumeLetterThenDigitSequence());

        CharacterReader reader2 = new CharacterReader("123test");
        Assertions.assertEquals("", reader2.consumeLetterThenDigitSequence());
    }

    @Test
    void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("A1B2C3");
        Assertions.assertEquals("A1B2C3", reader.consumeHexSequence());

        CharacterReader reader2 = new CharacterReader("G1B2C3");
        Assertions.assertEquals("", reader2.consumeHexSequence());
    }

    @Test
    void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        Assertions.assertEquals("123", reader.consumeDigitSequence());

        CharacterReader reader2 = new CharacterReader("abc");
        Assertions.assertEquals("", reader2.consumeDigitSequence());
    }

    @Test
    void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matches('t'));

        reader.consume();
        Assertions.assertFalse(reader.matches('t'));
    }

    @Test
    void testMatches_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matches("test"));

        reader.consume();
        Assertions.assertFalse(reader.matches("test"));
    }

    @Test
    void testMatchesIgnoreCase_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matchesIgnoreCase("TEST"));

        reader.consume();
        Assertions.assertFalse(reader.matchesIgnoreCase("TEST"));
    }

    @Test
    void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matchesAny('t'));

        reader.consume();
        Assertions.assertFalse(reader.matchesAny('t'));
    }

    @Test
    void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matchesLetter());

        CharacterReader reader2 = new CharacterReader("123");
        Assertions.assertFalse(reader2.matchesLetter());
    }

    @Test
    void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        Assertions.assertTrue(reader.matchesDigit());

        CharacterReader reader2 = new CharacterReader("abc");
        Assertions.assertFalse(reader2.matchesDigit());
    }

    @Test
    void testMatchConsume_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matchConsume("test"));
        Assertions.assertEquals((char)-1, reader.current());

        reader.consume();
        Assertions.assertFalse(reader.matchConsume("test"));
        Assertions.assertEquals('e', reader.current());
    }

    @Test
    void testMatchConsumeIgnoreCase_seq() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        Assertions.assertEquals((char)-1, reader.current());

        reader.consume();
        Assertions.assertFalse(reader.matchConsumeIgnoreCase("TEST"));
        Assertions.assertEquals('e', reader.current());
    }

    @Test
    void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assertions.assertTrue(reader.containsIgnoreCase("TES"));

        CharacterReader reader2 = new CharacterReader("test");
        Assertions.assertFalse(reader2.containsIgnoreCase("ABC"));
    }

    @Test
    void testToString() {
        CharacterReader reader1 = new CharacterReader("");
        Assertions.assertEquals("", reader1.toString());

        CharacterReader reader2 = new CharacterReader("test");
        Assertions.assertEquals("test", reader2.toString());
    }
}