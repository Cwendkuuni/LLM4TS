import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(-1, reader.nextIndexOf("ac"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeTo('d'));
        Assert.assertEquals("ef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeToAny('d', 'e'));
        Assert.assertEquals("f", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertEquals("abc", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab123");
        Assert.assertEquals("ab", reader.consumeHexSequence());
        Assert.assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab123");
        Assert.assertEquals("ab", reader.consumeDigitSequence());
        Assert.assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('b'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("ac"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesIgnoreCase("AB"));
        Assert.assertFalse(reader.matchesIgnoreCase("AC"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("abc");
        Assert.assertTrue(reader1.matchesLetter());
        CharacterReader reader2 = new CharacterReader("123");
        Assert.assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("abc");
        Assert.assertFalse(reader1.matchesDigit());
        CharacterReader reader2 = new CharacterReader("123");
        Assert.assertTrue(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("AB"));
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.containsIgnoreCase("BC"));
        Assert.assertFalse(reader.containsIgnoreCase("AC"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("abc", reader.toString());
    }
}