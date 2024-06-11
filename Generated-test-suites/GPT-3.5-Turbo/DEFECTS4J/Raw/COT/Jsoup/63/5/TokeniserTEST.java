import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser testTokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        testTokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = testTokeniser.read();
        
        // Check if token is not null
        assertNotNull(token);
        
        // Check if token type is correct
        assertEquals(Token.TokenType.StartTag, token.getType());
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();

        testTokeniser.emit(token);

        // Check if emitPending is set correctly
        assertTrue(testTokeniser.isEmitPending());

        // Check if emitPending is the expected token
        assertEquals(token, testTokeniser.getEmitPending());
    }

    @Test
    public void testEmitString() {
        String str = "Test string";

        testTokeniser.emit(str);

        // Check if charsString is set correctly
        assertEquals(str, testTokeniser.getCharsString());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'T', 'e', 's', 't'};

        testTokeniser.emit(chars);

        // Check if charsString is set correctly
        assertEquals("Test", testTokeniser.getCharsString());
    }

    @Test
    public void testEmitIntArry() {
        int[] codepoints = {84, 101, 115, 116};

        testTokeniser.emit(codepoints);

        // Check if charsString is set correctly
        assertEquals("Test", testTokeniser.getCharsString());
    }

    @Test
    public void testEmitChar() {
        char c = 'T';

        testTokeniser.emit(c);

        // Check if charsString is set correctly
        assertEquals("T", testTokeniser.getCharsString());
    }

    @Test
    public void testGetState() {
        TokeniserState state = testTokeniser.getState();

        // Check if initial state is correct
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Comment;

        testTokeniser.transition(newState);

        // Check if state is transitioned correctly
        assertEquals(newState, testTokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Doctype;

        testTokeniser.advanceTransition(newState);

        // Check if state is advanced and transitioned correctly
        assertEquals(newState, testTokeniser.getState());
        assertEquals(1, testTokeniser.getReader().pos());
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        testTokeniser.acknowledgeSelfClosingFlag();
        
        // Check if selfClosingFlagAcknowledged is set correctly
        assertTrue(testTokeniser.isSelfClosingFlagAcknowledged());
    }
    
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'A';
        boolean inAttribute = true;
        
        int[] codeRef = testTokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        
        // Check if codeRef is null (as reader is empty)
        assertNull(codeRef);
    }
    
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        
        Tokeniser.Tag tag = testTokeniser.createTagPending(start);
        
        // Check if tag is created correctly
        assertNotNull(tag);
        assertEquals(start, tag.isStartTag());
    }
    
    @Test
    public void testEmitTagPending() {
        testTokeniser.createTagPending(true);
        
        testTokeniser.emitTagPending();
        
        // Check if emitPending is set correctly
        assertTrue(testTokeniser.isEmitPending());
        
        // Check if emitPending is the expected token
        assertNotNull(testTokeniser.getEmitPending());
    }
    
    @Test
    public void testCreateCommentPending() {
        testTokeniser.createCommentPending();
        
        // Check if commentPending is created correctly
        assertNotNull(testTokeniser.getCommentPending());
    }
    
    @Test
    public void testEmitCommentPending() {
        testTokeniser.createCommentPending();
        
        testTokeniser.emitCommentPending();
        
        // Check if emitPending is set correctly
        assertTrue(testTokeniser.isEmitPending());
        
        // Check if emitPending is the expected token
        assertNotNull(testTokeniser.getEmitPending());
    }
    
    @Test
    public void testCreateDoctypePending() {
        testTokeniser.createDoctypePending();
        
        // Check if doctypePending is created correctly
        assertNotNull(testTokeniser.getDoctypePending());
    }
    
    @Test
    public void testEmitDoctypePending() {
        testTokeniser.createDoctypePending();
        
        testTokeniser.emitDoctypePending();
        
        // Check if emitPending is set correctly
        assertTrue(testTokeniser.isEmitPending());
        
        // Check if emitPending is the expected token
        assertNotNull(testTokeniser.getEmitPending());
    }
    
    @Test
    public void testCreateTempBuffer() {
        testTokeniser.createTempBuffer();
        
        // Check if dataBuffer is created correctly
        assertNotNull(testTokeniser.getDataBuffer());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        String startTag = "test";
        testTokeniser.setLastStartTag(startTag);
        
        boolean appropriate = testTokeniser.isAppropriateEndTagToken();
        
        // Check if appropriate is true
        assertTrue(appropriate);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        String startTag = "test";
        testTokeniser.setLastStartTag(startTag);
        
        String endTagName = testTokeniser.appropriateEndTagName();
        
        // Check if endTagName is correct
        assertEquals(startTag, endTagName);
    }
    
    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        
        testTokeniser.error(state);
        
        // Check if error is added correctly
        assertEquals(1, testTokeniser.getErrors().size());
    }
    
    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Comment;
        
        testTokeniser.eofError(state);
        
        // Check if error is added correctly
        assertEquals(1, testTokeniser.getErrors().size());
    }
    
    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        
        testTokeniser.characterReferenceError(message);
        
        // Check if error is added correctly
        assertEquals(1, testTokeniser.getErrors().size());
    }
    
    @Test
    public void testErrorString() {
        String errorMsg = "Error message";
        
        testTokeniser.error(errorMsg);
        
        // Check if error is added correctly
        assertEquals(1, testTokeniser.getErrors().size());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = testTokeniser.currentNodeInHtmlNS();
        
        // Check if currentNodeInHtmlNS returns true (default behavior)
        assertTrue(currentNodeInHtmlNS);
    }
    
    @Test
    public void testUnescapeEntities() {
        boolean inAttribute = true;
        
        String unescaped = testTokeniser.unescapeEntities(inAttribute);
        
        // Check if unescaped string is not empty
        assertNotEquals("", unescaped);
    }
}