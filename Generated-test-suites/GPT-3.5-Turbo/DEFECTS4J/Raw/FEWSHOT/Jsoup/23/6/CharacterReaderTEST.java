import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals(0, reader.pos());
        
        reader.consume();
        Assert.assertEquals(1, reader.pos());
        
        reader.advance();
        Assert.assertEquals(2, reader.pos());
        
        reader.rewindToMark();
        Assert.assertEquals(1, reader.pos());
        
        reader.unconsume();
        Assert.assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertFalse(reader.isEmpty());
        
        reader.consume();
        Assert.assertFalse(reader.isEmpty());
        
        reader.advance();
        Assert.assertFalse(reader.isEmpty());
        
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        
        reader.consume();
        Assert.assertEquals('b', reader.current());
        
        reader.advance();
        Assert.assertEquals('c', reader.current());
        
        reader.consume();
        Assert.assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.consume());
        Assert.assertEquals('b', reader.consume());
        reader.unconsume();
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals('c', reader.consume());
        Assert.assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals('a', reader.current());
        reader.advance();
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(CharacterReader.EOF, reader.advance());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.mark();
        reader.advance();
        Assert.assertEquals('c', reader.current());
        reader.rewindToMark();
        Assert.assertEquals('b', reader.current());
        reader.advance();
        Assert.assertEquals('c', reader.current());
        Assert.assertEquals(CharacterReader.EOF, reader.advance());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertEquals("a", reader.consumeAsString());
        Assert.assertEquals("b", reader.consumeAsString());
        Assert.assertEquals("c", reader.consumeAsString());
        Assert.assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abc", reader.consumeTo('d'));
        Assert.assertEquals('d', reader.consume());
        Assert.assertEquals("e", reader.consumeTo('f'));
        Assert.assertEquals("f", reader.consumeTo("g"));
        Assert.assertEquals("g", reader.consumeTo('h'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abc", reader.consumeToAny('d', 'e'));
        Assert.assertEquals('e', reader.consume());
        Assert.assertEquals("f", reader.consumeToAny('g', 'h'));
        Assert.assertEquals("g", reader.consumeToAny('h'));
        Assert.assertEquals("", reader.consumeToAny('i'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertEquals("abcdefg", reader.consumeToEnd());
        Assert.assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("abc", reader.consumeLetterSequence());
        Assert.assertEquals("123", reader.consumeLetterSequence());
        Assert.assertEquals("def", reader.consumeLetterSequence());
        Assert.assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("", reader.consumeHexSequence());
        
        reader.consume();
        Assert.assertEquals("", reader.consumeHexSequence());
        
        reader.consuem();
        Assert.assertEquals("abc123", reader.consumeHexSequence());
        
        reader.consuem();
        Assert.assertEquals("def", reader.consumeHexSequence());
        
        reader.consuem();
        Assert.assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        Assert.assertEquals("", reader.consumeDigitSequence());
        
        reader.consume();
        Assert.assertEquals("", reader.consumeDigitSequence());
        
        reader.consume();
        Assert.assertEquals("123", reader.consumeDigitSequence());
        
        reader.consume();
        Assert.assertEquals("", reader.consumeDigitSequence());
        
        reader.consume();
        Assert.assertEquals("def", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matches('a'));
        Assert.assertFalse(reader.matches('b'));
        
        reader.consume();
        Assert.assertTrue(reader.matches('b'));
        Assert.assertFalse(reader.matches('c'));
        
        reader.advance();
        Assert.assertTrue(reader.matches('c'));
        Assert.assertFalse(reader.matches('d'));
        
        reader.consume();
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesIgnoreCase("A"));
        
        reader.consume();
        Assert.assertTrue(reader.matchesIgnoreCase("B"));
        
        reader.advance();
        Assert.assertTrue(reader.matchesIgnoreCase("C"));
        
        reader.consume();
        Assert.assertFalse(reader.matchesIgnoreCase("D"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
        Assert.assertFalse(reader.matchesAny('d', 'e', 'f'));
        
        reader.consume();
        Assert.assertTrue(reader.matchesAny('b', 'c', 'd'));
        Assert.assertFalse(reader.matchesAny('e', 'f', 'g'));
        
        reader.advance();
        Assert.assertTrue(reader.matchesAny('c', 'd', 'e'));
        Assert.assertFalse(reader.matchesAny('f', 'g', 'h'));
        
        reader.consume();
        Assert.assertFalse(reader.matchesAny('i', 'j', 'k'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertTrue(reader.matchesLetter());
        
        reader.consume();
        Assert.assertTrue(reader.matchesLetter());
        
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        
        reader.consume();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        Assert.assertFalse(reader.matchesDigit());
        
        reader.consume();
        Assert.assertFalse(reader.matchesDigit());
        
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
        
        reader.consume();
        Assert.assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals('c', reader.current());
        
        Assert.assertFalse(reader.matchConsume("cd"));
    }

    @Test
    public void testMathcConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        Assert.assertEquals('c', reader.current());
        
        Assert.assertFalse(reader.matchConsumeIgnoreCase("Cd"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefg");
        Assert.assertTrue(reader.containsIgnoreCase("ABC"));
        Assert.assertFalse(reader.containsIgnoreCase("XYZ"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        Assert.assertEquals("bcdefg", reader.toString());
        
        reader.consumeToEnd();
        Assert.assertEquals("", reader.toString());
    }
}