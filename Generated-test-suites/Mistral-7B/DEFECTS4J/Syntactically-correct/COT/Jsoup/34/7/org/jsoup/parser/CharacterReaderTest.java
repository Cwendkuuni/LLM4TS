import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals("abcdef", reader.toString());
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentAndConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.current());
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEndOfInput() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // expected exception
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");;
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
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        Assert.assertEquals("def", reader.consumeToEnd());
        Assert.assertEquals("a", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abbbac");
        Assert.assertEquals(2, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abbbac");
        Assert.assertEquals(2, reader.nextIndexOf("bb"));
        Assert.assertEquals(-1, reader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("abbbac");
        Assert.assertEquals("bb", reader.consumeTo('b'));
        Assert.assertEquals("ac", reader.consumeTo('a'));
        Assert.assertEquals("", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeTo_CharSequence() {
        CharacterReader reader = new CharacterReader("abbbac");
        Assert.assertEquals("bb", reader.consumeTo("bb"));
        Assert.assertEquals("ac", reader.consumeTo("a"));
        Assert.assertEquals("", reader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("12345abcd");
        Assert.assertEquals("5abcd", reader.consumeToAny('5', 'a', 'b', 'c', 'd'));
        Assert.assertEquals("", reader.consumeToAny('x'));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123defg");
        Assert.assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234abcd56ef");
        Assert.assertEquals("1234", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345abcd6ef");
        Assert.assertEquals("12345", reader.consumeDigitSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches("cd"));
        Assert.assertFalse(reader.matches('x'));
        Assert.assertFalse(reader.matches("xy"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd123Ef");
        Assert.assertTrue(reader.matchesIgnoreCase("cd"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        Assert.assertTrue(reader.matchesAny('d', 'c'));
        Assert.assertTrue(reader.matchesAny("cd", 'e'));
        Assert.assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345abcd6ef");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        Assert.assertTrue(reader.matchConsume("cd"));
        Assert.assertTrue(reader.matchConsume("ef"));
        Assert.assertFalse(reader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCd123Ef");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("cd"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ef"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("xy"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("head"));
        Assert.assertFalse(reader.containsIgnoreCase("footer"));
    }

}