import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {
    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input.toCharArray(), reader.input);
        Assert.assertEquals(5, reader.length);
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
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        Assert.assertEquals('b', reader.current());

        reader = new CharacterReader("xyz");
        reader.consume();
        Assert.assertEquals('x', reader.current());

        reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.current());
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
        Assert.assertEquals("a", reader.consumeAsString());
        reader.consume();
        Assert.assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("aaabbb");
        Assert.assertEquals(1, reader.nextIndexOf('a'));
        Assert.assertEquals(2, reader.nextIndexOf('b'));
        Assert.assertEquals(-1, reader.nextIndexOf('c'));

        reader = new CharacterReader("aaabbbccc");
        Assert.assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("ababc");
        Assert.assertEquals("ab", reader.consumeTo('b'));
        Assert.assertEquals("abc", reader.consumeToEnd());

        reader = new CharacterReader("aaabbbaaa");
        Assert.assertEquals("aaa", reader.consumeTo('a'));
        Assert.assertEquals("bbb", reader.consumeTo('b'));
        Assert.assertEquals("", reader.consumeTo('c'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String data = reader.consumeToEnd();
        Assert.assertEquals("def", data);

        reader = new CharacterReader(" ");
        data = reader.consumeToEnd();
        Assert.assertEquals("", data);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("defg", reader.consumeLetterSequence());

        reader = new CharacterReader("a1b2c3");
        Assert.assertEquals("a", reader.consumeLetterSequence());
        Assert.assertEquals("123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("def456", reader.consumeLetterSequence());

        reader = new CharacterReader("123abc");
        Assert.assertEquals("123", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc#ffed123");
        Assert.assertEquals("abc#", reader.consumeHexSequence());
        Assert.assertEquals("ffed123", reader.consumeHexSequence());

        reader = new CharacterReader("abc#ffed12345");
        Assert.assertEquals("abc#ff", reader.consumeHexSequence());
        Assert.assertEquals("ed12345", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals("def456", reader.consumeDigitSequence());

        reader = new CharacterReader("abc123def456789");
        Assert.assertEquals("123", reader.consumeDigitSequence());
        Assert.assertEquals("def456789", reader.consumeDigitSequence());
    }

    @Test(expected = AssertionError.class)
    public void testMatchesInvalid() {
        CharacterReader reader = new CharacterReader("abc");
        reader.matches('x');
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertFalse(reader.matches('c'));

        reader = new CharacterReader("abcdef");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertTrue(reader.matches('e'));
        Assert.assertTrue(reader.matches('f'));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertFalse(reader.matchesIgnoreCase("c"));

        reader = new CharacterReader("ABCDEF");
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertTrue(reader.matchesIgnoreCase("e"));
        Assert.assertTrue(reader.matchesIgnoreCase("f"));
        Assert.assertFalse(reader.matchesIgnoreCase("g"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('b', 'c'));
        Assert.assertTrue(reader.matchesAny('a', 'c'));
        Assert.assertFalse(reader.matchesAny('d', 'c'));

        reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertTrue(reader.matchesAny('a', 'b'));
        Assert.assertTrue(reader.matchesAny('b', 'c'));
        Assert.assertTrue(reader.matchesAny('c', 'd'));
        Assert.assertTrue(reader.matchesAny('d', 'a'));
        Assert.assertFalse(reader.matchesAny('e', 'd'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());

        reader = new CharacterReader("ABC");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());

        reader = new CharacterReader("1234");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abab");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals(2, reader.pos);

        reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals(2, reader.pos);
        Assert.assertFalse(reader.matchConsume("xy"));
        Assert.assertEquals(3, reader.pos);

        reader = new CharacterReader("abcd");
        Assert.assertTrue(reader.matchConsume("cd"));
        Assert.assertEquals(5, reader.pos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abAB");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals(2, reader.pos);

        reader = new CharacterReader("abAB");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals(2, reader.pos);
        Assert.assertFalse(reader.matchConsumeIgnoreCase("cd"));
        Assert.assertEquals(2, reader.pos);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("TEST"));
        Assert.assertTrue(reader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(reader.containsIgnoreCase("footer"));
    }
}