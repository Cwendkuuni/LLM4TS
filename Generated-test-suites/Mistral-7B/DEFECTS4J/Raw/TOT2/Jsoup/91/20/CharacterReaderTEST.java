import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(13, reader.pos());
        Assert.assertEquals(13, reader.readerPos);
        Assert.assertEquals(13, reader.bufPos);

        reader.bufferUp();
        Assert.assertEquals(13, reader.pos());
        Assert.assertEquals(26, reader.readerPos);
        Assert.assertEquals(13, reader.bufPos);

        reader.bufferUp();
        Assert.assertEquals(13, reader.pos());
        Assert.assertEquals(40, reader.readerPos);
        Assert.assertEquals(13, reader.bufPos);

        reader.bufferUp();
        Assert.assertEquals(-1, reader.pos());
        Assert.assertEquals(40, reader.readerPos);
        Assert.assertEquals(-1, reader.bufPos);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufPos);

        reader.bufferUp();
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(0, reader.bufPos);

        reader.consume();
        Assert.assertFalse(reader.isEmpty());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(0, reader.readerPos);
        Assert.assertEquals(1, reader.bufPos);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertEquals(-1, reader.nextIndexOf('x'));
        Assert.assertEquals(6, reader.nextIndexOf('W'));
        Assert.assertEquals(12, reader.nextIndexOf('l'));
        Assert.assertEquals(13, reader.nextIndexOf('\u000D'));
        Assert.assertEquals(13, reader.nextIndexOf('\r'));

        reader.mark();
        reader.rewindToMark();

        Assert.assertEquals(11, reader.nextIndexOf('r'));
        Assert.assertEquals(12, reader.nextIndexOf('o'));
        Assert.assertEquals(13, reader.nextIndexOf('l'));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String expected = "Hello";
        String result = reader.consumeTo('l');
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());

        expected = "World";
        result = reader.consumeTo('W');
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "";
        result = reader.consumeToEnd();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "World";
        reader.mark();
        result = reader.consumeToEnd();
        reader.rewindToMark();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String expected = "Hello";
        String result = reader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());

        expected = "World";
        result = reader.consumeToAny(new char[]{'W'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "";
        result = reader.consumeToAny(new char[]{'\u0001'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "World";
        reader.mark();
        result = reader.consumeToAny(new char[]{'W'});
        reader.rewindToMark();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String expected = "Hello";
        String result = reader.consumeToAnySorted(new char[]{'l', 'o'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());

        expected = "World";
        result = reader.consumeToAnySorted(new char[]{'W'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "";
        result = reader.consumeToAnySorted(new char[]{'\u0001', 'W'});
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());

        expected = "World";
        reader.mark();
        result = reader.consumeToAnySorted(new char[]{'W'});
        reader.rewindToMark();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello & World"));

        String expected = "Hello &";
        String result = reader.consumeData();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(7, reader.pos());

        expected = "";
        result = reader.consumeData();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));

        String expected = "<html><head>";
        String result = reader.consumeTagName();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(12, reader.pos());

        expected = "</head>";
        result = reader.consumeTagName();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(21, reader.pos());

        expected = "";
        result = reader.consumeTagName();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(22, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        String expected = "Hello";
        String result = reader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(6, reader.pos());

        expected = "";
        result = reader.consumeLetterSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(13, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123 World"));

        String expected = "Hello123";
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(12, reader.pos());

        expected = "";
        result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(18, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello \u0041\u0042 World"));

        String expected = "Hello \u0041\u0042";
        String result = reader.consumeHexSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(11, reader.pos());

        expected = "";
        result = reader.consumeHexSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(17, reader.pos());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello 123 World"));

        String expected = "Hello 123";
        String result = reader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(8, reader.pos());

        expected = "";
        result = reader.consumeDigitSequence();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(14, reader.pos());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches('l', 4));
        Assert.assertFalse(reader.matches('x'));

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matches('l'));
        Assert.assertFalse(reader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("L"));
        Assert.assertTrue(reader.matchesIgnoreCase("w"));
        Assert.assertTrue(reader.matchesIgnoreCase("W"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("L"));
        Assert.assertTrue(reader.matchesIgnoreCase("w"));
        Assert.assertTrue(reader.matchesIgnoreCase("W"));
        Assert.assertFalse(reader.matchesIgnoreCase("x"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertTrue(reader.matchesAny('l', 'o', 3));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}, 3));
        Assert.assertFalse(reader.matchesAny('x'));

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matchesAny('l', 'o'));
        Assert.assertTrue(reader.matchesAny('l', 'o', 3));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}, 3));
        Assert.assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}, 3));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x'}));

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}, 3));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));

        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter(), 1);
        Assert.assertFalse(reader.matchesLetter());

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123 World"));

        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit(), 6);
        Assert.assertFalse(reader.matchesDigit());

        reader.mark();
        reader.rewindToMark();

        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }
}