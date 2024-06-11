import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Input String");
    }

    @Test
    public void testPos() {
        assert characterReader.pos() == 0;  // Check initial position
        characterReader.consume();  // Consume one character
        assert characterReader.pos() == 1;  // Check position after consuming one character
        characterReader.consumeToEnd();  // Consume to the end
        assert characterReader.pos() == characterReader.length - 1;  // Check position after consuming to the end
    }

    @Test
    public void testIsEmpty() {
        assert !characterReader.isEmpty();  // Check that characterReader is not empty at the beginning
        characterReader.consumeToEnd();  // Consume to the end
        assert characterReader.isEmpty();  // Check that characterReader is empty after consuming to the end
    }

    @Test
    public void testCurrent() {
        assert characterReader.current() == 'I';  // Check current character at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.current() == 'n';  // Check current character after consuming one character
        characterReader.consumeToEnd();  // Consume to the end
        assert characterReader.current() == CharacterReader.EOF;  // Check current character after consuming to the end
    }

    @Test
    public void testConsume() {
        assert characterReader.consume() == 'I';  // Check consumed character at the beginning
        assert characterReader.consume() == 'n';  // Check consumed character after consuming one character
        characterReader.consumeToEnd();  // Consume to the end
        assert characterReader.consume() == CharacterReader.EOF;  // Check consumed character after consuming to the end
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();  // Consume one character
        characterReader.unconsume();  // Unconsume one character
        assert characterReader.current() == 'I';  // Check current character after unconsuming one character
    }

    @Test
    public void testAdvance() {
        assert characterReader.current() == 'I';  // Check current character at the beginning
        characterReader.advance();  // Advance one position
        assert characterReader.current() == 'n';  // Check current character after advancing one position
    }

    @Test
    public void testMarkAndRewindToMark() {
        characterReader.advance();  // Advance one position
        characterReader.mark();  // Mark current position
        characterReader.advance();  // Advance one more position
        characterReader.rewindToMark();  // Rewind to marked position
        assert characterReader.current() == 'n';  // Check current character after rewinding to marked position
    }

    @Test
    public void testConsumeAsString() {
        assert characterReader.consumeAsString().equals("Input String");  // Check consumed string at the beginning
        assert characterReader.isEmpty();  // Check that characterReader is empty after consuming the entire string
    }

    @Test
    public void testConsumeTo() {
        assert characterReader.consumeTo(' ').equals("Input");  // Check consumed string until the first space character
        assert characterReader.current() == ' ';  // Check current character after consuming to the first space character
        assert characterReader.consumeTo("Str").equals(" ");  // Check consumed string until the "Str" sequence
        assert characterReader.current() == 'i';  // Check current character after consuming to the "Str" sequence
    }

    @Test
    public void testConsumeToAny() {
        assert characterReader.consumeToAny(' ', 's').equals("Input");  // Check consumed string until the first space or 's' character
        assert characterReader.current() == ' ';  // Check current character after consuming to the first space or 's' character
    }

    @Test
    public void testConsumeToEnd() {
        assert characterReader.consumeToEnd().equals("Input String");  // Check consumed string until the end
        assert characterReader.current() == CharacterReader.EOF;  // Check current character after consuming to the end
    }

    @Test
    public void testConsumeLetterSequence() {
        assert characterReader.consumeLetterSequence().equals("Input");  // Check consumed letter sequence at the beginning
        assert characterReader.consumeLetterSequence().equals("Str");  // Check consumed letter sequence after consuming "Input"
    }

    @Test
    public void testConsumeHexSequence() {
        assert characterReader.consumeHexSequence().equals("");  // Check consumed hex sequence at the beginning
        characterReader.consume();  // Consume one character
        characterReader.advance();  // Advance one position
        assert characterReader.consumeHexSequence().equals("");  // Check consumed hex sequence after advancing one position
    }

    @Test
    public void testConsumeDigitSequence() {
        assert characterReader.consumeDigitSequence().equals("");  // Check consumed digit sequence at the beginning
        assert characterReader.consumeDigitSequence().equals("");  // Check consumed digit sequence after consuming "Input String"
    }

    @Test
    public void testMatches() {
        assert characterReader.matches('I');  // Check match with character at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.matches('n');  // Check match with character after consuming one character
        assert !characterReader.matches('I');  // Check match with not-matching character after consuming one character
    }

    @Test
    public void testMatchesIgnoreCase() {
        assert characterReader.matchesIgnoreCase("Inp");  // Check match with case-insensitive string at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.matchesIgnoreCase("nPUt");  // Check match with case-insensitive string after consuming one character
        assert !characterReader.matchesIgnoreCase("Str");  // Check match with not-matching case-insensitive string after consuming one character
    }

    @Test
    public void testMatchesAny() {
        assert characterReader.matchesAny('I', 'n');  // Check match with any of the given characters at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.matchesAny('U', 'P');  // Check match with any of the given characters after consuming one character
        assert !characterReader.matchesAny('I', 'S');  // Check match with not-matching any of the given characters after consuming one character
    }

    @Test
    public void testMatchesLetter() {
        assert characterReader.matchesLetter();  // Check match with letter character at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.matchesLetter();  // Check match with letter character after consuming one character
        characterReader.consumeToEnd();  // Consume to the end
        assert !characterReader.matchesLetter();  // Check match with not-matching letter character after consuming to the end
    }

    @Test
    public void testMatchesDigit() {
        assert !characterReader.matchesDigit();  // Check match with not-matching digit character at the beginning
        characterReader.consumeToEnd();  // Consume to the end
        assert !characterReader.matchesDigit();  // Check match with not-matching digit character after consuming to the end
    }

    @Test
    public void testMatchConsume() {
        assert characterReader.matchConsume("In");  // Check match and consume of the given sequence at the beginning
        assert characterReader.current() == 'p';  // Check current character after matching and consuming "In" sequence
        assert characterReader.matchConsumeIgnoreCase("pu");  // Check match and consume of the given case-insensitive sequence after consuming "Inp"
        assert characterReader.current() == ' ';  // Check current character after matching and consuming "pu" sequence
        assert !characterReader.matchConsume("Str");  // Check match failure and no consume after consuming "Inpu "
        assert characterReader.current() == 'S';  // Check current character after match failure and no consume
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assert characterReader.matchConsumeIgnoreCase("inP");  // Check match and consume of the given case-insensitive sequence at the beginning
        assert characterReader.matchConsumeIgnoreCase("STR");  // Check match and consume of the given case-insensitive sequence after consuming "inP"
        assert !characterReader.matchConsumeIgnoreCase("ing");  // Check match failure and no consume after consuming "STR"
    }

    @Test
    public void testContainsIgnoreCase() {
        assert characterReader.containsIgnoreCase("ST");  // Check presence of case-insensitive string at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.containsIgnoreCase("S t");  // Check presence of case-insensitive string after consuming one character
        characterReader.consumeToEnd();  // Consume to the end
        assert !characterReader.containsIgnoreCase("Input");  // Check absence of case-insensitive string after consuming to the end
    }

    @Test
    public void testToString() {
        assert characterReader.toString().equals("Input String");  // Check string representation at the beginning
        characterReader.consume();  // Consume one character
        assert characterReader.toString().equals("nput String");  // Check string representation after consuming one character
    }
}