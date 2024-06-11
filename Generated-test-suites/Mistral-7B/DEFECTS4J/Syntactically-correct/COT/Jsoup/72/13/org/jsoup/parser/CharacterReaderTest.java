import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"), 10);
        Assert.assertEquals("Hello World", characterReader.toString());
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        Assert.assertEquals(13, characterReader.pos());
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(0, characterReader.pos());

        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());

        characterReader.mark();
        characterReader.consume();
        Assert.assertEquals(2, characterReader.pos());
        characterReader.rewindToMark();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(characterReader.isEmpty());

        characterReader.consume();
        Assert.assertFalse(characterReader.isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.current();
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals('H', characterReader.consume());
        Assert.assertEquals('e', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals(' ', characterReader.consume());
        Assert.assertEquals('W', characterReader.consume());
        Assert.assertEquals('o', characterReader.consume());
        Assert.assertEquals('r', characterReader.consume());
        Assert.assertEquals('l', characterReader.consume());
        Assert.assertEquals('d', characterReader.consume());
        Assert.assertEquals('\n', characterReader.consume());
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, characterReader.nextIndexOf(' '));
        Assert.assertEquals(12, characterReader.nextIndexOf('W'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals(6, characterReader.nextIndexOf(" "));
        Assert.assertEquals(12, characterReader.nextIndexOf("W"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("X"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo(' ');
        Assert.assertEquals("Hello", consumed);
        Assert.assertEquals(' ', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        Assert.assertEquals("Hello World\n", consumed);
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcAbC"));
        String consumed = characterReader.consumeLetterSequence();
        Assert.assertEquals("abc", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123AbC"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        Assert.assertEquals("abc123", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp;<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">"));
        String consumed = characterReader.consumeHexSequence();
        Assert.assertEquals("amp;htmlxmlnshttpwwww3org1999xhmllangenxmllangen", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("123456"));
        String consumed = characterReader.consumeDigitSequence();
        Assert.assertEquals("123456", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ll"));
        Assert.assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("L"));
        Assert.assertTrue(characterReader.matchesIgnoreCase("ll"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        Assert.assertTrue(characterReader.matchesAny('l', 'd'));
        Assert.assertFalse(characterReader.matchesAny('x', 'l'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        Assert.assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'd'}));
        Assert.assertFalse(characterReader.matchesAnySorted(new char[]{'x', 'l'}));
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
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesLetter());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertFalse(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.advance();
        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test(expected = AssertionError.class)
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsume("ll");
    }

    @Test(expected = AssertionError.class)
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsumeIgnoreCase("LL");
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Hello World</title>"));
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertTrue(characterReader.containsIgnoreCase("TITLE"));
        Assert.assertFalse(characterReader.containsIgnoreCase("body"));
    }
}