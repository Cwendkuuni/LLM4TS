import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterReaderTest {

    @Mock
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("This is a test string");
    }

    @Test
    public void testPos() {
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = characterReader.isEmpty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        char expected = 'T';
        char actual = characterReader.current();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        char expected = 'T';
        char actual = characterReader.consume();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        int expected = -1;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        int expected = 1;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        characterReader.mark();
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        characterReader.rewindToMark();
        int expected = 0;
        int actual = characterReader.pos();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        String expected = "T";
        String actual = characterReader.consumeAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToChar() {
        String expected = "This ";
        String actual = characterReader.consumeTo(' ');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToString() {
        String expected = "This";
        String actual = characterReader.consumeTo(" is");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        char[] seq = {' ', 'i'};
        String expected = "Th";
        String actual = characterReader.consumeToAny(seq);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "This is a test string";
        String actual = characterReader.consumeToEnd();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "This";
        String actual = characterReader.consumeLetterSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "";
        String actual = characterReader.consumeHexSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = characterReader.consumeDigitSequence();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesChar() {
        boolean expected = true;
        boolean actual = characterReader.matches('T');
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        boolean expected = true;
        boolean actual = characterReader.matches("This");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("this");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        char[] seq = {'T', 't'};
        boolean expected = true;
        boolean actual = characterReader.matchesAny(seq);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        boolean expected = true;
        boolean actual = characterReader.matchConsume("This ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("this ");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("test");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "This is a test string";
        String actual = characterReader.toString();
        Assert.assertEquals(expected, actual);
    }

}