import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertNotNull(characterReader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.consume('H');
        Assert.assertFalse(characterReader.isEmpty());

        characterReader.consume('e');
        characterReader.consume('l');
        characterReader.consume('l');
        characterReader.consume('o');
        characterReader.consume(' ');

        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals('H', characterReader.current());
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        characterReader.consume();
        Assert.assertEquals('l', characterReader.current());

        characterReader.consume('a'); // should consume 'l' and 'a'
        Assert.assertEquals('o', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consume();
        characterReader.unconsume();
        Assert.assertEquals('H', characterReader.current());

        characterReader.consume('e');
        characterReader.consume('l');
        characterReader.unconsume();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.advance();
        Assert.assertEquals('e', characterReader.current());

        characterReader.advance();
        Assert.assertEquals('l', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.mark();

        characterReader.consume();
        Assert.assertEquals('e', characterReader.current());

        characterReader.rewindToMark();
        Assert.assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello", characterReader.consumeAsString());
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals(4, characterReader.nextIndexOf('l'));
        Assert.assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals(11, characterReader.nextIndexOf("World"));
        Assert.assertEquals(-1, characterReader.nextIndexOf("Java"));
    }

    @Test(expected = AssertionError.class)
    public void testConsumeTo_Char_NegativeIndex() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        characterReader.consumeTo((char) -1);
    }

    @Test
    public void testConsumeTo_Char() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("ello", characterReader.consumeTo('l'));
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("ello", characterReader.consumeTo("ll"));
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        Assert.assertEquals("ll3", characterReader.consumeToAny('l', '3'));
        Assert.assertEquals('W', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("ello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        Assert.assertEquals("ello123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("0x12345678");
        Assert.assertEquals("12345678", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("12345678");
        Assert.assertEquals("12345678", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matches('l'));
        Assert.assertTrue(characterReader.matches("ello"));
        Assert.assertFalse(characterReader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesIgnoreCase("ello"));
        Assert.assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesAny('l', 'o'));
        Assert.assertFalse(characterReader.matchesAny('z', 'o'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertTrue(characterReader.matchesLetter());
        Assert.assertFalse(characterReader.matchesLetter());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("Hello123World");
        Assert.assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("<html><title>Hello World</title></html>");
        Assert.assertTrue(characterReader.containsIgnoreCase("title"));
        Assert.assertFalse(characterReader.containsIgnoreCase("Java"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        Assert.assertEquals("lloWorld", characterReader.toString());
    }
}