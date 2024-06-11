import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader(new StringReader("Test string"));
        Assert.assertEquals("Test string", reader.toString());
    }

    @Test
    public void testCurrentChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.advance();
        Assert.assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("JavaTest"));
        String actual = reader.consumeLetterSequence();
        String expected = "Java";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Java123Test"));
        String actual = reader.consumeLetterThenDigitSequence();
        String expected = "Java123";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x12345f"));
        String actual = reader.consumeHexSequence();
        String expected = "12345f";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("12345678"));
        String actual = reader.consumeDigitSequence();
        String expected = "12345678";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        String actual = reader.consumeTo('>');
        String expected = "Test<";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        String actual = reader.consumeToAny(',', ';');
        String expected = "Test<";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        String actual = reader.consumeToAnySorted(',', ';');
        String expected = "Test<";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        String actual = reader.consumeToEnd();
        String expected = "Test<Test>";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        reader.advance();
        reader.advance();
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Test>"));
        reader.advance();
        reader.advance();
        Assert.assertFalse(reader.isEmpty());

        reader.unconsume();
        reader.unconsume();
        Assert.assertTrue(reader.isEmpty());
    }
}