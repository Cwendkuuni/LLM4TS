import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testTokeniserRead() {
        // Arrange
        CharacterReader reader = new CharacterReader("Test Input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        Token token = tokeniser.read();

        // Assert
        assertNotNull(token);
    }

    @Test
    public void testTokeniserEmit() {
        // Arrange
        CharacterReader reader = new CharacterReader("Test Input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = new Token.Character();

        // Act
        tokeniser.emit(token);

        // Assert
        assertTrue(tokeniser.getState() == TokeniserState.Data);
    }

    @Test
    public void testTokeniserConsumeCharacterReference() {
        // Arrange
        CharacterReader reader = new CharacterReader("Test Input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        int[] result = tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertNull(result);
    }

    // Add more test cases for other methods

}