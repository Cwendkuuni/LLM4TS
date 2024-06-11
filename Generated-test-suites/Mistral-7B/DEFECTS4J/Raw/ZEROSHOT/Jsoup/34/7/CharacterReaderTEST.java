import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
        Assert.assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderSimple() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals("ab", reader.consumeAsString());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals("cdef", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals(3, reader.nextIndexOf('d'));
        Assert.assertEquals(4, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf('g'));
        Assert.assertEquals(0, reader.nextIndexOf('a'));
        Assert.assertEquals("def", reader.consumeTo("def"));
        Assert.assertEquals(3, reader.pos());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterReaderConsumeInvalidInput() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consumeTo('x');
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abcdef", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals("123def", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals(5, reader.pos());
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc#x12def");
        Assert.assertEquals("x12", reader.consumeHexSequence());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc#123def");
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(6, reader.length());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('e'));
        Assert.assertTrue(reader.matches("ef"));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertTrue(reader.matchesIgnoreCase("EF"));
        Assert.assertFalse(reader.matchesIgnoreCase("g"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matchesAny('e', 'f'));
        Assert.assertFalse(reader.matchesAny('g', 'f'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.containsIgnoreCase("ef"));
        Assert.assertTrue(reader.containsIgnoreCase("EF"));
        Assert.assertFalse(reader.containsIgnoreCase("g"));
    }

}