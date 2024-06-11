import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_nonEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent_emptyString() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals((char)-1, reader.current());
    }

    @Test
    public void testCurrent_nonEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testConsume_emptyString() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals((char)-1, reader.consume());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsume_nonEmptyString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals('T', reader.consume());
        Assert.assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test");
        reader.consume(); // position: 1
        reader.unconsume(); // position: 0
        Assert.assertEquals('T', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Test");
        reader.mark(); // marks position: 0
        reader.consume(); // position: 1
        Assert.assertEquals('e', reader.current());
        reader.rewindToMark(); // position: 0
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertEquals("T", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo_charExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("T", reader.consumeTo('i'));
        Assert.assertEquals('i', reader.consume());
    }

    @Test
    public void testConsumeTo_charNotExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("This is a test", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeTo_stringExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("This", reader.consumeTo(" is"));
        Assert.assertEquals('i', reader.consume());
    }

    @Test
    public void testConsumeTo_stringNotExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("This is a test", reader.consumeTo("foo"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("This is ", reader.consumeToAny('a', 'e'));
        Assert.assertEquals('a', reader.consume());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertEquals("This is a test", reader.consumeToEnd());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        Assert.assertEquals("ABC", reader.consumeLetterSequence());
        Assert.assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        Assert.assertEquals("", reader.consumeHexSequence());
        reader.advance();
        Assert.assertEquals("BC1", reader.consumeHexSequence());
        Assert.assertEquals("23", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("ABC123");
        Assert.assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        Assert.assertEquals("23", reader.consumeDigitSequence());
        Assert.assertEquals("3", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matches('T'));
        Assert.assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatches_allCharacters() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/~`");
        Assert.assertTrue(reader.matches('!'));
        Assert.assertTrue(reader.matches('~'));
        Assert.assertFalse(reader.matches('@'));
    }

    @Test
    public void testMatches_stringExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertTrue(reader.matches("This"));
        Assert.assertTrue(reader.matches("This is a test"));
    }

    @Test
    public void testMatches_stringNotExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertFalse(reader.matches("this"));
        Assert.assertFalse(reader.matches("This is not a test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matchesIgnoreCase("T"));
        Assert.assertTrue(reader.matchesIgnoreCase("t"));
        Assert.assertFalse(reader.matchesIgnoreCase("a"));
    }

    @Test
    public void testMatchesIgnoreCase_stringExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertTrue(reader.matchesIgnoreCase("This"));
        Assert.assertTrue(reader.matchesIgnoreCase("THIS"));
        Assert.assertTrue(reader.matchesIgnoreCase("this"));
        Assert.assertTrue(reader.matchesIgnoreCase("This is a test"));
    }

    @Test
    public void testMatchesIgnoreCase_stringNotExists() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertFalse(reader.matchesIgnoreCase("foo"));
        Assert.assertFalse(reader.matchesIgnoreCase("This is not a test"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matchesAny('T', 't'));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesAny_allCharacters() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/~`");
        Assert.assertTrue(reader.matchesAny('!', '~'));
        Assert.assertFalse(reader.matchesAny('@', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test");
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123");
        Assert.assertFalse(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertTrue(reader.matchConsume("This is"));
        Assert.assertEquals(' ', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("this is"));
        Assert.assertEquals(' ', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("This is a test");
        Assert.assertTrue(reader.containsIgnoreCase("test"));
        Assert.assertTrue(reader.containsIgnoreCase("TeSt"));
        Assert.assertFalse(reader.containsIgnoreCase("foo"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test");
        reader.advance();
        Assert.assertEquals("est", reader.toString());
    }
}