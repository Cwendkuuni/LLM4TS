import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseError;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

public class TokeniserTest {
    @Test
    public void testRead() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("Test String"));
        Token token = tokeniser.read();
        // Check token properties
    }

    @Test
    public void testEmitToken() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("Test String"));
        Token token = new Token.Character('A');
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        // Check emitted token properties
    }

    @Test
    public void testEmitString() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("Test String"));
        tokeniser.emit("Test String");
        Token token = tokeniser.read();
        // Check token properties
    }

    @Test
    public void testConsumeCharacterReference_ValidCharacterReference() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&#123;"));
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        assertThat(charRef, is((char) 123));
    }

    @Test
    public void testConsumeCharacterReference_InvalidCharacterReference() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&#&"));
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(charRef);
    }

    @Test
    public void testConsumeCharacterReference_InvalidSequence() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&#123;&#123;"));
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(charRef);
    }

    @Test
    public void testConsumeCharacterReference_InvalidNamedEntity() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&foo;"));
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        assertNull(charRef);
    }

    @Test
    public void testConsumeCharacterReference_NameWithAttribute() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&foo; foo="));
        Character charRef = tokeniser.consumeCharacterReference(null, true);
        assertNull(charRef);
    }

    @Test
    public void testErrorHandling_UnexpectedCharacter() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("Test&"));
        try {
            tokeniser.consumeCharacterReference(null, false);
            fail("Expected error");
        } catch (ParseError e) {
            // Expected error
        }
    }

    @Test
    public void testErrorHandling_UnrecognizedNamedEntity() throws Exception {
        Tokeniser tokeniser = new Tokeniser(new StringReader("&bar;"));
        try {
            tokeniser.consumeCharacterReference(null, false);
            fail("Expected error");
        } catch (ParseError e) {
            // Expected error
        }
    }
}