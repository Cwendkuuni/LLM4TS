import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead_emptyCharacterReader_returnsNullToken() {
        // Arrange
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        Token token = tokeniser.read();

        // Assert
        assertNull(token);
    }

    @Test
    public void testRead_startTagToken_returnsCharacterToken() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        Token token = tokeniser.read();

        // Assert
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("<div>", ((Token.Character)token).getData());
    }

    @Test
    public void testEmit_emitToken_tokenPendingTrue() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = new Token.Character("<div>");

        // Act
        tokeniser.emit(token);

        // Assert
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmit_emitCharacter_dataBufferNotEmpty() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        tokeniser.emit("<div>");

        // Assert
        assertEquals("<div>", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testCreateTagPending_startTag_startTagPendingNotNull() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        Token.Tag tag = tokeniser.createTagPending(true);

        // Assert
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testIsAppropriateEndTagToken_sameTagName_true() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.createTagPending(false);

        // Act
        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();

        // Assert
        assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName_sameTagName_tagName() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.createTagPending(false);

        // Act
        String appropriateEndTagName = tokeniser.appropriateEndTagName();

        // Assert
        assertEquals("div", appropriateEndTagName);
    }

    @Test
    public void testError_errorCanAddError_errorAddedToList() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        tokeniser.error("Unknown tag");

        // Assert
        assertEquals(1, errors.size());
        assertEquals("Unknown tag", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_errorCanAddError_errorAddedToList() {
        // Arrange
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        tokeniser.eofError(TokeniserState.Data);

        // Assert
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError_errorCanAddError_errorAddedToList() {
        // Arrange
        CharacterReader reader = new CharacterReader("&#;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        tokeniser.consumeCharacterReference(null, false);

        // Assert
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: numeric reference with no numerals", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_noCurrentNode_returnsTrue() {
        // Arrange
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // Act
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        // Assert
        assertTrue(currentNodeInHtmlNS);
    }
}