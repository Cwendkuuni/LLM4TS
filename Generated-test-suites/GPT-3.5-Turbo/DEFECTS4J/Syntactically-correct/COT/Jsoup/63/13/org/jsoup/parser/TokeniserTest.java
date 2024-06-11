import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    // Test Tokeniser.read() method
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        // Add test cases for the reader method
        // ...

        token = tokeniser.read();
        assertNotNull(token);
    }

    // Test Tokeniser.emit(Token token) method
    @Test
    public void testEmitToken() {
        Token token = new Token.StartTag();

        tokeniser.emit(token);

        Token emitPending = tokeniser.read();
        assertEquals(token, emitPending);
    }

    // Test Tokeniser.emit(String str) method
    @Test
    public void testEmitString() {
        String str = "test string";

        tokeniser.emit(str);

        Token emitPending = tokeniser.read();
        assertEquals(str, ((Token.Character) emitPending).getData());
    }

    // Test Tokeniser.emit(char[] chars) method
    @Test
    public void testEmitCharArray() {
        char[] chars = "test".toCharArray();

        tokeniser.emit(chars);

        Token emitPending = tokeniser.read();
        assertEquals("test", ((Token.Character) emitPending).getData());
    }

    // Test Tokeniser.getCodepointHolder() method
    @Test
    public void testGetCodepointHolder() {
        int[] codepointHolder = tokeniser.getCodepointHolder();
        assertNotNull(codepointHolder);
    }

    // Test Tokeniser.getMultipointHolder() method
    @Test
    public void testGetMultipointHolder() {
        int[] multipointHolder = tokeniser.getMultipointHolder();
        assertNotNull(multipointHolder);
    }

    // Add more test cases for other methods in the Tokeniser class

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.DoctypeName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertTrue(tokeniser.getReader().isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.getSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        char[] additionalAllowedCharacter = {'<'};
        int[] codeRef = tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
        assertNull(codeRef);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        tokeniser.createTagPending(start);
        assertNotNull(tokeniser.getTagPending());
    }

    // Add more test cases for other methods in the Tokeniser class

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.setLastStartTag("startTag");
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.setLastStartTag("startTag");
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("startTag", tagName);
    }

    @Test
    public void testErrorString() {
        tokeniser.error("Error message");
        ParseError error = errors.get(0);
        assertNotNull(error);
        assertEquals("Error message", error.getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
    
    
    // Add more test cases for other methods in the Tokeniser class
    
}