import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testReadEmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testReadSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("A"));
        char c = reader.current();
        Assert.assertEquals('A', c);
        Assert.assertEquals("A", reader.consumeToEnd());
        reader.unconsume();
        Assert.assertEquals('A', reader.current());
    }

    @Test
    public void testReadMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        String consumed = reader.consumeTo('t');
        Assert.assertEquals("Test", reader.toString());
        Assert.assertEquals("Te", reader.toString());
        Assert.assertEquals("Test", reader.consumeToEnd());
    }

    @Test(expected = UncheckedIOException.class)
    public void testReadIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Test") {
            @Override
            public int read(char[] cbuf) throws IOException {
                throw new IOException("IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testReadNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('e'));
    }

    @Test
    public void testReadConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("TestStringTest"));
        Assert.assertEquals("String", reader.consumeToAny('s', 't'));
        Assert.assertEquals("Test", reader.consumeToAny('t'));
        Assert.assertEquals("TestString", reader.consumeToEnd());
    }

    @Test
    public void testReadConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("TestStringTest"));
        Assert.assertEquals("StringTest", reader.consumeToAnySorted('s', 't'));
        Assert.assertEquals("TestString", reader.consumeToEnd());
    }

    @Test
    public void testReadConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&amp;<tag>&lt;tag&gt;&amp;"));
        Assert.assertEquals("&amp;<tag>", reader.consumeData());
        Assert.assertEquals("&lt;tag&gt;", reader.consumeData());
        Assert.assertEquals("&amp;", reader.consumeData());
    }

    @Test
    public void testReadConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertEquals("title", reader.consumeTagName());
        Assert.assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testReadConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetterSequence"));
        Assert.assertEquals("TestLetter", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testReadConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123LetterSequence"));
        Assert.assertEquals("Test123Letter", reader.consumeLetterThenDigitSequence());
        Assert.assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testReadConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test\u0041\u0042\u0043Sequence"));
        Assert.assertEquals("Test\u0041\u0042", reader.consumeHexSequence());
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testReadConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test12345DigitSequence"));
        Assert.assertEquals("Test12345", reader.consumeDigitSequence());
        Assert.assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testReadMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatches"));
        Assert.assertTrue(reader.matches('t'));
        Assert.assertTrue(reader.matches("es"));
        Assert.assertFalse(reader.matches('a'));
        Assert.assertFalse(reader.matches("h"));
    }

    @Test
    public void testReadMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchesIgnoreCase"));
        Assert.assertTrue(reader.matchesIgnoreCase("es"));
        Assert.assertTrue(reader.matchesIgnoreCase("test"));
        Assert.assertFalse(reader.matchesIgnoreCase("A"));
        Assert.assertFalse(reader.matchesIgnoreCase("h"));
    }

    @Test
    public void testReadMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchesAny"));
        Assert.assertTrue(reader.matchesAny('t', 's'));
        Assert.assertTrue(reader.matchesAny('e', 's'));
        Assert.assertFalse(reader.matchesAny('a'));
        Assert.assertFalse(reader.matchesAny('h'));
    }

    @Test
    public void testReadMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchesAnySorted"));
        Assert.assertTrue(reader.matchesAnySorted('s', 't'));
        Assert.assertTrue(reader.matchesAnySorted('e', 't'));
        Assert.assertFalse(reader.matchesAnySorted('a'));
        Assert.assertFalse(reader.matchesAnySorted('h'));
    }

    @Test
    public void testReadMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchesLetter"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testReadMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchesDigit"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testReadMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchConsume"));
        Assert.assertTrue(reader.matchConsume("es"));
        Assert.assertEquals("st", reader.toString());
    }

    @Test
    public void testReadMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("TestMatchConsumeIgnoreCase"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("es"));
        Assert.assertEquals("st", reader.toString());
    }

    @Test
    public void testReadContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/style"));
        Assert.assertFalse(reader.containsIgnoreCase("/body"));
    }
}