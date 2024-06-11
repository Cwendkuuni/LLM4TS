import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test String", reader.toString());
    }

    @Test
    public void testCharacterReaderConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.consume();
        Assert.assertEquals('T', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Test")) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        String result = reader.consumeTo('T');
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        String result = reader.consumeToEnd();
        Assert.assertEquals("TestTest", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetterSequence"));
        String result = reader.consumeLetterSequence();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestLetter123Sequence"));
        String result = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("TestLetter", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test#x1a#x2b"));
        String result = reader.consumeHexSequence();
        Assert.assertEquals("1a2b", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Test12345"));
        String result = reader.consumeDigitSequence();
        Assert.assertEquals("12345", result);
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        Assert.assertEquals(2, reader.nextIndexOf('T'));
    }

    @Test
    public void testCharacterReaderNextIndexOfCharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTestSequence"));
        Assert.assertEquals(4, reader.nextIndexOf("Test"));
    }

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderIsNotEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        reader.matchConsume('T');
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        reader.matchConsume("Test");
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        reader.matchConsumeIgnoreCase("test");
        Assert.assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertTrue(reader.matches('T'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        Assert.assertTrue(reader.matches("Te"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader(new StringReader("TestTest"));
        Assert.assertTrue(reader.matchesIgnoreCase("te"));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Test"));
        Assert.assertTrue(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesAnyChar() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(reader.matchesAny('\n'));
    }

    @Test
    public void testCharacterReaderMatchesAnySortedChars() {
        CharacterReader reader = new CharacterReader(new StringReader("Test123"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'1', '2', '3'}));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Letter>123"));
        String result = reader.consumeToAny('L');
        Assert.assertEquals("Test<", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Test<Letter>123"));
        String result = reader.consumeToAnySorted(new char[]{'<', '1', '2', '3'});
        Assert.assertEquals("<", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("Test&<data>"));
        String result = reader.consumeData();
        Assert.assertEquals("&<data>", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<TestTag>"));
        String result = reader.consumeTagName();
        Assert.assertEquals("TestTag", result);
    }
}