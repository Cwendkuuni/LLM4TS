import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        Assert.assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("abcdef");
        Assert.assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader1.current());

        CharacterReader reader2 = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcabcdef");
        Assert.assertEquals("abc", reader.consumeTo("ab"));
        Assert.assertEquals("cabc", reader.consumeToAny('d', 'e'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeToAny('d', 'e'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123ABCdef");
        Assert.assertEquals("123ABC", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123ABCdef");
        Assert.assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.matchesIgnoreCase("ABC"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ABC"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        Assert.assertTrue(reader.containsIgnoreCase("BCD"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abcdef", reader.toString());
    }
}