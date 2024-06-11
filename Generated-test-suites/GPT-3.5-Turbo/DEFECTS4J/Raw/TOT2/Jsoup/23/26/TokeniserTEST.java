import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    
    @Before
    public void setup() {
        // Initialize Tokeniser with dummy CharacterReader and ParseErrorList
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        
        // Ensure first token is not null
        assertNotNull(token);
        
        // Test if token is of type Character
        assertTrue(token instanceof Token.Character);
    }
    
    @Test
    public void testEmit() {
        Token startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        
        assertNotNull(tokeniser.read());
        assertTrue(tokeniser.read() instanceof Token.StartTag);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'A';
        boolean inAttribute = true;
        
        // Test case for consuming character reference
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
        
        // Test case for invalid numeric reference with no numerals
        tokeniser.reader = new CharacterReader("&#;");
        result = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(result);
        assertEquals((char) 0xFFFD, result.charValue());
        
        // Add more test cases for named character references
    }
    
    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
        
        assertEquals("", tag.tagName);
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "testTag";
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTag";
        tokeniser.lastStartTag = startTag;
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        
        // Check if error is added in ParseErrorList
        assertTrue(tokeniser.errors.size() > 0);
    }
    
    // Add more test cases for other public methods
    
}