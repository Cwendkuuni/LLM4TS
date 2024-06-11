import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("testing");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("testing");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("testing");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("t", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("test", reader.consumeTo('i'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("test", reader.consumeToAny('i', 'g'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("testing", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Abc123");
        Assert.assertEquals("Abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEF");
        Assert.assertEquals("0123456789ABCDEF", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        Assert.assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesIgnoreCase("Tes"));
        Assert.assertFalse(reader.matchesIgnoreCase("Est"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesAny('e', 'i'));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchConsume("test"));
        Assert.assertFalse(reader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Tes"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("Tes"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.containsIgnoreCase("est"));
        Assert.assertFalse(reader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("testing");
        reader.consume();
        reader.consume();
        Assert.assertEquals("sting", reader.toString());
    }

}