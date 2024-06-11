// import statements

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("abcdef");
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = reader.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        char expected = 'a';
        char actual = reader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'a';
        char actual = reader.consume();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been incremented
        expected = 1;
        actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        // Verify that pos has been decremented
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        // Verify that pos has been incremented
        int expected = 1;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        reader.mark();
        // Verify that mark has been set to pos
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        // Verify that pos has been set to mark
        int expected = 0;
        int actual = reader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "a";
        String actual = reader.consumeAsString();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been incremented
        int expectedPos = 1;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeTo() {
        String expected = "abc";
        String actual = reader.consumeTo('d');
        Assert.assertEquals(expected, actual);
        // Verify that pos has been updated correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "abc";
        String actual = reader.consumeToAny('d', 'e', 'f');
        Assert.assertEquals(expected, actual);
        // Verify that pos has been updated correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "abcdef";
        String actual = reader.consumeToEnd();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been set to the length of the input string
        int expectedPos = 6;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "abcdef";
        String actual = reader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been set to the length of the input string
        int expectedPos = 6;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("123abc");
        String expected = "123";
        String actual = reader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been set correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123abc");
        String expected = "123";
        String actual = reader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
        // Verify that pos has been set correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testMatches() {
        boolean expected = true;
        boolean actual = reader.matches('a');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        boolean expected = true;
        boolean actual = reader.matches("abc");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        reader = new CharacterReader("Abc");
        boolean expected = true;
        boolean actual = reader.matchesIgnoreCase("abc");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        boolean expected = true;
        boolean actual = reader.matchesAny('a', 'b', 'c');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = reader.matchesLetter();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader("123");
        boolean expected = true;
        boolean actual = reader.matchesDigit();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = reader.matchConsume("abc");
        Assert.assertEquals(expected, actual);
        // Verify that pos has been updated correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader = new CharacterReader("Abc");
        boolean expected = true;
        boolean actual = reader.matchConsumeIgnoreCase("abc");
        Assert.assertEquals(expected, actual);
        // Verify that pos has been updated correctly
        int expectedPos = 3;
        int actualPos = reader.pos();
        Assert.assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader("abcDEF");
        boolean expected = true;
        boolean actual = reader.containsIgnoreCase("def");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "abcdef";
        String actual = reader.toString();
        Assert.assertEquals(expected, actual);
    }
}