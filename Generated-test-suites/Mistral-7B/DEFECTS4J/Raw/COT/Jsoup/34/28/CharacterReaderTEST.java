import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdefg";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals("abcdefg", reader.toString());
        Assert.assertEquals(0, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // should throw an exception
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
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
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        String result = reader.consumeAsString();
        Assert.assertEquals("abc", result);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(0, reader.nextIndexOf("ab"));
        Assert.assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        String result = reader.consumeTo('b');
        Assert.assertEquals("a", result);
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abcabc");
        String result = reader.consumeTo("ab");
        Assert.assertEquals("abc", result);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("1234abc");
        String result = reader.consumeToAny('1', '2');
        Assert.assertEquals("12", result);
        Assert.assertEquals('3', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        String result = reader.consumeToEnd();
        Assert.assertEquals("abc", result);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd");
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("abcd", reader.toString());
        Assert.assertEquals("abc", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab1cd");
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab1", result);
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("%#F0F0F0");
        String result = reader.consumeHexSequence();
        Assert.assertEquals("F0F0F0", result);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('b'));
        reader.consume();
        Assert.assertFalse(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches("ef"));
        reader.consumeLetterSequence();
        Assert.assertTrue(reader.matches("cd"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
        Assert.assertFalse(reader.matchesIgnoreCase("ef"));
        reader.consumeLetterSequence();
        Assert.assertTrue(reader.matchesIgnoreCase("cd"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertFalse(reader.matchesAny('e', 'f'));
        reader.consumeLetterSequence();
        Assert.assertTrue(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
        reader.consumeLetterSequence();
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123456");
        Assert.assertFalse(reader.matchesDigit());
        reader.consumeDigitSequence();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        String result = reader.consumeTo("ab").toString();
        Assert.assertEquals("cd", result);
        Assert.assertEquals('c', reader.current());
        Assert.assertTrue(reader.matchConsume("cd"));
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdEf");
        String result = reader.consumeTo("ab").toString();
        Assert.assertEquals("Cd", result);
        Assert.assertEquals('E', reader.current());
        Assert.assertTrue(reader.matchConsumeIgnoreCase("cd"));
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("</title><style>");
        Assert.assertTrue(reader.containsIgnoreCase("</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("</style>"));
    }
}