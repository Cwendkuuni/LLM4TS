import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input, reader.input);
        Assert.assertEquals(input.length(), reader.length);
        Assert.assertEquals(0, reader.pos);
        Assert.assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        reader = new CharacterReader("Hello World");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        // check EOF
        Assert.assertEquals(-1, reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "World";
        Assert.assertEquals(expected, reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "World";
        Assert.assertEquals(expected, reader.consumeTo('l').trim());
        reader.consume();
        expected = "ld";
        Assert.assertEquals(expected, reader.consumeTo('d').trim());
        reader.consume();
        Assert.assertEquals("", reader.consumeTo('a').trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "llo";
        Assert.assertEquals(expected, reader.consumeToAny('l'.toString()).trim());
        expected = "";
        Assert.assertEquals(expected, reader.consumeToAny('z'.toString()).trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "World";
        Assert.assertEquals(expected, reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "llo";
        Assert.assertEquals(expected, reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1a");
        String expected = "1a";
        Assert.assertEquals(expected, reader.consumeHexSequence());

        reader = new CharacterReader("Hello World");
        Assert.assertNull(reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        String expected = "5";
        Assert.assertEquals(expected, reader.consumeDigitSequence());

        reader = new CharacterReader("Hello World");
        Assert.assertNull(reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("oo"));

        reader.consume();
        Assert.assertFalse(reader.matches('l'));
        Assert.assertTrue(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesIgnoreCase("Ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("oo"));

        reader.consume();
        Assert.assertTrue(reader.matchesIgnoreCase("e"));
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchConsumeInvalid() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.matchConsume("ll");
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        String expected = "llo";
        Assert.assertEquals(expected, reader.consumeTo("ll").trim());
        Assert.assertTrue(reader.matchConsume("ll"));
        Assert.assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesAny('l', 'e'));
        Assert.assertFalse(reader.matchesAny('z', 'z'));

        reader.consume();
        Assert.assertTrue(reader.matchesAny('e', 'l'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }
}