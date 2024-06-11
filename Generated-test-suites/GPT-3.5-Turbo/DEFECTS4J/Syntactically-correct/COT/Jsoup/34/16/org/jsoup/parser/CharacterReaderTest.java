import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // test functionality of pos method
        
        CharacterReader characterReader = new CharacterReader("input");
        int expected = 0;
        int actual = characterReader.pos();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        // test functionality of isEmpty method
        
        CharacterReader characterReader = new CharacterReader("");
        boolean expected = true;
        boolean actual = characterReader.isEmpty();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testCurrent() {
        // test functionality of current method
        
        CharacterReader characterReader = new CharacterReader("input");
        char expected = 'i';
        char actual = characterReader.current();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsume() {
        // test functionality of consume method
        
        CharacterReader characterReader = new CharacterReader("input");
        char expected = 'i';
        char actual = characterReader.consume();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testUnconsume() {
        // test functionality of unconsume method
        
        CharacterReader characterReader = new CharacterReader("input");
        characterReader.consume();
        characterReader.unconsume();
        
        int expected = 0;
        int actual = characterReader.pos();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvance() {
        // test functionality of advance method
        
        CharacterReader characterReader = new CharacterReader("input");
        characterReader.advance();
        
        int expected = 1;
        int actual = characterReader.pos();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMark() {
        // test functionality of mark method
        
        CharacterReader characterReader = new CharacterReader("input");
        characterReader.mark();
        
        int expected = 0;
        int actual = characterReader.pos();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testRewindToMark() {
        // test functionality of rewindToMark method
        
        CharacterReader characterReader = new CharacterReader("input");
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        
        int expected = 0;
        int actual = characterReader.pos();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeAsString() {
        // test functionality of consumeAsString method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "i";
        String actual = characterReader.consumeAsString();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOf() {
        // test functionality of nextIndexOf method
        
        CharacterReader characterReader = new CharacterReader("input");
        int expected = 0;
        int actual = characterReader.nextIndexOf('i');
        
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIndexOfCharSequence() {
        // test functionality of nextIndexOf method
        
        CharacterReader characterReader = new CharacterReader("input");
        int expected = 0;
        int actual = characterReader.nextIndexOf("in");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        // test functionality of consumeTo method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "inp";
        String actual = characterReader.consumeTo("ut");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        // test functionality of consumeToAny method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "in";
        String actual = characterReader.consumeToAny("pu");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        // test functionality of consumeToEnd method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "input";
        String actual = characterReader.consumeToEnd();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterSequence() {
        // test functionality of consumeLetterSequence method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "input";
        String actual = characterReader.consumeLetterSequence();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        // test functionality of consumeLetterThenDigitSequence method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "input";
        String actual = characterReader.consumeLetterThenDigitSequence();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        // test functionality of consumeHexSequence method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "input";
        String actual = characterReader.consumeHexSequence();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        // test functionality of consumeDigitSequence method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "";
        String actual = characterReader.consumeDigitSequence();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        // test functionality of matches method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matches('i');
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesString() {
        // test functionality of matches method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matches("in");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesIgnoreCase() {
        // test functionality of matchesIgnoreCase method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matchesIgnoreCase("IN");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesAny() {
        // test functionality of matchesAny method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matchesAny('i', 'n');
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesLetter() {
        // test functionality of matchesLetter method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matchesLetter();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchesDigit() {
        // test functionality of matchesDigit method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = false;
        boolean actual = characterReader.matchesDigit();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsume() {
        // test functionality of matchConsume method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matchConsume("in");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        // test functionality of matchConsumeIgnoreCase method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.matchConsumeIgnoreCase("IN");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsIgnoreCase() {
        // test functionality of containsIgnoreCase method
        
        CharacterReader characterReader = new CharacterReader("input");
        boolean expected = true;
        boolean actual = characterReader.containsIgnoreCase("IN");
        
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        // test functionality of toString method
        
        CharacterReader characterReader = new CharacterReader("input");
        String expected = "input";
        String actual = characterReader.toString();
        
        assertEquals(expected, actual);
    }

}