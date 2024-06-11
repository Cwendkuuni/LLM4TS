import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.nodes.ParseError;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testReadValidCharacter() {
        // Test code for reading and emitting a valid character
    }

    @Test
    public void testEmitToken() {
        // Test code for emitting a valid token
    }

    @Test
    public void testEmitString() {
        // Test code for emitting a string as a single token
    }

    @Test
    public void testEmitCharacter() {
        // Test code for emitting a single character as a token
    }

    // Test cases for other methods and their edge cases go here

    @Test
    public void testReadInvalidCharacter() {
        // Test code for reading and handling an invalid character
    }

    @Test
    public void testReadInvalidState() {
        // Test code for reading in an invalid state
    }

    @Test
    public void testReadEmptyInput() {
        // Test code for reading from an empty input stream
    }

    @Test
    public void testConsumeCharacterReferenceInvalidSyntax() {
        // Test code for handling invalid character reference syntax
    }

    @Test
    public void testConsumeCharacterReferenceOutsideRange() {
        // Test code for handling character references outside the valid range
    }

    @Test
    public void testConsumeNamedCharacterReference() {
        // Test code for handling named character references
    }

    @Test
    public void testSelfClosingTags() {
        // Test code for handling self-closing tags
    }

    @Test
    public void testInvalidTagNames() {
        // Test code for handling invalid tag names
    }

    @Test
    public void testMalformedHTML() {
        // Test code for handling malformed HTML
    }

    // Test case for error handling goes here

    @Test
    public void testTokeniserError() {
        Tokeniser tokeniser = new Tokeniser(new StubCharacterReader(), new ArrayList<>());
        tokeniser.error(TokeniserState.Data);
        ParseError expectedError = new ParseError(0, "Unexpected character '%s' in input state [Data]", '\u0001');
        Assert.assertEquals(expectedError, tokeniser.errors.get(0));
    }
}