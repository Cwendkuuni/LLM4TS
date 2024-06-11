import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(EOF, reader.current());
        Assert.assertFalse(reader.matchesIgnoreCase("abc"));
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        Assert.assertTrue(reader.matches("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.consume();
        Assert.assertEquals('c', reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
        Assert.assertTrue(reader.matches("abc"));
        Assert.assertTrue(reader.matchesIgnoreCase("abc"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
        reader.consumeTo('d');
        Assert.assertEquals(6, reader.pos());
        Assert.assertEquals(4, reader.nextIndexOf('d'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String consumed = reader.consumeTo('d');
        Assert.assertEquals("abc", consumed);
        Assert.assertEquals(4, reader.pos());

        consumed = reader.consumeTo("abcd");
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals(8, reader.pos());

        consumed = reader.consumeTo("xyz");
        Assert.assertEquals("", consumed);
        Assert.assertEquals(12, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String consumed = reader.consumeToAny(new char[]{'a', 'b'});
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals(6, reader.pos());

        consumed = reader.consumeToAny(new char[]{'x', 'y', 'z'});
        Assert.assertEquals("", consumed);
        Assert.assertEquals(12, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        String consumed = reader.consumeToAnySorted(new char[]{'b', 'd', 'a'});
        Assert.assertEquals("abcd", consumed);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;&amp;&gt;"));
        String consumed = reader.consumeData();
        Assert.assertEquals("&lt;&amp;&gt;", consumed);
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("tag", consumed);
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", consumed);
        Assert.assertEquals(45, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("a1b2c3"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("a1b2c3", consumed);
        Assert.assertEquals(6, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x0F"));
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("xF", consumed);
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("12345", consumed);
        Assert.assertEquals(5, reader.pos());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));
        Assert.assertFalse(reader.matches('f'));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEf"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
        Assert.assertFalse(reader.matchesIgnoreCase("f"));
        Assert.assertFalse(reader.matchesIgnoreCase("g"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a'));
        Assert.assertTrue(reader.matchesAny('b'));
        Assert.assertTrue(reader.matchesAny('c'));
        Assert.assertTrue(reader.matchesAny('d'));
        Assert.assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted('a'));
        Assert.assertTrue(reader.matchesAnySorted('b'));
        Assert.assertTrue(reader.matchesAnySorted('c'));
        Assert.assertTrue(reader.matchesAnySorted('d'));
        Assert.assertFalse(reader.matchesAnySorted('e'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter()); // no more letters
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit()); // no more digits
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertTrue(reader.matchConsume("a"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertTrue(reader.matchConsume("b"));
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals('c', reader.current());
        Assert.assertTrue(reader.matchConsume("c"));
        Assert.assertEquals(3, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbC"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("a"));
        Assert.assertEquals(1, reader.pos());
        Assert.assertTrue(reader.matchConsumeIgnoreCase("B"));
        Assert.assertEquals(2, reader.pos());
        Assert.assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("<title>"));
        Assert.assertTrue(reader.containsIgnoreCase("test"));
        Assert.assertFalse(reader.containsIgnoreCase("<body>"));
    }

}