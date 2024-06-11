import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(0, reader.pos());
        Assert.assertEquals(11, reader.length());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        Assert.assertEquals(10, reader.pos());
        Assert.assertEquals(9, reader.length());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
        reader.consume();
        Assert.assertEquals('l', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('l', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        Assert.assertEquals('H', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeAsString();
        Assert.assertEquals("ll", str);
        reader.consume();
        reader.consume();
        str = reader.consumeAsString();
        Assert.assertEquals("rld", str);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals(-1, reader.nextIndexOf('X'));
        Assert.assertEquals(1, reader.nextIndexOf('e'));
        Assert.assertEquals(5, reader.nextIndexOf('l'));
        Assert.assertEquals(6, reader.nextIndexOf('o'));
        reader.consume();
        reader.consume();
        Assert.assertEquals(8, reader.nextIndexOf('r'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String seq = "ll";
        Assert.assertEquals(1, reader.nextIndexOf(seq));
        String seq2 = "ld";
        Assert.assertEquals(8, reader.nextIndexOf(seq2));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeTo('l');
        Assert.assertEquals("ll", str);
        reader.consume();
        str = reader.consumeTo('r');
        Assert.assertEquals("ldr", str);
        reader.consume();
        str = reader.consumeTo('d');
        Assert.assertEquals("rd", str);
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeToSequence(new char[]{'l', 'l'});
        Assert.assertEquals("ll", str);
        reader.consume();
        reader.consume();
        str = reader.consumeToSequence(new char[]{'r', 'd'});
        Assert.assertEquals("rd", str);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeToEnd();
        Assert.assertEquals("ldr", str);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeLetterSequence();
        Assert.assertEquals("Hello", str);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("Hello123World");
        String str = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("Hello123", str);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&x0F");
        String str = reader.consumeHexSequence();
        Assert.assertEquals("xF", str);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        String str = reader.consumeDigitSequence();
        Assert.assertEquals("12345", str);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matches('l'));
        Assert.assertTrue(reader.matches("ll"));
        Assert.assertFalse(reader.matches('X'));
        Assert.assertFalse(reader.matches("Xll"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesIgnoreCase("l"));
        Assert.assertTrue(reader.matchesIgnoreCase("ll"));
        Assert.assertFalse(reader.matchesIgnoreCase("X"));
        Assert.assertTrue(reader.matchesIgnoreCase("lL"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'l'}));
        Assert.assertTrue(reader.matchesAny(new char[]{'l', 'e'}));
        Assert.assertFalse(reader.matchesAny(new char[]{'X', 'l'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello123World");
        Assert.assertFalse(reader.matchesDigit());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.matchConsume("ll");
        Assert.assertEquals("ll", str);
        reader.consume();
        reader.consume();
        str = reader.matchConsume("rd");
        Assert.assertEquals("rd", str);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.matchConsumeIgnoreCase("LL");
        Assert.assertEquals("ll", str);
        reader.consume();
        reader.consume();
        str = reader.matchConsumeIgnoreCase("Rd");
        Assert.assertEquals("rd", str);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>My Title</title></head><body>");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("HEAD"));
        Assert.assertFalse(reader.containsIgnoreCase("body"));
    }

    @Test(expected = IllegalStateException.class)
    public void testToStringPastEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        String str = reader.toString();
    }

}