import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(5, reader.length());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader("abcdef");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals(EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
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
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("bc", reader.consumeAsString());
        Assert.assertEquals(EOF, reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdeabcde");
        Assert.assertEquals(5, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        Assert.assertEquals(6, reader.nextIndexOf("def"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("a", reader.consumeTo('b').toString());
        Assert.assertEquals("bc", reader.consumeTo('c').toString());
        Assert.assertEquals("def", reader.consumeTo('e').toString());
        Assert.assertEquals("", reader.consumeTo('z').toString());
    }

    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader("abcdefabcdef");
        Assert.assertEquals("abcdef", reader.consumeTo("def").toString());
        Assert.assertEquals("", reader.consumeTo("xyz").toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234567890");
        Assert.assertEquals("1", reader.consumeToAny('1').toString());
        Assert.assertEquals("567890", reader.consumeToAny('0').toString());
        Assert.assertEquals("", reader.consumeToAny('a').toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("def", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("abc", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a2b3c");
        Assert.assertEquals("1a2b3c", reader.consumeHexSequence().toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertEquals("12345", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matches("d"));
        Assert.assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("AbC"));
        Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd1234ef");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertTrue(reader.matchesAny('1', '2', '3', '4'));
        Assert.assertFalse(reader.matchesAny('z', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd1234efg");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd1234efg");
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchConsume_NullSeq() {
        CharacterReader reader = new CharacterReader("abcd1234efg");
        reader.matchConsume(null);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcXYZdefXYZ");
        Assert.assertTrue(reader.containsIgnoreCase("XYZ"));
        Assert.assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("def", reader.toString());
    }
}