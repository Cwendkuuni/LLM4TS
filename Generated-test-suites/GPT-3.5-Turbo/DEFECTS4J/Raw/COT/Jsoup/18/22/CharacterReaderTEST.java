import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader reader;
    
    @Before
    public void setUp() {
        reader = new CharacterReader("test string");
    }
    
    @Test
    public void testPos() {
        Assert.assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        Assert.assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        Assert.assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        reader.advance();
        Assert.assertEquals('e', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        Assert.assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        Assert.assertEquals("t", reader.consumeAsString());
        Assert.assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        Assert.assertEquals("test ", reader.consumeTo(' '));
        Assert.assertEquals("string", reader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToAny() {
        Assert.assertEquals("test", reader.consumeToAny(' ', 's'));
        Assert.assertEquals(" string", reader.consumeToAny(' ', 's'));
    }
    
    @Test
    public void testConsumeToEnd() {
        Assert.assertEquals("test string", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        Assert.assertEquals("test", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        Assert.assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        Assert.assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        Assert.assertTrue(reader.matches('t'));
        Assert.assertFalse(reader.matches('s'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        Assert.assertTrue(reader.matchesIgnoreCase("TeSt"));
        Assert.assertFalse(reader.matchesIgnoreCase("String"));
    }
    
    @Test
    public void testMatchesAny() {
        Assert.assertTrue(reader.matchesAny('t', 's'));
        Assert.assertFalse(reader.matchesAny('a', 'b'));
    }
    
    @Test
    public void testMatchesLetter() {
        Assert.assertTrue(reader.matchesLetter());
        Assert.assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        Assert.assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        Assert.assertTrue(reader.matchConsume("te"));
        Assert.assertFalse(reader.matchConsume("st"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Te"));
        Assert.assertFalse(reader.matchConsumeIgnoreCase("St"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        Assert.assertTrue(reader.containsIgnoreCase("Te"));
        Assert.assertFalse(reader.containsIgnoreCase("St"));
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("test string", reader.toString());
    }
}