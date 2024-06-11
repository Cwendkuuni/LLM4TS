import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = new CharacterReader("Test input");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        // Add assertions here
    }

    @Test
    public void testEmit() {
        // Add test for emit method
    }

    @Test
    public void testConsumeCharacterReference() {
        // Add test for consumeCharacterReference method
    }

    // Add more test cases for other methods in Tokeniser class

}