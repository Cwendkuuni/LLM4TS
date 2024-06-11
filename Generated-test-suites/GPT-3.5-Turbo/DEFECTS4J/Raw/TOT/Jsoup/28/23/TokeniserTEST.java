import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);
        assertEquals(startTag, tokeniser.read());
    }

    @Test
    public void testEmit_String() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        String str = "Test";
        tokeniser.emit(str);
        Token.Character character = (Token.Character) tokeniser.read();
        assertEquals(str, character.getData());
    }

    @Test
    public void testEmit_Char() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        char c = 'T';
        tokeniser.emit(c);
        Token.Character character = (Token.Character) tokeniser.read();
        assertEquals(Character.toString(c), character.getData());
    }

    @Test
    public void testTokeniserState() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
        tokeniser.advanceTransition(TokeniserState.TagAttributes);
        assertEquals(TokeniserState.TagAttributes, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#65;"), new ParseErrorList());
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', character.charValue());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTagPending(true);
        Token.Tag tag = tokeniser.getTagPending();
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.getCommentPending());
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.getDoctypePending());
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.getDataBuffer());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.createTagPending(true);
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals(tokeniser.getTagPending().tagName, tagName);
    }

    @Test
    public void testError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error(TokeniserState.Data);
        ParseErrorList errorList = tokeniser.getErrors();
        assertEquals(1, errorList.size());
    }

    @Test
    public void testEofError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.eofError(TokeniserState.Data);
        ParseErrorList errorList = tokeniser.getErrors();
        assertEquals(1, errorList.size());
    }

    @Test
    public void testCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.characterReferenceError("Invalid character reference");
        ParseErrorList errorList = tokeniser.getErrors();
        assertEquals(1, errorList.size());
    }

    @Test
    public void testErrorString() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser.error("Error message");
        ParseErrorList errorList = tokeniser.getErrors();
        assertEquals(1, errorList.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}