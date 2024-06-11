import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input, reader.input);
        Assert.assertEquals(input.length(), reader.length);
        Assert.assertEquals(0, reader.pos);
        Assert.assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader("abc");

        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());

        reader.consume();
        Assert.assertEquals('b', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.current());
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
        reader.consume();
        Assert.assertEquals('a', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("bc", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("def", reader.consumeTo('f'));
        Assert.assertEquals("abc", reader.consumeTo('b'));
        Assert.assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdABCD");
        Assert.assertEquals("d", reader.consumeToAny('d'));
        Assert.assertEquals("ABCD", reader.consumeToAny('A'));
        Assert.assertEquals("", reader.consumeToAny('z'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToAnyNoMatch() {
        CharacterReader reader = new CharacterReader("abcdABCD");
        reader.consumeToAny('e');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToEndNegative() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consumeToEnd(-1);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('e'));
        Assert.assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCd");
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'd'));
        Assert.assertFalse(reader.matchesAny('e', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals('c', reader.current());
        Assert.assertTrue(reader.matchConsume("cd"));
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals('C', reader.current());
        Assert.assertTrue(reader.matchConsumeIgnoreCase("CD"));
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc<def>xyz");
        Assert.assertTrue(reader.containsIgnoreCase("def"));
        Assert.assertFalse(reader.containsIgnoreCase("ghi"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("abc", reader.toString());
    }
}