import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertEquals("", reader.toString());
    }

    @Test
    public void testCurrentChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('E', reader.current());
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consume();
        Assert.assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionOnBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp(); // IOException expected
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("HelloWorld"));
        String sequence = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", sequence);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123World"));
        String sequence = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", sequence);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x12ABCdef"));
        String sequence = reader.consumeHexSequence();
        Assert.assertEquals("12ABCdef", sequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String sequence = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", sequence);
    }

    @Test
    public void testMatchSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches('H'));
        Assert.assertFalse(reader.matches('W'));
    }

    @Test
    public void testMatchString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matches("He"));
        Assert.assertFalse(reader.matches("We"));
    }

    @Test
    public void testMatchIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesIgnoreCase("He"));
        Assert.assertTrue(reader.matchesIgnoreCase("we"));
        Assert.assertFalse(reader.matchesIgnoreCase("We"));
    }

    @Test
    public void testMatchAnySingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesAny('H'));
        Assert.assertTrue(reader.matchesAny('o'));
        Assert.assertFalse(reader.matchesAny('!'));
    }

    @Test
    public void testMatchAnySortedArray() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        char[] chars = {'o', 'H', 'W'};
        Assert.assertTrue(reader.matchesAnySorted(chars));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = "He";
        reader.matchConsume(sequence);
        Assert.assertEquals(sequence, reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String sequence = "He";
        reader.matchConsumeIgnoreCase(sequence);
        Assert.assertEquals(sequence, reader.toString());
    }

    @Test
    public void testContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head></html>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/TITLE"));
    }
}