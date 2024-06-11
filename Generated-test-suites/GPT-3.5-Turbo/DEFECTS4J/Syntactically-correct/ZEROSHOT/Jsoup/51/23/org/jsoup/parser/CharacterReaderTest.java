import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader("");
    }

    @Test
    public void testPos() {
        Assert.assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Assert.assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsume() {
        Assert.assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        Assert.assertEquals(-1, characterReader.pos());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        Assert.assertEquals(1, characterReader.pos());
    }

    @Test
    public void testMark() {
        characterReader.mark();
        Assert.assertEquals(0, characterReader.mark);
    }

    @Test
    public void testRewindToMark() {
        characterReader.rewindToMark();
        Assert.assertEquals(characterReader.mark, characterReader.pos);
    }

    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("", characterReader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        Assert.assertEquals(-1, characterReader.nextIndexOf('c'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        Assert.assertEquals(-1, characterReader.nextIndexOf("test"));
    }

    @Test
    public void testConsumeTo() {
        Assert.assertEquals("", characterReader.consumeTo('c'));
    }

    @Test
    public void testConsumeToSequence() {
        Assert.assertEquals("", characterReader.consumeTo("test"));
    }

    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("", characterReader.consumeToAny('a', 'b', 'c'));
    }

    @Test
    public void testConsumeToAnySorted() {
        Assert.assertEquals("", characterReader.consumeToAnySorted('a', 'b', 'c'));
    }

    @Test
    public void testConsumeData() {
        Assert.assertEquals("", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        Assert.assertEquals("", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        Assert.assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Assert.assertEquals("", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        Assert.assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        Assert.assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        Assert.assertFalse(characterReader.matches('c'));
    }

    @Test
    public void testMatchesSequence() {
        Assert.assertFalse(characterReader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertFalse(characterReader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        Assert.assertFalse(characterReader.matchesAny('a', 'b', 'c'));
    }

    @Test
    public void testMatchesAnySorted() {
        Assert.assertFalse(characterReader.matchesAnySorted('a', 'b', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        Assert.assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        Assert.assertFalse(characterReader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertFalse(characterReader.matchConsumeIgnoreCase("test"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        Assert.assertFalse(characterReader.containsIgnoreCase("test"));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        characterReader.cacheString(0, 0);
        Assert.assertEquals("", characterReader.toString());
    }

    @Test
    public void testRangeEquals() {
        Assert.assertFalse(characterReader.rangeEquals(0, 0, ""));
    }
}