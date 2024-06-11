import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate; // for testing purpose only

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'}, characterReader.charBuf, 0, characterReader.bufLength);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'}, characterReader.charBuf, 0, characterReader.bufLength);
    }

    @Test
    public void testBufferUp() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.bufferUp();
        Assert.assertEquals(input.length(), characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.reader.reset(); // Reset reader to mark position
        characterReader.reader.close(); // Close reader
        characterReader.bufferUp(); // This should throw IOException, but it's wrapped in UncheckedIOException
    }

    @Test
    public void testIsEmpty() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
        characterReader.advance();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        characterReader.mark(); // Mark already set
    }

    @Test
    public void testNextIndexOf() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertEquals(5, characterReader.nextIndexOf(' '));
        Assert.assertEquals(7, characterReader.nextIndexOf("html"));
        Assert.assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithCaseSensitive() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input.toUpperCase()));
        Assert.assertEquals(5, characterReader.nextIndexOf("H"));
        Assert.assertEquals(7, characterReader.nextIndexOf("HTML"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("z"));
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo(' ');
        Assert.assertEquals("Hello", result);
        result = characterReader.consumeTo("html");
        Assert.assertEquals("Hello World!<", result);
    }

    @Test
    public void testConsumeToWithEmptyString() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo("");
        Assert.assertEquals("Hello World!<html>", result);
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World!<html>", input);
        Assert.assertEquals("Hello World!<html>", result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeLetterSequence();
        Assert.assertEquals("Hello", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "Hello123World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", result);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "Hello&#x61;&#x63;&#x63;&#x65;&#x73;&#x73;&#x26;"; // "Helloccss&"
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeHexSequence();
        Assert.assertEquals("ccss", result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "Hello123World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeDigitSequence();
        Assert.assertEquals("123", result);
    }

    @Test
    public void testMatches() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matches(' '));
        Assert.assertTrue(characterReader.matches("World"));
        Assert.assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesIgnoreCase("world"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesAny(new char[]{' ', 'l'}));
        Assert.assertFalse(characterReader.matchesAny(new char[]{'Z'}));
    }

    @Test
    public void testMatchesAnySorted() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', ' '}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'Z'}));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter('3'));
    }

    @Test
    public void testMatchesDigit() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertFalse(characterReader.matchesDigit());
        Assert.assertTrue(characterReader.matchesDigit('1'));
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo("html").trim();
        Assert.assertEquals("Hello World!", result);
        characterReader.matchConsume("html");
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello World!<html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        String result = characterReader.consumeTo("html").trim();
        Assert.assertEquals("Hello World!", result);
        characterReader.matchConsumeIgnoreCase("html");
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title>Test</title></head><body><div class=\"test\">Test</div></body></html>";
        CharacterReader characterReader = new CharacterReader(new StringReader(input));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("test"));
        Assert.assertFalse(characterReader.containsIgnoreCase("Z"));
    }
}