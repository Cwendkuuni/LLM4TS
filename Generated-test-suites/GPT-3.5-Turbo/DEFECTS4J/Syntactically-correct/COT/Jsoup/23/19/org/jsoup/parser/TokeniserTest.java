import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.parser.ParseErrorList;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }
    
    @Test
    public void testRead() {
        // TODO: Test the read() method
    }
    
    @Test
    public void testEmit() {
        // TODO: Test the emit(Token token) method
    }
    
    @Test
    public void testEmitString() {
        // TODO: Test the emit(String str) method
    }
    
    @Test
    public void testEmitChar() {
        // TODO: Test the emit(char c) method
    }
    
    @Test
    public void testGetState() {
        // TODO: Test the getState() method
    }
    
    @Test
    public void testTransition() {
        // TODO: Test the transition(TokeniserState state) method
    }
    
    @Test
    public void testAdvanceTransition() {
        // TODO: Test the advanceTransition(TokeniserState state) method
    }
    
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // TODO: Test the acknowledgeSelfClosingFlag() method
    }
    
    @Test
    public void testConsumeCharacterReference() {
        // TODO: Test the consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) method
    }
    
    @Test
    public void testCreateTagPending() {
        // TODO: Test the createTagPending(boolean start) method
    }
    
    @Test
    public void testEmitTagPending() {
        // TODO: Test the emitTagPending() method
    }
    
    @Test
    public void testCreateCommentPending() {
        // TODO: Test the createCommentPending() method
    }
    
    @Test
    public void testEmitCommentPending() {
        // TODO: Test the emitCommentPending() method
    }
    
    @Test
    public void testCreateDoctypePending() {
        // TODO: Test the createDoctypePending() method
    }
    
    @Test
    public void testEmitDoctypePending() {
        // TODO: Test the emitDoctypePending() method
    }
    
    @Test
    public void testCreateTempBuffer() {
        // TODO: Test the createTempBuffer() method
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        // TODO: Test the isAppropriateEndTagToken() method
    }
    
    @Test
    public void testAppropriateEndTagName() {
        // TODO: Test the appropriateEndTagName() method
    }
    
    @Test
    public void testError() {
        // TODO: Test the error(TokeniserState state) method
    }
    
    @Test
    public void testEofError() {
        // TODO: Test the eofError(TokeniserState state) method
    }
    
    @Test
    public void testCharacterReferenceError() {
        // TODO: Test the characterReferenceError(String message) method
    }
    
    @Test
    public void testErrorString() {
        // TODO: Test the error(String errorMsg) method
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        // TODO: Test the currentNodeInHtmlNS() method
    }
}