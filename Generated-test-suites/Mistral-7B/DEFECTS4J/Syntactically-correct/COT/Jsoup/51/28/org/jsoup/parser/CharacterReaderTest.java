import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("TestString");
        Assert.assertNotNull(reader);
        Assert.assertEquals("TestString".toCharArray(), reader.input);
        Assert.assertEquals(12, reader.maxCacheLen);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 4;
        Assert.assertEquals(4, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("TestString");
        Assert.assertFalse(reader.isEmpty());
        reader.pos = reader.length();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 0;
        Assert.assertEquals('T', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = reader.length();
        reader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 0;
        Assert.assertEquals('T', reader.current());
        Assert.assertEquals('T', reader.consume());
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 1;
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 0;
        reader.advance();
        Assert.assertEquals('T', reader.current());
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 3;
        reader.mark();
        Assert.assertEquals(3, reader.mark);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.pos = 3;
        reader.mark();
        reader.rewindToMark();
        Assert.assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("TestString");
        Assert.assertEquals("Test", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("TestStringTest");
        Assert.assertEquals(3, reader.nextIndexOf('t'));
        Assert.assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("TestStringTest");
        Assert.assertEquals(5, reader.nextIndexOf("String"));
        Assert.assertEquals(-1, reader.nextIndexOf("TestX"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("TestStringTest");
        Assert.assertEquals("String", reader.consumeTo(' ').trim());
        Assert.assertEquals("Test", reader.consumeTo('T').trim());
    }

    @Test
    public void testConsumeToWithCharSequence() {
        CharacterReader reader = new CharacterReader("TestStringTest");
        Assert.assertEquals("TestString", reader.consumeTo("String").trim());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test123String");
        Assert.assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}));
        Assert.assertEquals("String", reader.consumeToAny(new char[]{'s', 't', 'r', 'i', 'n', 'g'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("Test123String");
        Assert.assertEquals("123", reader.consumeToAnySorted(new char[]{'1', '2', '3'}));
        Assert.assertEquals("String", reader.consumeToAnySorted(new char[]{'t', 'r', 'i', 'n', 'g', 's'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<nullTest");
        Assert.assertEquals("&<null", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Test</title>");
        Assert.assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("<title>Test</title>");
        Assert.assertEquals("<title>Test</title>", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("TestLetterSequence");
        Assert.assertEquals("LetterSequence", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("TestLetter123Sequence");
        Assert.assertEquals("Letter123Sequence", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test%#A%F%a%f%Test");
        Assert.assertEquals("%#A%F%a%f", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test12345Test");
        Assert.assertEquals("12345", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("TestLetterSequenceTest");
        Assert.assertTrue(reader.matches('T'));
        Assert.assertTrue(reader.matches("LetterSequence"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestLetterSequenceTest");
        Assert.assertTrue(reader.matchesIgnoreCase("lettersequence"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Test123String");
        Assert.assertTrue(reader.matchesAny(new char[]{'1', '2', '3'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'4'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("Test123String");
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'4'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("TestLetterSequenceTest");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test123String");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("TestLetterSequenceTest");
        Assert.assertTrue(reader.matchConsume("LetterSequence"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestLetterSequenceTest");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("lettersequence"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }
}