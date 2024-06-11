import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.input);
        Assert.assertEquals(11, characterReader.length);
        Assert.assertEquals(0, characterReader.pos);
        Assert.assertEquals(0, characterReader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader("hello");
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        Assert.assertEquals('a', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('b', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('b', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        Assert.assertEquals('a', characterReader.consume());
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals(5, characterReader.pos);
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals(5, characterReader.pos);
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        characterReader.advance();
        Assert.assertEquals('b', characterReader.current());
        Assert.assertEquals(6, characterReader.pos);
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        characterReader.mark();
        characterReader.consume();
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals(5, characterReader.pos);
        characterReader.rewindToMark();
        Assert.assertEquals('a', characterReader.current());
        Assert.assertEquals(5, characterReader.pos);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        String consumed = characterReader.consumeAsString();
        Assert.assertEquals("a", consumed);
        Assert.assertEquals('b', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("abccdef");
        Assert.assertEquals(-1, characterReader.nextIndexOf('x'));
        Assert.assertEquals(1, characterReader.nextIndexOf('b'));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(3, characterReader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader("abccdefabc");
        Assert.assertEquals(-1, characterReader.nextIndexOf("xyz"));
        Assert.assertEquals(0, characterReader.nextIndexOf("ab"));
        characterReader.advance();
        characterReader.advance();
        Assert.assertEquals(3, characterReader.nextIndexOf("ab"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("abccdefabc");
        String consumed = characterReader.consumeTo('c');
        Assert.assertEquals("a", consumed);
        Assert.assertEquals('c', characterReader.current());
        consumed = characterReader.consumeTo('b');
        Assert.assertEquals("cdef", consumed);
        Assert.assertEquals('a', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("abccdefabc");
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("abcdefabc", consumed);
        Assert.assertEquals('\0', characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("abccdefabc123");
        String consumed = characterReader.consumeToAny('1', '2', '3');
        Assert.assertEquals("abccdef", consumed);
        Assert.assertEquals('1', characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("abcd12efg");
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals('1', characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("abcd12efg");
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abcd12", consumed);
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("abchixefghi");
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("hix", consumed);
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("ab12cd3efghi");
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", consumed);
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        Assert.assertTrue(characterReader.matches('e'));
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader("abdefghijk");
        Assert.assertTrue(characterReader.matches("def"));
        Assert.assertFalse(characterReader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("abDEFghijk");
        Assert.assertTrue(characterReader.matchesIgnoreCase("def"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("abdefghijk");
        Assert.assertTrue(characterReader.matchesAny('e', 'f'));
        Assert.assertFalse(characterReader.matchesAny('x', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("abdefghijk");
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.consumeLetterSequence();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("ab1defghijk");
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consumeDigitSequence();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        CharacterReader characterReader = new CharacterReader(null);
    }

    @Test
    public void testConsumeLetterThenDigitSequence_EdgeCase() {
        CharacterReader characterReader = new CharacterReader("123abcdef123");
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("123abcdef", consumed);
        Assert.assertEquals('1', characterReader.current());
    }

    @Test
    public void testMatchesIgnoreCase_EdgeCase() {
        CharacterReader characterReader = new CharacterReader("ABCDEFGHIJK");
        Assert.assertTrue(characterReader.matchesIgnoreCase("Def"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testConsumeToEnd_EmptyInput() {
        CharacterReader characterReader = new CharacterReader("");
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("", consumed);
        Assert.assertEquals('\0', characterReader.current());
    }
}