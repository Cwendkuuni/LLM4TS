import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.CharacterReader.EOF;
import static org.jsoup.parser.CharacterReader.consume;
import static org.jsoup.parser.CharacterReader.consumeAsString;
import static org.jsoup.parser.CharacterReader.consumeLetterSequence;
import static org.jsoup.parser.CharacterReader.consumeLetterThenDigitSequence;
import static org.jsoup.parser.CharacterReader.consumeTo;
import static org.jsoup.parser.CharacterReader.consumeToAny;
import static org.jsoup.parser.CharacterReader.isEmpty;
import static org.jsoup.parser.CharacterReader.matches;
import static org.jsoup.parser.CharacterReader.nextIndexOf;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertNotNull(reader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(isEmpty(reader));

        reader = new CharacterReader("Hello World");
        Assert.assertFalse(isEmpty(reader));
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', current(reader));
        consume(reader);
        Assert.assertEquals('e', current(reader));
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', consume(reader));
        Assert.assertEquals('e', current(reader));
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello", consumeAsString(reader));
        Assert.assertEquals('W', current(reader));
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(4, nextIndexOf(reader, 'l'));
        Assert.assertEquals(-1, nextIndexOf(reader, 'z'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(6, nextIndexOf(reader, "llo"));
        Assert.assertEquals(-1, nextIndexOf(reader, "zzz"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("llo", consumeTo(reader, 'l'));
        Assert.assertEquals("ld", consumeTo(reader, 'd'));
        Assert.assertEquals("", consumeTo(reader, 'z'));
    }

    @Test
    public void testConsumeToCharSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("llo", consumeTo(reader, "llo"));
        Assert.assertEquals("ld", consumeTo(reader, "ld"));
        Assert.assertEquals("", consumeTo(reader, "zzz"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertEquals("1", consumeToAny(reader, '1', '2'));
        Assert.assertEquals("2345", consumeToAny(reader, '3', '5'));
        Assert.assertEquals("", consumeToAny(reader, '0'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("World", consumeToEnd(reader));
        Assert.assertEquals(EOF, current(reader));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abc", consumeLetterSequence(reader));
        Assert.assertEquals("defg", consumeLetterSequence(reader));
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        Assert.assertEquals("abc123", consumeLetterThenDigitSequence(reader));
        Assert.assertEquals("def456", consumeLetterThenDigitSequence(reader));
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(matches(reader, 'l'));
        Assert.assertTrue(matches(reader, "llo"));
        Assert.assertFalse(matches(reader, 'z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(matchesIgnoreCase(reader, "llo"));
        Assert.assertTrue(matchesIgnoreCase(reader, "lo"));
        Assert.assertFalse(matchesIgnoreCase(reader, "zzz"));
    }

    @Test(expected = AssertionError.class)
    public void testMatchConsumeCharSequenceFails() {
        CharacterReader reader = new CharacterReader("Hello World");
        matchConsume(reader, "zzz");
    }

    @Test(expected = AssertionError.class)
    public void testMatchConsumeCharSequenceIgnoreCaseFails() {
        CharacterReader reader = new CharacterReader("Hello World");
        matchConsumeIgnoreCase(reader, "zzz");
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>...</body></html>");
        Assert.assertTrue(containsIgnoreCase(reader, "</title>"));
        Assert.assertTrue(containsIgnoreCase(reader, "</style>"));
        Assert.assertFalse(containsIgnoreCase(reader, "</script>"));
    }

    private char current(CharacterReader reader) {
        return reader.current();
    }

    private String consumeToEnd(CharacterReader reader) {
        return reader.consumeToEnd();
    }
}