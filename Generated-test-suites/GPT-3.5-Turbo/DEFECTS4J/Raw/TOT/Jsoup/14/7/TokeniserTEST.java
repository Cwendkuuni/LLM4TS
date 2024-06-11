import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("sample HTML input");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_validCharacter_returnsCharacterToken() {
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals("sample HTML input", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_invalidCharacter_returnsError() {
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);

        Token token = tokeniser.read();
        assertNull(token);
        assertTrue(tokeniser.getState() instanceof TokeniserState.Data);
        assertTrue(tokeniser.isTrackErrors());
        assertFalse(tokeniser.getErrors().isEmpty());
        assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.getErrors().get(0).getMessage());
    }

    @Test
    public void testEmit_startTagToken_emitsStartTagToken() {
        TokeniserState currentState = tokeniser.getState();
        Token.Tag tagToken = tokeniser.createTagPending(true);

        tokeniser.emit(tagToken);

        assertEquals(tagToken, tokeniser.getEmitPending());
        assertTrue(tokeniser.isEmitPending());
        assertEquals(currentState, tokeniser.getState());
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testEmit_endTagTokenWithAttributes_emitsError() {
        Token.Tag tagToken = tokeniser.createTagPending(false);
        tagToken.getAttributes().add(new Token.Attribute("attr1", "value1"));

        tokeniser.emit(tagToken);

        assertNull(tokeniser.getEmitPending());
        assertFalse(tokeniser.isEmitPending());
        assertTrue(tokeniser.isTrackErrors());
        assertFalse(tokeniser.getErrors().isEmpty());
        assertEquals("Attributes incorrectly present on end tag", tokeniser.getErrors().get(0).getMessage());
    }

    @Test
    public void testEmit_stringToken_emitsStringToken() {
        String str = "sample string";

        tokeniser.emit(str);

        assertEquals(Token.TokenType.Character, tokeniser.getEmitPending().getType());
        assertEquals(str, ((Token.Character) tokeniser.getEmitPending()).getData());
        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testTransition_changesState() {
        tokeniser.transition(TokeniserState.Null);

        assertEquals(TokeniserState.Null, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_advancesAndChangesState() {
        TokeniserState currentState = tokeniser.getState();

        tokeniser.advanceTransition(TokeniserState.EndTagOpen);

        assertEquals(TokeniserState.EndTagOpen, tokeniser.getState());
        assertEquals(currentState, tokeniser.getReader().getPos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setsFlagToTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference_validCharacterReference_returnsCharacter() {
        reader = new CharacterReader("&#x0041;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_invalidCharacterReference_returnsError() {
        reader = new CharacterReader("&#x00;");
        tokeniser = new Tokeniser(reader);

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('\uFFFD', character.charValue());
        assertFalse(tokeniser.getErrors().isEmpty());
        assertEquals("Invalid character reference", tokeniser.getErrors().get(0).getMessage());
    }

    @Test
    public void testCreateTagPending_startTag_returnsStartTagToken() {
        Token.Tag startTagToken = tokeniser.createTagPending(true);

        assertTrue(startTagToken instanceof Token.StartTag);
        assertTrue(tokeniser.isTagPending());
        assertEquals(startTagToken, tokeniser.getTagPending());
    }

    @Test
    public void testEmitTagPending_emitsTagToken() {
        Token.Tag tagToken = tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        assertFalse(tokeniser.isTagPending());
        assertEquals(tagToken, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateCommentPending_createsCommentToken() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.isCommentPending());
        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending_emitsCommentToken() {
        Token.Comment commentToken = new Token.Comment();
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        assertFalse(tokeniser.isCommentPending());
        assertEquals(commentToken, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateDoctypePending_createsDoctypeToken() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.isDoctypePending());
        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending_emitsDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype();
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        assertFalse(tokeniser.isDoctypePending());
        assertEquals(doctypeToken, tokeniser.getEmitPending());
    }

    @Test
    public void testCreateTempBuffer_createsDataBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken_sameTagName_returnsTrue() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.setTagName("tag");

        tokeniser.createTagPending(false);
        tokeniser.createTagPending(true);
        tokeniser.getLastStartTag().setTagName("tag");

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_differentTagName_returnsFalse() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.setTagName("tag");

        tokeniser.createTagPending(false);
        tokeniser.createTagPending(true);
        tokeniser.getLastStartTag().setTagName("other");

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS_validNode_returnsTrue() {
        boolean result = tokeniser.currentNodeInHtmlNS();
      
        assertTrue(result);
    }

}