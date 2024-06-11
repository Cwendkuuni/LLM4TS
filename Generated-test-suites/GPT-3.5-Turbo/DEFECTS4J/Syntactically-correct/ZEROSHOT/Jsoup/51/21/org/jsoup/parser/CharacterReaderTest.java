import org.junit.Assert;
import org.junit.Test;

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
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals('t', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals(-1, reader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("tes", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("te", reader.consumeTo("st"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("tes", reader.consumeToAny('s'));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("tes", reader.consumeToAnySorted('s'));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        Assert.assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html>");
        Assert.assertEquals("html", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("t1est");
        Assert.assertEquals("t1", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1A");
        Assert.assertEquals("1A", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123");
        Assert.assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesIgnoreCase("Test"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAny('t'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesAnySorted('t'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        Assert.assertTrue(reader.containsIgnoreCase("</html>"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        Assert.assertEquals("test", reader.toString());
    }

}