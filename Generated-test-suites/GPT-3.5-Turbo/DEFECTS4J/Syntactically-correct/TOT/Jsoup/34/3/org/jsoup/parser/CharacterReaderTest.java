import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_notEmpty() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent_empty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCurrent_notEmpty() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume_empty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume_notEmpty() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testNextIndexOf_found() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(1, reader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOf_notFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_sequence_found() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(0, reader.nextIndexOf("te"));
    }

    @Test
    public void testNextIndexOf_sequence_notFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(-1, reader.nextIndexOf("zt"));
    }

    @Test
    public void testConsumeTo_found() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeTo('s'));
        Assert.assertEquals('s', reader.current());
    }

    @Test
    public void testConsumeTo_notFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeTo('z'));
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTo_sequence_found() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeTo("es"));
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeTo_sequence_notFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeTo("zt"));
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeToAny_found() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeToAny('s', 'z'));
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeToAny_notFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("", reader.consumeToAny('z'));
        Assert.assertEquals("test", reader.toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeToEnd());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeLetterSequence());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        Assert.assertEquals("test123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertEquals("abc123", reader.consumeHexSequence());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatches_any() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAny('t', 'e'));
        Assert.assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsume("te"));
        Assert.assertEquals('s', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Te"));
        Assert.assertEquals('s', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("TEST");
        Assert.assertTrue(reader.containsIgnoreCase("es"));
        Assert.assertTrue(reader.containsIgnoreCase("ST"));
        Assert.assertFalse(reader.containsIgnoreCase("esST"));
    }
}