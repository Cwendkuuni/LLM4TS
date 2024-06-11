import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        Assert.assertEquals(11, reader.bufLength);
        Assert.assertEquals(0, reader.bufPos);
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(-1, reader.bufMark);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        Assert.assertEquals(0, reader.pos());
        reader.advance();
        Assert.assertEquals(1, reader.pos());
        reader.consume();
        Assert.assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        reader.bufferUp();
        Assert.assertFalse(reader.isEmpty());
        reader.unconsume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        Assert.assertEquals('a', reader.current());
        reader.consume();
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(-1, reader.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc") {
            @Override
            public void mark(int readlimit) {}

            @Override
            public void reset() throws IOException {
                throw new IOException("Test IOException");
            }
        });
        reader.consume();
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        Assert.assertEquals(7, reader.nextIndexOf('g'));
        Assert.assertEquals(13, reader.nextIndexOf('x'));
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        reader.rewindToMark();
        Assert.assertEquals(7, reader.nextIndexOf('g'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        Assert.assertEquals(1, reader.nextIndexOf("ab"));
        Assert.assertEquals(13, reader.nextIndexOf("xy"));
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        reader.rewindToMark();
        Assert.assertEquals(1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        String result = reader.consumeTo('g');
        Assert.assertEquals("abcdef", result);
        reader.rewindToMark();
        result = reader.consumeTo('g');
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        reader.bufferUp();
        String result = reader.consumeToEnd();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", result);
        reader.rewindToMark();
        result = reader.consumeToEnd();
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", result);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890!"));
        reader.bufferUp();
        String result = reader.consumeToAny(new char[]{'0', '1'});
        Assert.assertEquals("1", result);
        result = reader.consumeToAny(new char[]{'1', '2'});
        Assert.assertEquals("12", result);
        result = reader.consumeToAny(new char[]{'9', '0'});
        Assert.assertEquals("9", result);
        result = reader.consumeToAny(new char[]{
                '!', '8'});
        Assert.assertEquals("!", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890!"));
        reader.bufferUp();
        String result = reader.consumeToAnySorted(new char[]{'_', '0'});
        Assert.assertEquals("", result);
        result = reader.consumeToAnySorted(new char[]{'0', '_'});
        Assert.assertEquals("0", result);
        result = reader.consumeToAnySorted(new char[]{'2', '1'});
        Assert.assertEquals("12", result);
        result = reader.consumeToAnySorted(new char[]{
                '!', '8'});
        Assert.assertEquals("!", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        reader.bufferUp();
        String result = reader.consumeData();
        Assert.assertEquals("&<", result);
        reader.rewindToMark();
        result = reader.consumeData();
        Assert.assertEquals("&<", result);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name='test'>"));
        reader.bufferUp();
        String result = reader.consumeTagName();
        Assert.assertEquals("tag", result);
        reader.rewindToMark();
        result = reader.consumeTagName();
        Assert.assertEquals("tag", result);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("<tag name='test'>test</tag>"));
        reader.bufferUp();
        String result = reader.consumeToEnd();
        Assert.assertEquals("test</tag>", result);
        reader.rewindToMark();
        result = reader.consumeToEnd();
        Assert.assertEquals("test</tag>", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijk"));
        reader.bufferUp();
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("abcdef", result);
        reader.rewindToMark();
        result = reader.consumeLetterSequence();
        Assert.assertEquals("abcdef", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ab1cd2ef"));
        reader.bufferUp();
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab1", result);
        reader.rewindToMark();
        result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab1", result);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('g'));
        reader.rewindToMark();
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches("ab"));
        Assert.assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEf"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("xy"));
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesIgnoreCase("ab"));
        Assert.assertTrue(reader.matchesIgnoreCase("ef"));
        Assert.assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'d', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'g', 'h'}));
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'d', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'g', 'h'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'h'}));
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'h'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghij"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        reader.rewindToMark();
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals("cd", reader.toString());
        reader.rewindToMark();
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals("cd", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCdEf"));
        reader.bufferUp();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals("ef", reader.toString());
        reader.rewindToMark();
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals("ef", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("/head"));
    }
}