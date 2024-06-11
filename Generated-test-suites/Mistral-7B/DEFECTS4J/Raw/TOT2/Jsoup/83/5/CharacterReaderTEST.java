import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals(11, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(12, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals(11, characterReader.pos());
        characterReader.advance();
        Assert.assertEquals(12, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals(11, characterReader.bufPos);
        assert characterReader.bufLength > characterReader.bufPos;

        characterReader.bufferUp();
        Assert.assertEquals(11, characterReader.bufMark);
        Assert.assertEquals(13, characterReader.bufPos);

        characterReader.advance();
        characterReader.bufferUp();
        Assert.assertEquals(13, characterReader.bufMark);
        Assert.assertEquals(15, characterReader.bufPos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.advance();
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testConsumeWhenNoMoreElements() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo('l');
        Assert.assertEquals("He", result);
        Assert.assertEquals(5, characterReader.pos());

        result = characterReader.consumeTo('r');
        Assert.assertEquals("Hello", result);
        Assert.assertEquals(6, characterReader.pos());

        result = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World", result);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAny(new char[]{'l', 'o'});
        Assert.assertEquals("He", result);

        result = characterReader.consumeToAny(new char[]{'l', 'o', 'r'});
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAnySorted(new char[]{'l', 'o'});
        Assert.assertEquals("He", result);

        result = characterReader.consumeToAnySorted(new char[]{'l', 'o', 'r'});
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World & < null"));
        String result = characterReader.consumeData();
        Assert.assertEquals("Hello World", result);
        Assert.assertEquals(11, characterReader.pos());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String result = characterReader.consumeTagName();
        Assert.assertEquals("html", result);
        Assert.assertEquals(6, characterReader.pos());

        result = characterReader.consumeTagName();
        Assert.assertEquals("head", result);
        Assert.assertEquals(11, characterReader.pos());

        result = characterReader.consumeTagName();
        Assert.assertEquals("title", result);
        Assert.assertEquals(11, characterReader.pos());

        result = characterReader.consumeTagName();
        Assert.assertEquals("Test", result);
        Assert.assertEquals(5, characterReader.pos());

        result = characterReader.consumeTagName();
        Assert.assertEquals(CharacterReader.EOF, result);
        Assert.assertEquals(5, characterReader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HelloWorld"));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
        Assert.assertEquals(5, characterReader.pos());

        result = characterReader.consumeLetterSequence();
        Assert.assertEquals("World", result);
        Assert.assertEquals(7, characterReader.pos());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World456"));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
        Assert.assertEquals(7, characterReader.pos());

        result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("World456", result);
        Assert.assertEquals(13, characterReader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&hellp; this is % a test"));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("hellp;", result);
        Assert.assertEquals(9, characterReader.pos());

        result = characterReader.consumeHexSequence();
        Assert.assertEquals("this is % a test", characterReader.toString());
        Assert.assertEquals(18, characterReader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("123456", result);
        Assert.assertEquals(6, characterReader.pos());

        result = characterReader.consumeDigitSequence();
        Assert.assertEquals("7890", result);
        Assert.assertEquals(10, characterReader.pos());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('r'));
        Assert.assertFalse(characterReader.matches("rr"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("L"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("R"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("rr"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        Assert.assertTrue(characterReader.matchesAny('l', 'o', 'r'));
        Assert.assertFalse(characterReader.matchesAny('r', 'o'));
        Assert.assertFalse(characterReader.matchesAny('r', 'o', 'r'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o', 'r'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'r', 'o'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'r', 'o', 'r'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123456"));
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo("ll");
        Assert.assertEquals("Hello", result);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertTrue(characterReader.matches("ll"));
        result = characterReader.consumeTo("ll");
        Assert.assertEquals("Hello", result);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertTrue(characterReader.matches("ll"));

        result = characterReader.consumeTo("rr");
        Assert.assertEquals("Hello World", result);
        Assert.assertEquals(11, characterReader.pos());
        Assert.assertFalse(characterReader.matches("rr"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToIgnoreCase("LL");
        Assert.assertEquals("Hello", result);
        Assert.assertEquals(5, characterReader.pos());
        Assert.assertTrue(characterReader.matchesIgnoreCase("LL"));

        result = characterReader.consumeToIgnoreCase("RR");
        Assert.assertEquals("Hello World", result);
        Assert.assertEquals(11, characterReader.pos());
        Assert.assertFalse(characterReader.matchesIgnoreCase("RR"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("/title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("/head"));
        Assert.assertFalse(characterReader.containsIgnoreCase("head"));
        Assert.assertFalse(characterReader.containsIgnoreCase("HEAD"));
    }
}