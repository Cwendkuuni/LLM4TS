import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test when selfClosingFlagAcknowledged is false
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"), new ParseErrorList());
        tokeniser.acknowledgeSelfClosingFlag(); // set selfClosingFlagAcknowledged to false
        Token token = tokeniser.read();
        assertNull(token);
        // Add more test cases for different scenarios
    }

    @Test
    public void testEmit() {
        // Test emitting a simple token
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("input"), new ParseErrorList());
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.read());
        // Add more test cases for different scenarios
    }

    // Add more test methods for other public methods in Tokeniser class

}