import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        Assert.assertEquals(input, reader.input);
        Assert.assertEquals(5, reader.length);
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
        Assert.assertEquals('H', reader.current());
        reader.consume();
        Assert.assertEquals('e', reader.current());
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
        String actual = reader.consumeAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        String actual = reader.consumeTo('d');
        Assert.assertEquals(expected, actual);

        reader.rewindToMark();
        reader.consume();
        expected = "abc";
        actual = reader.consumeTo('d');
        Assert.assertEquals(expected, actual);

        reader.consumeToEnd();
        reader.consume('d');
        reader.mark();
        reader.consume('d');
        expected = "";
        actual = reader.consumeTo('d');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        String expected = "abcd";
        String actual = reader.consumeToAny('d');
        Assert.assertEquals(expected, actual);

        reader.consumeToEnd();
        reader.consume('a');
        reader.mark();
        reader.consume('a');
        expected = "";
        actual = reader.consumeToAny('d');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String expected = "abcd";
        String actual = reader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String expected = "12";
        String actual = reader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        String expected = "123";
        String actual = reader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));

        reader.consume();
        Assert.assertFalse(reader.matches('a'));
        Assert.assertTrue(reader.matches('d'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdABcdABCD");
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
        Assert.assertFalse(reader.matchesIgnoreCase("E"));

        reader.consume();
        Assert.assertTrue(reader.matchesIgnoreCase("D"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matchesAny('a', 'd'));
        Assert.assertFalse(reader.matchesAny('e'));

        reader.consume();
        Assert.assertTrue(reader.matchesAny('d', 'd'));
        Assert.assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());

        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertTrue(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());

        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>Hello World</title>efg");
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("HELLO WORLD"));
        Assert.assertFalse(reader.containsIgnoreCase("TITLE"));

        reader.consumeLetterSequence();
        Assert.assertFalse(reader.containsIgnoreCase("title"));
        Assert.assertFalse(reader.containsIgnoreCase("HELLO WORLD"));
        Assert.assertFalse(reader.containsIgnoreCase("TITLE"));
    }

}