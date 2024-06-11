package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "testinput";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertNotNull(reader);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());

        String input = "testinput";
        reader = new CharacterReader(input);
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertEquals('t', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertEquals('t', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('s', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("testinput");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("testinput");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("testinput");
        reader.mark();
        reader.consume();
        Assert.assertEquals('t', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("testinput");
        String testString = reader.consumeAsString();
        Assert.assertEquals("test", testString);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertEquals(-1, reader.nextIndexOf('z'));
        Assert.assertEquals(1, reader.nextIndexOf('e'));
        reader.consume();
        reader.consume();
        Assert.assertEquals(2, reader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader reader = new CharacterReader("testinputtest");
        Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
        Assert.assertEquals(0, reader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("testinput");
        String consumed = reader.consumeTo('t');
        Assert.assertEquals("est", consumed);
    }

    @Test
    public void testConsumeToWithString() {
        CharacterReader reader = new CharacterReader("testinput");
        String consumed = reader.consumeTo("est");
        Assert.assertEquals("test", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test123input");
        String consumed = reader.consumeToAny(new char[]{'1', '2'});
        Assert.assertEquals("est1", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testinput");
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("testinput", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("testLetterSequence");
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("testLetter", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("testLetter1234Sequence");
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("testLetter1234", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test#1fhexSequence");
        String consumed = reader.consumeHexSequence();
        Assert.assertEquals("#1f", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test1234digitSequence");
        String consumed = reader.consumeDigitSequence();
        Assert.assertEquals("1234", consumed);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithInvalidChar() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesWithValidChar() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesWithString() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertTrue(reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCaseWithString() {
        CharacterReader reader = new CharacterReader("TestInput");
        Assert.assertTrue(reader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAnyWithValidChar() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertTrue(reader.matchesAny('t', 'e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("testinput");
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test123input");
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeWithString() {
        CharacterReader reader = new CharacterReader("testinputtestinput");
        Assert.assertTrue(reader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseWithString() {
        CharacterReader reader = new CharacterReader("TestInputTestInput");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCaseWithString() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("testinput");
        String output = reader.toString();
        Assert.assertEquals("testinput", output);
    }
}