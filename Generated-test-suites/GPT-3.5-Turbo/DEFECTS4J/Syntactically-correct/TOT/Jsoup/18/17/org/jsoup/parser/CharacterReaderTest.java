import org.junit.Test;
import org.junit.Assert;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent_emptyString() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCurrent_nonEmptyString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        Assert.assertEquals('e', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo_charFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeTo('s'));
    }

    @Test
    public void testConsumeTo_charNotFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_stringFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeTo("e"));
    }

    @Test
    public void testConsumeTo_stringNotFound() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeTo("z"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeToAny('s'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        Assert.assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");
        Assert.assertEquals("test", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        Assert.assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_charTrue() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatches_charFalse() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatches_stringTrue() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches("test"));
    }

    @Test
    public void testMatches_stringFalse() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matches("tzzt"));
    }

    @Test
    public void testMatchesIgnoreCase_stringTrue() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesIgnoreCase("Test"));
    }

    @Test
    public void testMatchesIgnoreCase_stringFalse() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesIgnoreCase("tzzt"));
    }

    @Test
    public void testMatchesAny_true() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAny('t', 'z'));
    }

    @Test
    public void testMatchesAny_false() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesAny('z', 'x'));
    }

    @Test
    public void testMatchesLetter_true() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_false() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit_true() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_false() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_stringTrue() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsume("test"));
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatchConsume_stringFalse() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsume("tzzt"));
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase_stringTrue() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Test"));
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase_stringFalse() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchConsumeIgnoreCase("tzzt"));
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testContainsIgnoreCase_true() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertTrue(reader.containsIgnoreCase("String"));
    }

    @Test
    public void testContainsIgnoreCase_false() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertFalse(reader.containsIgnoreCase("Testz"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.toString());
    }
}