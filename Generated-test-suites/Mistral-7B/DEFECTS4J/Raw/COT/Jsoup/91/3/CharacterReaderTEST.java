import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jsoup.parser.CharacterReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CharacterReaderTest {
    private CharacterReader reader;
    private Reader input;
    private int sz;

    @Parameterized.Parameters(name = "{index}: CharacterReader({0}, {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new StringReader("Test input"), maxBufferLen},
                {new StringReader("Test input"), 10},
                {new StringReader("Test input"), 5},
                {new StringReader("Test input"), 3}
        });
    }

    public CharacterReaderTest(Reader input, int sz) {
        this.input = input;
        this.sz = sz;
    }

    @Before
    public void setUp() {
        reader = new CharacterReader(input, sz);
    }

    @Test
    public void testConstructorWithReaderAndSize() {
        assertNotNull(reader);
    }

    @Test
    public void testConstructorWithReader() {
        CharacterReader readerWithMaxBufferSize = new CharacterReader(input);
        assertNotNull(readerWithMaxBufferSize);
    }

    @Test
    public void testConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"), 10);
        assertNotNull(characterReader);
    }

    @Test
    public void testBufferUp() {
        reader.bufferUp();
        assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenBufferingUp() {
        final byte[] exceptionInput = new byte[1];
        Reader readerWithExceptionInput = new InputStreamReader(new ByteArrayInputStream(exceptionInput));
        CharacterReader characterReader = new CharacterReader(readerWithExceptionInput, 10);
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = reader.pos();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
        reader.advance();
        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        reader.consume();
        char actual = reader.current();
        assertEquals(reader.charAt(0), actual);
    }

    @Test
    public void testConsume() {
        char expected = reader.charAt(0);
        reader.consume();
        char actual = reader.current();
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(reader.charAt(0), reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        int expected = reader.pos();
        int actual = reader.pos();
        assertEquals(expected + 1, actual);
    }

    @Test
    public void testMark() {
        String expected = reader.consumeToEnd();
        reader.mark();
        reader.rewindToMark();
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test(expected = UncheckedIOException.class)
    public void testMarkInvalid() {
        reader.mark();
        reader.rewindToMark();
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        char testChar = 'a';
        int expected = reader.nextIndexOf(testChar);
        int actual = reader.nextIndexOf(testChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        String testSequence = "test";
        int expected = reader.nextIndexOf(testSequence);
        int actual = reader.nextIndexOf(testSequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "test";
        String actual = reader.consumeTo(testChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToWithString() {
        String expected = "test";
        String actual = reader.consumeTo(testSequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        char[] testChars = {'a', 'b', 'c'};
        String expected = "abc";
        String actual = reader.consumeToAny(testChars);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAnySorted() {
        char[] testChars = {'b', 'a', 'c'};
        String expected = "abc";
        String actual = reader.consumeToAnySorted(testChars);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeData() {
        String expected = "&<null";
        String actual = reader.consumeData();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "<html>";
        String actual = reader.consumeTagName();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = reader.toString();
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "LetterSequence";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "LetterThenDigitSequence123";
        String actual = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "0x123456";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "123456";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        char testChar = 'a';
        boolean expected = reader.matches(testChar);
        boolean actual = reader.matches(testChar);
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesString() {
        String testString = "test";
        boolean expected = reader.matches(testString);
        boolean actual = reader.matches(testString);
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        String testString = "Test";
        boolean expected = reader.matchesIgnoreCase(testString);
        boolean actual = reader.matchesIgnoreCase(testString);
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesAny() {
        char[] testChars = {'a', 'b'};
        boolean expected = reader.matchesAny(testChars);
        boolean actual = reader.matchesAny(testChars);
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesAnySorted() {
        char[] testChars = {'b', 'a'};
        boolean expected = reader.matchesAnySorted(testChars);
        boolean actual = reader.matchesAnySorted(testChars);
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = reader.matchesLetter();
        boolean actual = reader.matchesLetter();
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchesDigit() {
        boolean expected = reader.matchesDigit();
        boolean actual = reader.matchesDigit();
        assertTrue(expected == actual);
    }

    @Test
    public void testMatchConsume() {
        String testString = "test";
        String expected = reader.consumeToEnd();
        reader.matchConsume(testString);
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String testString = "Test";
        String expected = reader.consumeToEnd();
        reader.matchConsumeIgnoreCase(testString);
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        String testString = "<html><body><title>Test</title></body></html>";
        boolean expected = reader.containsIgnoreCase("title");
        boolean actual = reader.containsIgnoreCase(testString);
        assertTrue(expected == actual);
    }
}