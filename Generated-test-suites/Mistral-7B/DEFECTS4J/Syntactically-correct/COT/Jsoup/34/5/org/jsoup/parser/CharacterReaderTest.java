import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertNotNull(reader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertFalse(reader.isEmpty());

        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertEquals('t', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeTo(CharacterReader.EOF);
        Assert.assertEquals("test", consumed);
    }

    @Test
    public void testConsumeMultipleCharacters() {
        CharacterReader reader = new CharacterReader("teststring");
        String consumed = reader.consumeTo(CharacterReader.EOF);
        Assert.assertEquals("teststring", consumed);
    }

    @Test
    public void testConsumeSpecificCharacter() {
        CharacterReader reader = new CharacterReader("teststring");
        String consumed = reader.consumeTo('t');
        Assert.assertEquals("eststring", consumed);
    }

    @Test
    public void testConsumeString() {
        CharacterReader reader = new CharacterReader("testtesttest");;
        String consumed = reader.consumeTo("est");
        Assert.assertEquals("testtest", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abcdef", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a2b3c");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("0x1a2b3c", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("1234567890", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("testtesttest");;
        boolean result = reader.matches('t');
        Assert.assertTrue(result);

        result = reader.matches("est");
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestTestTest");;
        boolean result = reader.matchesIgnoreCase("est");
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test123test");;
        boolean result = reader.matchesAny('t', '3');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test123test");;
        boolean result = reader.matchesLetter();
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test123test");;
        boolean result = reader.matchesDigit();
        Assert.assertTrue(result);
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("testtesttest");;
        reader.mark();
        reader.consumeTo('t');
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("testtesttest");;
        int index = reader.nextIndexOf('t');
        Assert.assertEquals(3, index);

        index = reader.nextIndexOf("est");
        Assert.assertEquals(6, index);
    }

    @Test
    public void testNextIndexOfWithEmptySequence() {
        CharacterReader reader = new CharacterReader("test");
        int index = reader.nextIndexOf("");
        Assert.assertEquals(-1, index);
    }
}