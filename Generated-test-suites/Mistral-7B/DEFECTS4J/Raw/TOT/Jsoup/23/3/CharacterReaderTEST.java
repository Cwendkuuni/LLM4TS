import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        Assert.assertNull(reader.consumeAsString());
        Assert.assertNull(reader.consumeTo('a'));
        Assert.assertNull(reader.consumeTo("ab"));
        Assert.assertNull(reader.consumeToAny('a', 'b'));
        Assert.assertNull(reader.consumeToEnd());
        Assert.assertNull(reader.consumeLetterSequence());
        Assert.assertNull(reader.consumeHexSequence());
        Assert.assertNull(reader.consumeDigitSequence());
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("AB"));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("ab"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("AB"));
        Assert.assertFalse(reader.containsIgnoreCase("AB"));
    }

    @Test
    public void testSimpleReader() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("bc", reader.consumeTo('c'));
        Assert.assertEquals("ab", reader.consumeTo("ab"));
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("12", reader.consumeDigitSequence());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertTrue(reader.containsIgnoreCase("Def"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testWhitespaceInput() {
        CharacterReader reader = new CharacterReader(" \t\r\n ");
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(' ', reader.current());
        Assert.assertEquals(" ", reader.consumeAsString());
        Assert.assertNull(reader.consumeTo('a'));
        Assert.assertNull(reader.consumeTo("ab"));
        Assert.assertNull(reader.consumeToAny('a', 'b'));
        Assert.assertNull(reader.consumeToEnd());
        Assert.assertNull(reader.consumeLetterSequence());
        Assert.assertNull(reader.consumeHexSequence());
        Assert.assertNull(reader.consumeDigitSequence());
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("ab"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertFalse(reader.containsIgnoreCase("Ab"));
    }

    @Test
    public void testEOF() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals(CharacterReader.EOF, reader.current());
        Assert.assertNull(reader.consumeAsString());
        Assert.assertNull(reader.consumeTo('a'));
        Assert.assertNull(reader.consumeTo("ab"));
        Assert.assertNull(reader.consumeToAny('a', 'b'));
        Assert.assertEquals("", reader.consumeToEnd());
        Assert.assertNull(reader.consumeLetterSequence());
        Assert.assertNull(reader.consumeHexSequence());
        Assert.assertNull(reader.consumeDigitSequence());
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesLetter());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchConsume("ab"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertFalse(reader.containsIgnoreCase("Ab"));
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        Assert.assertEquals('a', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeToEndWithMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        Assert.assertEquals("def", reader.consumeToEnd());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }
}