import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrentEmpty() {
        CharacterReader reader = new CharacterReader("");
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }
    
    @Test
    public void testCurrentNonEmpty() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals('t', reader.consume());
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals('s', reader.consume());
        Assert.assertEquals('t', reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("t", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("tes", reader.consumeTo('t'));
        reader.advance();
        Assert.assertEquals("ing", reader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeToNotFound() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("testing", reader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("test", reader.consumeTo("ing"));
        reader.advance();
        Assert.assertEquals("ing", reader.consumeTo("ing"));
    }
    
    @Test
    public void testConsumeToSequenceNotFound() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("testing", reader.consumeTo("xyz"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("te", reader.consumeToAny('s', 't'));
        Assert.assertEquals('s', reader.current());
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("testing", reader.consumeToEnd());
        Assert.assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertEquals("testing", reader.consumeLetterSequence());
        Assert.assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x1234");
        Assert.assertEquals("0x1234", reader.consumeHexSequence());
        Assert.assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertEquals("12345", reader.consumeDigitSequence());
        Assert.assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matches('t'));
        Assert.assertTrue(reader.matches("test"));
        Assert.assertFalse(reader.matches('z'));
        Assert.assertFalse(reader.matches("xyz"));
        Assert.assertFalse(reader.matches('T'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesIgnoreCase("Test"));
        Assert.assertFalse(reader.matchesIgnoreCase("XYZ"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesAny('t', 's'));
        Assert.assertFalse(reader.matchesAny('z'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchesLetter());
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        Assert.assertTrue(reader.matchesDigit());
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchConsume("test"));
        Assert.assertEquals("ing", reader.toString());
        Assert.assertFalse(reader.matchConsume("xyz"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Test"));
        Assert.assertEquals("ing", reader.toString());
        Assert.assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        Assert.assertTrue(reader.containsIgnoreCase("TeSt"));
        Assert.assertFalse(reader.containsIgnoreCase("XYZ"));
    }
    
}