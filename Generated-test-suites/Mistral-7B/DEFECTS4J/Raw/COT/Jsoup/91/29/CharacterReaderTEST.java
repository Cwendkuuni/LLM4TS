import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        Assert.assertEquals("Test String", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Test String");
        Assert.assertEquals("Test String", characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C"));
        characterReader.bufferUp();
        char[] charBuf = characterReader.charBuf;
        int bufLength = characterReader.bufLength;
        int bufPos = characterReader.bufPos;
        int readerPos = characterReader.readerPos;

        Assert.assertEquals('A', charBuf[bufPos]);
        Assert.assertEquals('B', charBuf[bufPos + 1]);
        Assert.assertEquals('C', charBuf[bufPos + 2]);
        Assert.assertEquals(3, bufLength);
        Assert.assertEquals(3, readerPos);
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C"));
        characterReader.bufferUp();

        int pos = characterReader.pos();
        Assert.assertEquals(6, pos);
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C"));
        characterReader.bufferUp();

        Assert.assertFalse(characterReader.isEmpty());
        characterReader.advance();

        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C"));
        characterReader.bufferUp();

        Assert.assertEquals('A', characterReader.current());
        characterReader.consume();

        Assert.assertEquals('B', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferUpFails() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };

        characterReader.consume();
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String A B C"));
        characterReader.bufferUp();

        Assert.assertEquals('A', characterReader.current());
        characterReader.consume();

        Assert.assertEquals('B', characterReader.current());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC"));
        characterReader.bufferUp();
        characterReader.consume();

        characterReader.unconsume();

        Assert.assertEquals('A', characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC"));
        characterReader.bufferUp();

        characterReader.advance();

        Assert.assertEquals('B', characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC"));
        characterReader.bufferUp();

        characterReader.mark();
        characterReader.advance();
        characterReader.consume();

        characterReader.rewindToMark();

        Assert.assertEquals('A', characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        int index = characterReader.nextIndexOf('B');

        Assert.assertEquals(3, index);
    }

    @Test
    public void testNextIndexOfMethodWithMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        int index = characterReader.nextIndexOf("AB");

        Assert.assertEquals(3, index);
    }

    @Test
    public void testNextIndexOfMethodWithEmptyCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC"));
        characterReader.bufferUp();

        int index = characterReader.nextIndexOf("");

        Assert.assertEquals(-1, index);
    }

    @Test
    public void testConsumeToMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        String result = characterReader.consumeTo('B');

        Assert.assertEquals("AB", result);
    }

    @Test
    public void testConsumeToMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        String result = characterReader.consumeTo("AB");

        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testConsumeToMethodToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        String result = characterReader.consumeToEnd();

        Assert.assertEquals("ABC ABC", result);
    }

    @Test
    public void testConsumeToAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        String result = characterReader.consumeToAny('B');

        Assert.assertEquals("ABA", result);
    }

    @Test
    public void testConsumeToAnyMethodWithMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABC ABC"));
        characterReader.bufferUp();

        String result = characterReader.consumeToAny(new char[]{'A', 'B'});

        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        characterReader.bufferUp();

        String result = characterReader.consumeData();

        Assert.assertEquals("&<null", result);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><body>"));
        characterReader.bufferUp();

        String result = characterReader.consumeTagName();

        Assert.assertEquals("html", result);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterSequence123"));
        characterReader.bufferUp();

        String result = characterReader.consumeLetterSequence();

        Assert.assertEquals("TestLetterSequence", result);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetter123Sequence"));
        characterReader.bufferUp();

        String result = characterReader.consumeLetterThenDigitSequence();

        Assert.assertEquals("TestLetter", result);
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test&x0A&x0B&x0C"));
        characterReader.bufferUp();

        String result = characterReader.consumeHexSequence();

        Assert.assertEquals("&x0A&x0B&x0C", result);
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test12345Sequence"));
        characterReader.bufferUp();

        String result = characterReader.consumeDigitSequence();

        Assert.assertEquals("Test", result);
    }

    @Test
    public void testMatchesMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringA"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matches('A'));
    }

    @Test
    public void testMatchesMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringAB"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matches("AB"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringA"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesIgnoreCase("a"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringAB"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesIgnoreCase("Ab"));
    }

    @Test
    public void testMatchesAnyMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringA"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesAny('A'));
    }

    @Test
    public void testMatchesAnyMethodWithMultipleChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringAB"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesAny(new char[]{ 'A', 'B' }));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString123"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString123"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringA"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchConsume('A'));
    }

    @Test
    public void testMatchConsumeMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringAB"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.matchConsume("AB"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringA"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.containsIgnoreCase("A"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithMultiChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestStringAB"));
        characterReader.bufferUp();

        Assert.assertTrue(characterReader.containsIgnoreCase("Ab"));
    }
}