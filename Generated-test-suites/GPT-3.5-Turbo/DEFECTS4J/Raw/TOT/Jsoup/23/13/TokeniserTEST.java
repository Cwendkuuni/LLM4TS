import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = Mockito.mock(CharacterReader.class);
        errors = Mockito.mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Setup
        Token tagToken = new Token.StartTag();
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('<');
        when(reader.consumeLetterSequence()).thenReturn("html");
        when(reader.matchConsume(">")).thenReturn(true);
        when(errors.canAddError()).thenReturn(true);

        // Action
        Token result = tokeniser.read();

        // Verify
        assertEquals(tagToken, result);
        verify(reader, times(1)).isEmpty();
        verify(reader, times(1)).current();
        verify(reader, times(1)).consumeLetterSequence();
        verify(reader, times(1)).matchConsume(">");
        verify(errors, times(1)).canAddError();
    }

    @Test
    public void testEmit() {
        // Setup
        String str = "<html>";
        StringBuilder charBuffer = new StringBuilder();
        charBuffer.append(str);
        Token characterToken = new Token.Character(str);
        when(errors.canAddError()).thenReturn(true);

        // Action
        tokeniser.emit(str);

        // Verify
        assertEquals(charBuffer.toString(), tokeniser.charBuffer.toString());
        verify(errors, times(1)).canAddError();
    }

    @Test
    public void testStateGetterAndTransition() {
        // Setup
        TokeniserState state = TokeniserState.Data;

        // Action
        TokeniserState result = tokeniser.getState();

        // Verify
        assertEquals(state, result);
        assertEquals(state, tokeniser.state);
    }

    @Test
    public void testAdvanceTransition() {
        // Setup
        TokeniserState state = TokeniserState.Doctype;
        when(reader.current()).thenReturn("a");

        // Action
        tokeniser.advanceTransition(state);

        // Verify
        verify(reader, times(1)).advance();
        assertEquals(state, tokeniser.state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Setup
        tokeniser.selfClosingFlagAcknowledged = false;

        // Action
        tokeniser.acknowledgeSelfClosingFlag();

        // Verify
        assertEquals(true, tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Setup
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character characterRef = 'b';
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('&');
        when(reader.matchesAny('\t', '\n', '\f', ' ', '<', '&')).thenReturn(false);
        when(reader.mark()).thenReturn(true);
        when(reader.matchConsume("#")).thenReturn(false);
        when(reader.matches(';')).thenReturn(true);
        when(reader.matchesLetter()).thenReturn(true);
        when(reader.matchesDigit()).thenReturn(false);
        when(reader.matchesAny('=', '-', '_')).thenReturn(false);
        when(reader.matchConsumeIgnoreCase("X")).thenReturn(false);
        when(reader.consumeHexSequence()).thenReturn("001F");
        when(reader.consumeDigitSequence()).thenReturn("");
        when(reader.matchConsume(";")).thenReturn(true);
        when(errors.canAddError()).thenReturn(true);

        // Action
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        // Verify
        assertEquals(characterRef, result);
        verify(reader, times(1)).isEmpty();
        verify(reader, times(1)).current();
        verify(reader, times(4)).matchesAny('\t', '\n', '\f', ' ', '<', '&');
        verify(reader, times(1)).mark();
        verify(reader, times(1)).matchConsume("#");
        verify(reader, times(1)).matches(";");
        verify(reader, times(1)).matchesLetter();
        verify(reader, times(1)).matchesDigit();
        verify(reader, times(1)).matchesAny('=', '-', '_');
        verify(reader, times(1)).matchConsumeIgnoreCase("X");
        verify(reader, times(1)).consumeHexSequence();
        verify(reader, times(1)).consumeDigitSequence();
        verify(reader, times(2)).matchConsume(";");
        verify(errors, times(1)).canAddError();
        verify(reader, times(0)).rewindToMark();
    }

    @Test
    public void testCreateTagPending() {
        // Setup
        boolean start = true;
        Token.Tag expectedTag = new Token.StartTag();

        // Action
        Token.Tag result = tokeniser.createTagPending(start);

        // Verify
        assertEquals(expectedTag.getClass(), result.getClass());
        assertEquals(expectedTag.getClass(), tokeniser.tagPending.getClass());
    }

    @Test
    public void testEmitTagPending() {
        // Setup
        Token.Tag tag = Mockito.mock(Token.Tag.class);

        // Action
        tokeniser.emitTagPending();

        // Verify
        verify(tag, times(1)).finaliseTag();
        verify(tag, times(1)).finaliseTag();
    }

    @Test
    public void testCreateCommentPending() {
        // Setup
        Token.Comment comment = new Token.Comment();

        // Action
        tokeniser.createCommentPending();

        // Verify
        assertEquals(comment.getClass(), tokeniser.commentPending.getClass());
    }

    @Test
    public void testEmitCommentPending() {
        // Setup
        Token.Comment comment = Mockito.mock(Token.Comment.class);

        // Action
        tokeniser.emitCommentPending();

        // Verify
        verify(comment, times(1)).emit();
    }

    @Test
    public void testCreateDoctypePending() {
        // Setup
        Token.Doctype doctype = new Token.Doctype();

        // Action
        tokeniser.createDoctypePending();

        // Verify
        assertEquals(doctype.getClass(), tokeniser.doctypePending.getClass());
    }

    @Test
    public void testEmitDoctypePending() {
        // Setup
        Token.Doctype doctype = Mockito.mock(Token.Doctype.class);

        // Action
        tokeniser.emitDoctypePending();

        // Verify
        verify(doctype, times(1)).emit();
    }

    @Test
    public void testCreateTempBuffer() {
        // Setup
        StringBuilder dataBuffer = new StringBuilder();

        // Action
        tokeniser.createTempBuffer();

        // Verify
        assertEquals(dataBuffer.getClass(), tokeniser.dataBuffer.getClass());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Setup
        tokeniser.tagPending = Mockito.mock(Token.StartTag.class);
        tokeniser.lastStartTag = Mockito.mock(Token.StartTag.class);
        tokeniser.tagPending.tagName = "html";
        tokeniser.lastStartTag.tagName = "html";

        // Action
        boolean result = tokeniser.isAppropriateEndTagToken();

        // Verify
        assertEquals(true, result);
        assertEquals("html", tokeniser.lastStartTag.tagName);
        assertEquals("html", tokeniser.lastStartTag.tagName);
    }

    @Test
    public void testAppropriateEndTagName() {
        // Setup
        tokeniser.lastStartTag = Mockito.mock(Token.StartTag.class);
        tokeniser.lastStartTag.tagName = "html";
        String expectedTagName = "html";

        // Action
        String result = tokeniser.appropriateEndTagName();

        // Verify
        assertEquals(expectedTagName, result);
        assertEquals(expectedTagName, tokeniser.lastStartTag.tagName);
    }

    @Test
    public void testErrorWithState() {
        // Setup
        TokeniserState state = TokeniserState.TagName;
        when(errors.canAddError()).thenReturn(true);

        // Action
        tokeniser.error(state);

        // Verify
        verify(errors, times(1)).canAddError();
//        verify(errors, times(1)).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testEofErrorWithState() {
        // Setup
        TokeniserState state = TokeniserState.AttrName;
        when(errors.canAddError()).thenReturn(true);

        // Action
        tokeniser.eofError(state);

        // Verify
        verify(errors, times(1)).canAddError();
//        verify(errors, times(1)).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testCharacterReferenceError() {
        // Setup
        String message = "Invalid character reference: test";
        when(errors.canAddError()).thenReturn(true);

        // Action
        tokeniser.characterReferenceError(message);

        // Verify
        verify(errors, times(1)).canAddError();
//        verify(errors, times(1)).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testErrorWithMsg() {
        // Setup
        String errorMsg = "Invalid character";
        when(errors.canAddError()).thenReturn(true);

        // Action
        tokeniser.error(errorMsg);

        // Verify
        verify(errors, times(1)).canAddError();
//        verify(errors, times(1)).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Action
        boolean result = tokeniser.currentNodeInHtmlNS();

        // Verify
        assertEquals(true, result);
    }
}