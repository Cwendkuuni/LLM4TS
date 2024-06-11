import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(), errors);
    }
    
    @Test
    public void read_ShouldReturnCharacterToken_WhenCharacterBufferNotEmptyAndEmitPendingFalse() {
        // Arrange
        tokeniser.charBuffer.append("Hello");
        tokeniser.isEmitPending = false;
        
        // Act
        Token result = tokeniser.read();
        
        // Assert
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("Hello", ((Token.Character) result).getData());
    }
    
    @Test
    public void read_ShouldReturnEmitPendingToken_WhenCharacterBufferIsEmptyAndEmitPendingTrue() {
        // Arrange
        Token pendingToken = new Token.StartTag();
        tokeniser.emitPending = pendingToken;
        tokeniser.isEmitPending = true;
        
        // Act
        Token result = tokeniser.read();
        
        // Assert
        assertEquals(pendingToken, result);
        assertEquals(false, tokeniser.isEmitPending);
    }
    
    @Test
    public void read_ShouldReturnCharacterToken_WhenCharacterBufferEmptyAndEmitPendingFalse() {
        // Arrange
        tokeniser.charBuffer.setLength(0);
        tokeniser.isEmitPending = false;
        tokeniser.reader = new CharacterReader(new StringReader("Hello"));
        
        // Act
        Token result = tokeniser.read();
        
        // Assert
        assertEquals(Token.TokenType.Character, result.type);
        assertEquals("Hello", ((Token.Character) result).getData());
    }
    
    @Test
    public void emit_ShouldSetEmitPendingAndStartTagToken_WhenTokenTypeIsStartTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        
        // Act
        tokeniser.emit(startTag);
        
        // Assert
        assertEquals(true, tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }
    
    @Test
    public void emit_ShouldSetEmitPendingAndCharacterToken_WhenTokenTypeIsCharacter() {
        // Arrange
        Token.Character character = new Token.Character("Hello");
        
        // Act
        tokeniser.emit(character);
        
        // Assert
        assertEquals(true, tokeniser.isEmitPending);
        assertEquals(character, tokeniser.emitPending);
    }
    
    @Test
    public void emit_ShouldSetEmitPendingAndDoctypeToken_WhenTokenTypeIsDoctype() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype();
        
        // Act
        tokeniser.emit(doctype);
        
        // Assert
        assertEquals(true, tokeniser.isEmitPending);
        assertEquals(doctype, tokeniser.emitPending);
    }
    
    @Test
    public void error_ShouldAddParseError_WhenErrorsListCanAddError() {
        // Arrange
        String errorMsg = "Unexpected token";
        
        // Act
        tokeniser.error(errorMsg);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals(errorMsg, errors.get(0).getErrorMessage());
    }
    
    @Test
    public void error_ShouldAddParseError_WhenErrorsListCannotAddError() {
        // Arrange
        String errorMsg = "Unexpected token";
        errors.setCanAddError(false);
        
        // Act
        tokeniser.error(errorMsg);
        
        // Assert
        assertEquals(0, errors.size());
    }
    
    @Test
    public void eofError_ShouldAddParseError_WhenErrorsListCanAddError() {
        // Arrange
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        
        // Act
        tokeniser.eofError(state);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [" + state + "]", errors.get(0).getErrorMessage());
    }
    
    @Test
    public void eofError_ShouldAddParseError_WhenErrorsListCannotAddError() {
        // Arrange
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        errors.setCanAddError(false);
        
        // Act
        tokeniser.eofError(state);
        
        // Assert
        assertEquals(0, errors.size());
    }
    
    @Test
    public void characterReferenceError_ShouldAddParseError_WhenErrorsListCanAddError() {
        // Arrange
        String message = "Invalid character reference";
        
        // Act
        tokeniser.characterReferenceError(message);
        
        // Assert
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: " + message, errors.get(0).getErrorMessage());
    }
    
    @Test
    public void characterReferenceError_ShouldAddParseError_WhenErrorsListCannotAddError() {
        // Arrange
        String message = "Invalid character reference";
        errors.setCanAddError(false);
        
        // Act
        tokeniser.characterReferenceError(message);
        
        // Assert
        assertEquals(0, errors.size());
    }
    
    @Test
    public void currentNodeInHtmlNS_ShouldReturnTrue_Always() {
        // Act
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        // Assert
        assertEquals(true, result);
    }
}