import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokeniserState;
import org.jsoup.nodes.Entities;

import static org.mockito.Mockito.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = Mockito.mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void read_shouldEmitCharacterToken_whenCharBufferHasContent() {
        Token.Character expectedToken = new Token.Character("test");
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('\0'); // Valid char

        tokeniser.emit("test");
        Token result = tokeniser.read();

        verify(errors, never()).add(any()); // No errors should have been added
        assert(result instanceof Token.Character);
        assert(result.equals(expectedToken));
    }

    // Add test cases for all other public methods

    // Edge case and exception handling tests

    // Test case for consumeCharacterReference()

    // Test case for currentNodeInHtmlNS()

    
    // Combine all test cases into a single test file
    @Test
    public void tokeniserTest() {
        // Test cases here
    }
    
}