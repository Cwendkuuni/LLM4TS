import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setup() {
        characterReader = new CharacterReader("test input");
    }
    
    @Test
    public void testPos() {
        int pos = characterReader.pos();
        // assert statements
    }
    
    @Test
    public void testIsEmpty() {
        boolean isEmpty = characterReader.isEmpty();
        // assert statements
    }
    
    @Test
    public void testCurrent() {
        char current = characterReader.current();
        // assert statements
    }
    
    @Test
    public void testConsume() {
        char consumed = characterReader.consume();
        // assert statements
    }
    
    @Test
    public void testUnconsume() {
        characterReader.unconsume();
        // assert statements
    }
    
    @Test
    public void testAdvance() {
        characterReader.advance();
        // assert statements
    }
    
    @Test
    public void testMark() {
        characterReader.mark();
        // assert statements
    }
    
    @Test
    public void testRewindToMark() {
        characterReader.rewindToMark();
        // assert statements
    }
    
    @Test
    public void testConsumeAsString() {
        String consumed = characterReader.consumeAsString();
        // assert statements
    }
    
    @Test
    public void testNextIndexOfChar() {
        int offset = characterReader.nextIndexOf('c');
        // assert statements
    }
    
    @Test
    public void testNextIndexOfCharSequence() {
        int offset = characterReader.nextIndexOf("test");
        // assert statements
    }
    
    @Test
    public void testConsumeToChar() {
        String consumed = characterReader.consumeTo('t');
        // assert statements
    }
    
    @Test
    public void testConsumeToString() {
        String consumed = characterReader.consumeTo("input");
        // assert statements
    }
    
    @Test
    public void testConsumeToAny() {
        String consumed = characterReader.consumeToAny('t', 's');
        // assert statements
    }
    
    @Test
    public void testConsumeToAnySorted() {
        String consumed = characterReader.consumeToAnySorted('t', 's');
        // assert statements
    }
    
    @Test
    public void testConsumeData() {
        String consumed = characterReader.consumeData();
        // assert statements
    }
    
    @Test
    public void testConsumeTagName() {
        String consumed = characterReader.consumeTagName();
        // assert statements
    }
    
    @Test
    public void testConsumeToEnd() {
        String consumed = characterReader.consumeToEnd();
        // assert statements
    }
    
    @Test
    public void testConsumeLetterSequence() {
        String consumed = characterReader.consumeLetterSequence();
        // assert statements
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        String consumed = characterReader.consumeLetterThenDigitSequence();
        // assert statements
    }
    
    @Test
    public void testConsumeHexSequence() {
        String consumed = characterReader.consumeHexSequence();
        // assert statements
    }
    
    @Test
    public void testConsumeDigitSequence() {
        String consumed = characterReader.consumeDigitSequence();
        // assert statements
    }
    
    @Test
    public void testMatchesChar() {
        boolean matchesChar = characterReader.matches('t');
        // assert statements
    }
    
    @Test
    public void testMatchesString() {
        boolean matchesString = characterReader.matches("input");
        // assert statements
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        boolean matchesIgnoreCase = characterReader.matchesIgnoreCase("TEST");
        // assert statements
    }
    
    @Test
    public void testMatchesAny() {
        boolean matchesAny = characterReader.matchesAny('t', 's');
        // assert statements
    }
    
    @Test
    public void testMatchesAnySorted() {
        boolean matchesAnySorted = characterReader.matchesAnySorted('t', 's');
        // assert statements
    }
    
    @Test
    public void testMatchesLetter() {
        boolean matchesLetter = characterReader.matchesLetter();
        // assert statements
    }
    
    @Test
    public void testMatchesDigit() {
        boolean matchesDigit = characterReader.matchesDigit();
        // assert statements
    }
    
    @Test
    public void testMatchConsume() {
        boolean matchConsume = characterReader.matchConsume("input");
        // assert statements
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean matchConsumeIgnoreCase = characterReader.matchConsumeIgnoreCase("INPUT");
        // assert statements
    }
    
    @Test
    public void testContainsIgnoreCase() {
        boolean containsIgnoreCase = characterReader.containsIgnoreCase("INPUT");
        // assert statements
    }
    
    @Test
    public void testToString() {
        String string = characterReader.toString();
        // assert statements
    }
    
    @Test
    public void testCacheString() {
        String cached = characterReader.cacheString(0, 4);
        // assert statements
    }
    
    @Test
    public void testRangeEquals() {
        boolean rangeEquals = characterReader.rangeEquals(0, 4, "test");
        // assert statements
    }
}