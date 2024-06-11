
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {

    @Test
    // ... Add all test cases from Experts 1, 2, and 3 here

    // ... Add more test cases for other methods, edge cases, and error scenarios

    @Test
    public void testReadCompleteInput() {
        // Given
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        while (tokeniser.getState() != TokeniserState.End) {
            Token token = tokeniser.read();
            System.out.println(token);
        }

        // Then
        Assert.assertEquals(TokeniserState.End, tokeniser.getState());
        Assert.assertEquals(0, errors.size());
    }

}
