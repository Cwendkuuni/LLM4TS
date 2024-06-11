import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input.toCharArray().length, reader.length);
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
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume(); // should be EOF
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(EOF, reader.current());

        reader = new CharacterReader("abcdef");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('f', reader.consume());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('a', reader.current());

        reader = new CharacterReader("abcdef");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.consume();
        Assert.assertEquals('a', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());

        reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        Assert.assertEquals('b', reader.current());

        reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        Assert.assertEquals('c', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));

        reader = new CharacterReader("abccabc");
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(3, reader.nextIndexOf('c'));
        Assert.assertEquals(6, reader.nextIndexOf('c'));
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("bc", reader.consumeAsString());
        Assert.assertEquals(EOF, reader.consumeAsString());

        reader = new CharacterReader("abcdef");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("def", reader.consumeAsString());
    }

    @Test(expected = AssertionError.class)
    public void testConsumeBeyondEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // should be EOF

        String input = "abc";
        reader = new CharacterReader(input);
        String consumed = reader.consumeAsString();
        Assert.assertEquals(input.length(), consumed.length());
        Assert.assertEquals(input, consumed);
        reader.consume(); // should throw an AssertionError
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdabc");
        String consumed = reader.consumeTo('b');
        Assert.assertEquals("a", consumed);

        consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);

        consumed = reader.consumeTo('e');
        Assert.assertEquals("abcd", consumed);

        consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdabc", consumed);

        reader = new CharacterReader("abcdabcd");
        consumed = reader.consumeTo('d');
        Assert.assertEquals("abcd", consumed);

        consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdabcd", consumed);
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("abcdabc");
        String consumed = reader.consumeTo("ba");
        Assert.assertEquals("ab", consumed);

        consumed = reader.consumeTo("cd");
        Assert.assertEquals("abcd", consumed);

        consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdabc", consumed);

        reader = new CharacterReader("abcdabcd");
        consumed = reader.consumeTo("ab");
        Assert.assertEquals("ab", consumed);

        consumed = reader.consumeToEnd();
        Assert.assertEquals("abcdabcd", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        String consumed = reader.consumeToAny('a', 'b');
        Assert.assertEquals("ab", consumed);

        consumed = reader.consumeToAny('c', 'd');
        Assert.assertEquals("cd", consumed);

        consumed = reader.consumeToAny();
        Assert.assertEquals("abcdabcd", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdabc");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abcd", consumed);

        consumed = reader.consumeLetterSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));

        reader = new CharacterReader("abcdabcd");
        consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abcd", consumed);

        consumed = reader.consumeLetterSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("ab1cd2ef");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab1", consumed);

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("cd2", consumed);

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));

        reader = new CharacterReader("ab12cd2ef");
        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab1", consumed);

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("cd2", consumed);

        consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab1cd%ef");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("ab1cd", consumed);

        consumed = reader.consumeHexSequence();
        Assert.assertEquals("%ef", consumed);

        reader = new CharacterReader("ab1cd%efgh");
        consumed = reader.consumeHexSequence();
        Assert.assertEquals("ab1cd", consumed);

        consumed = reader.consumeHexSequence();
        Assert.assertEquals("%ef", consumed);

        consumed = reader.consumeHexSequence();
        Assert.assertEquals("gh", consumed);

        consumed = reader.consumeHexSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ab1cd2ef");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("2", consumed);

        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("ef", consumed);

        reader = new CharacterReader("ab1cd2efghi");
        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("2", consumed);

        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("ef", consumed);

        consumed = reader.consumeDigitSequence();
        Assert.assertEquals("ghi", consumed);

        consumed = reader.consumeDigitSequence();
        Assert.assertEquals(EOF, consumed.charAt(0));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertFalse(reader.matches('x'));

        reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCabc");
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));

        reader = new CharacterReader("ABCabcDEF");
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("E"));
        Assert.assertTrue(reader.matchesIgnoreCase("F"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabc");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'c'));
        Assert.assertTrue(reader.matchesAny('b', 'c'));
        Assert.assertFalse(reader.matchesAny('x', 'y'));

        reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('a', 'c'));
        Assert.assertTrue(reader.matchesAny('b', 'c'));
        Assert.assertTrue(reader.matchesAny('d', 'd'));
        Assert.assertFalse(reader.matchesAny('x', 'y'));

        reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'c'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'b', 'c'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabc");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());

        reader = new CharacterReader("abcdabcd");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("ab1cd2ef");
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());

        reader = new CharacterReader("ab1cd2efghi");
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}