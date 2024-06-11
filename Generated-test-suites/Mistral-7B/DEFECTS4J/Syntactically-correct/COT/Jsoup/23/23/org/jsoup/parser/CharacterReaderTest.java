import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("testString");
        Assert.assertEquals("testString", reader.input);
        Assert.assertEquals(13, reader.length);
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("testString");
        Assert.assertEquals(0, reader.pos());

        reader.consume();
        Assert.assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("testString");
        Assert.assertFalse(reader.isEmpty());

        reader.consumeToEnd();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("testString");
        Assert.assertEquals('t', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("testString");
        char c = reader.current();
        reader.consume();
        Assert.assertEquals(c, reader.current());
        reader.consume();
        Assert.assertEquals(EOF, reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("testString");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("testString");
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("testString");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("testString");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("testString");
        String str = reader.consumeAsString();
        Assert.assertEquals("test", str);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("testStringtest");
        String str = reader.consumeTo('t');
        Assert.assertEquals("est", str);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testStringtest");
        String str = reader.consumeToEnd();
        Assert.assertEquals("testStringtest", str);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test123test");
        String str = reader.consumeToAny('1', '2', '3');
        Assert.assertEquals("123", str);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("testStringtest");
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("test", str);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test%#$%&Stringtest");
        String hexSequence = reader.consumeHexSequence();
        Assert.assertEquals("%23%25%26", hexSequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123Stringtest");
        String digitSequence = reader.consumeDigitSequence();
        Assert.assertEquals("123", digitSequence);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("testStringtest");
        Assert.assertTrue(reader.matches('t'));
        Assert.assertTrue(reader.matches("est"));
        Assert.assertFalse(reader.matches('5'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("testStringtest");
        Assert.assertTrue(reader.matchesIgnoreCase("test"));
        Assert.assertTrue(reader.matchesIgnoreCase("Est"));
        Assert.assertFalse(reader.matchesIgnoreCase("Foo"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test123Stringtest");
        Assert.assertTrue(reader.matchesAny('1', '2', '3'));
        Assert.assertFalse(reader.matchesAny('4', '5', '6'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("testStringtest");
        Assert.assertTrue(reader.matchesLetter());
        reader.consumeLetterSequence();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test123Stringtest");
        Assert.assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
        Assert.assertTrue(reader.matchConsume("title"));
        Assert.assertEquals("<html><head></head>", reader.input.substring(reader.pos));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("<HTML><HEAD><TITLE>Test</TITLE></HEAD>");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Title"));
        Assert.assertEquals("<HTML><HEAD></HEAD>", reader.input.substring(reader.pos));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title><body><div>Foo</div></body></html>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("DIV"));
        Assert.assertFalse(reader.containsIgnoreCase("BAR"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToWithEmptyString() {
        CharacterReader reader = new CharacterReader("test");
        reader.consumeTo("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeToWithNullSeq() {
        CharacterReader reader = new CharacterReader("testStringtest");
        reader.consumeToAny(null);
    }

}