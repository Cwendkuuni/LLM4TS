import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader("Test");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals('T', reader.current());

        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals('T', reader.consume());
        Assert.assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals("T", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals(1, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals(1, reader.nextIndexOf("es"));
        Assert.assertEquals(-1, reader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeTo('1'));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeTo("123"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeToAny('1', '2'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeToAnySorted('1', '2'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertEquals("Test123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        Assert.assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        Assert.assertEquals("0123456789", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matches('T'));
        Assert.assertFalse(reader.matches('X'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matches("Test"));
        Assert.assertFalse(reader.matches("TestX"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchesIgnoreCase("TEST"));
        Assert.assertFalse(reader.matchesIgnoreCase("TESTX"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchesAny('T', 'X'));
        Assert.assertFalse(reader.matchesAny('X'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchesAnySorted('T', 'X'));
        Assert.assertFalse(reader.matchesAnySorted('X'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123");
        reader.consumeTo('1');
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchConsume("Test"));
        Assert.assertFalse(reader.matchConsume("TestX"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("TESTX"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertTrue(reader.containsIgnoreCase("EST"));
        Assert.assertFalse(reader.containsIgnoreCase("XYZ"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume();
        Assert.assertEquals("est", reader.toString());
    }

}