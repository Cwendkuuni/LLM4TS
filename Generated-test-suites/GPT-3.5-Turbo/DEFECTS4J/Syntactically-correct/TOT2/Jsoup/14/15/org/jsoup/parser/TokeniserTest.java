import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        // Test reading tokens from Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("Test input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        // Act
        Token token = tokeniser.read();
        
        // Assert
        assertNotNull(token); // Ensure that a token is read
    }
    
    @Test
    public void testEmit() {
        // Test emitting tokens from Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("Test input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        Token token = new Token.Character("Test token");
        
        // Act
        tokeniser.emit(token);
        
        // Assert
        assertTrue(tokeniser.isEmitPending); // Ensure that token is pending for emission
    }
    
    @Test
    public void testCharacterReferenceError() {
        // Test handling error in character reference in Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("&#");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        // Act
        Character character = tokeniser.consumeCharacterReference(null, false);
        
        // Assert
        assertEquals(Character.valueOf('\uFFFD'), character); // Ensure that replacement character is returned
    }
    
    @Test
    public void testTrackErrors() {
        // Test tracking errors in Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        // Act
        tokeniser.error("Test error");
        
        // Assert
        assertFalse(tokeniser.errors.isEmpty()); // Ensure that error is tracked
    }
    
    @Test
    public void testCreateTagPending() {
        // Test creating tag pending in Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("Test input");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        // Act
        Token.Tag tag = tokeniser.createTagPending(true);
        
        // Assert
        assertNotNull(tag); // Ensure that tag is successfully created
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // Test checking appropriate end tag token in Tokeniser
        // Arrange
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        
        tokeniser.createTagPending(true);
        tokeniser.createTagPending(false);
        
        // Act
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        // Assert
        assertTrue(result); // Ensure that end tag token is appropriate
    }
    
}