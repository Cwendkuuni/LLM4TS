
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        // Create an instance of Tokeniser with a mock reader and error list
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    // Test the read() method
    @Test
    public void testRead() {
        Token token = tokeniser.read(); // Call the read() method
        
        // Assert that the method returns a non-null value
        assertNotNull(token);
    }
    
    // Test the emit() method
    @Test
    public void testEmit() {
        Token token = new Token(); // Create a mock token
        tokeniser.emit(token); // Call the emit() method
        
        // Assert that the emitPending variable is set to the emitted token
        assertEquals(token, tokeniser.emitPending);
        
        // Assert that the isEmitPending variable is set to true
        assertTrue(tokeniser.isEmitPending);
    }
    
    // Test the emit(String) method
    @Test
    public void testEmitString() {
        String str = "test"; // Create a mock string
        tokeniser.emit(str); // Call the emit() method
        
        // Assert that the charsString variable is set to the emitted string
        assertEquals(str, tokeniser.charsString);
    }
    
    // Test the emit(char[]) method
    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'}; // Create a mock char array
        tokeniser.emit(chars); // Call the emit() method
        
        // Assert that the charsString variable is set to the emitted string
        assertEquals(new String(chars), tokeniser.charsString);
    }
    
    // Test the emit(int[]) method
    @Test
    public void testEmitIntArray() {
        int[] codepoints = {116, 101, 115, 116}; // Create a mock int array
        tokeniser.emit(codepoints); // Call the emit() method
        
        // Assert that the charsString variable is set to the emitted string
        assertEquals(new String(codepoints, 0, codepoints.length), tokeniser.charsString);
    }
    
    // Test the emit(char) method
    @Test
    public void testEmitChar() {
        char c = 't'; // Create a mock char
        tokeniser.emit(c); // Call the emit() method
        
        // Assert that the charsString variable is set to the emitted string
        assertEquals(String.valueOf(c), tokeniser.charsString);
    }
    
    // Test the getState() method
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState(); // Call the getState() method
        
        // Assert that the method returns a non-null value
        assertNotNull(state);
    }
    
    // Test the transition(TokeniserState) method
    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data; // Create a mock state
        tokeniser.transition(state); // Call the transition() method
        
        // Assert that the state variable is set to the transitioned state
        assertEquals(state, tokeniser.getState());
    }

    // Test the advanceTransition(TokeniserState) method
    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName; // Create a mock state
        tokeniser.advanceTransition(state); // Call the advanceTransition() method
        
        // Assert that the state variable is set to the advanced state
        assertEquals(state, tokeniser.getState());
        
        // Assert that the reader's position is advanced by one character
        assertEquals(1, tokeniser.reader.pos());
    }
    
    // Test the acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag(); // Call the acknowledgeSelfClosingFlag() method
        
        // Assert that the selfClosingFlagAcknowledged variable is set to true
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }
}
