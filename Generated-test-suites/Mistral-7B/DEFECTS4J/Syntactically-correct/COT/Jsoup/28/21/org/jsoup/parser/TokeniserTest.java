import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * JUnit test file for Tokeniser class.
 */
public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    @Mock
    private ParseErrorList errors;

    @InjectMocks
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test all methods in Tokeniser class

    @Test
    public void testTokeniserConstructor() {
        tokeniser = new Tokeniser(reader, errors);
        assertNotNull(tokeniser.reader);
        assertNotNull(tokeniser.errors);
    }

    @Test
    public void testReadChar() {
        when(reader.current()).thenReturn('a');
        tokeniser.read();
        ArgumentCaptor<Character> charCaptor = ArgumentCaptor.forClass(Character.class);
        verify(tokeniser).emit(charCaptor.capture());
        assertEquals('a', charCaptor.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWhenIsEmitPending() {
        tokeniser.emit(new Token.Character('a'));
        tokeniser.read();
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.Character('a'));
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokeniser).isEmitPending();
        verify(tokeniser, times(0)).emitPending;
        verify(tokeniser).emit(tokenCaptor.capture());
        assertEquals(Token.TokenType.Character, tokenCaptor.getValue().type);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("hello");
        ArgumentCaptor<String> strCaptor = ArgumentCaptor.forClass(String.class);
        verify(tokeniser).emit(strCaptor.capture());
        assertEquals("hello", strCaptor.getValue());
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('a');
        ArgumentCaptor<Character> charCaptor = ArgumentCaptor.forClass(Character.class);
        verify(tokeniser).emit(charCaptor.capture());
        assertEquals('a', charCaptor.getValue());
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.StartTagBegin);
        assertEquals(TokeniserState.StartTagBegin, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.StartTagBegin);
        tokeniser.advance();
        assertEquals(TokeniserState.InsideHTML, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        verify(tokeniser).selfClosingFlagAcknowledged;
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeCharacterReferenceWithNullCharacter() {
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testConsumeCharacterReferenceWithValidCharacter() {
        when(reader.current()).thenReturn('&');
        tokeniser.consumeCharacterReference(null, false);
        verifyZeroInteractions(tokeniser);
        verify(reader).consume('&');
    }

    @Test
    public void testConsumeCharacterReferenceWithDigitSequence() {
        when(reader.current()).thenReturn('&', '1');
        tokeniser.consumeCharacterReference(null, false);
        verifyZeroInteractions(tokeniser);
        verify(reader).consume('&');
        verify(reader).consume('1');
        verifyZeroInteractions(tokeniser);
    }

    @Test
    public void testConsumeCharacterReferenceWithHexSequence() {
        when(reader.current()).thenReturn('&', 'X', 'A', '9');
        tokeniser.consumeCharacterReference(null, false);
        verifyZeroInteractions(tokeniser);
        verify(reader).consume('&');
        verify(reader).consume('X');
        verify(reader).consume('A');
        verify(reader).consume('9');
        verifyZeroInteractions(tokeniser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeCharacterReferenceWithInvalidCharacter() {
        when(reader.current()).thenReturn('&');
        tokeniser.consumeCharacterReference('a', false);
        verifyZeroInteractions(tokeniser);
        verify(reader).consume('&');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsumeCharacterReferenceWithInvalidCharacterInAttribute() {
        when(reader.current()).thenReturn('&');
        tokeniser.consumeCharacterReference(null, true);
        verifyZeroInteractions(tokeniser);
        verify(reader).consume('&');
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertEquals(Token.TagType.StartTag, tokeniser.tagPending.tagType);
        assertEquals(Token.Tag.EMPTY_ELEMENT_TAG, tokeniser.tagPending.tagName);
    }

    @Test
    public void testCreateTagPendingWithName() {
        tokeniser.createTagPending(false);
        tokeniser.tagPending.tagName = "div";
        assertEquals("div", tokeniser.tagPending.tagName);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        ArgumentCaptor<Token.StartTag> startTagCaptor = ArgumentCaptor.forClass(Token.StartTag.class);
        verify(tokeniser).emit(startTagCaptor.capture());
        assertEquals(Token.TagType.StartTag, startTagCaptor.getValue().type);
        assertEquals(Token.Tag.EMPTY_ELEMENT_TAG, startTagCaptor.getValue().tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertEquals(Token.TagType.Comment, tokeniser.commentPending.tagType);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        ArgumentCaptor<Token.Comment> commentCaptor = ArgumentCaptor.forClass(Token.Comment.class);
        verify(tokeniser).emit(commentCaptor.capture());
        assertEquals(Token.TagType.Comment, commentCaptor.getValue().type);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertEquals(Token.TagType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        ArgumentCaptor<Token.Doctype> doctypeCaptor = ArgumentCaptor.forClass(Token.Doctype.class);
        verify(tokeniser).emit(doctypeCaptor.capture());
        assertEquals(Token.TagType.Doctype, doctypeCaptor.getValue().type);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals(new StringBuilder(), tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.tagPending = startTag;
        tokeniser.lastStartTag = startTag;
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.tagPending = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        assertEquals("div", tokeniser.appropriateEndTagName());
        tokeniser.lastStartTag = null;
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorWhenInHtmlNS() {
        tokeniser.currentNodeInHtmlNS();
        tokeniser.error(TokeniserState.StartTagBegin);
    }

    // Test edge cases and exception handling scenarios

    @Test(expected = IllegalStateException.class)
    public void testErrorDuringReadWhenNoErrorsCanAdd() {
        when(errors.canAddError()).thenReturn(false);
        tokeniser.error(TokeniserState.StartTagBegin);
    }

    @Test
    public void testErrorWhenEmptyFile() {
        when(reader.isEmpty()).thenReturn(true);
        tokeniser.eofError(TokeniserState.StartTagBegin);
        verify(errors).add(eq(new ParseError(anyInt(), "Unexpectedly reached end of file (EOF) in input state [StartTagBegin]")));
    }

    @Test
    public void testErrorWhenUnexpectedCharacter() {
        when(reader.current()).thenReturn('%');
        tokeniser.error(TokeniserState.StartTagBegin);
        verify(errors).add(eq(new ParseError(anyInt(), "Unexpected character '%%' in input state [StartTagBegin]")));
    }

    @Test
    public void testErrorWhenUnexpectedCharacterDuringCharacterReference() {
        when(reader.current()).thenReturn('&');
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.error(TokeniserState.StartTagBegin);
        verify(errors).add(eq(new ParseError(anyInt(), "Invalid character reference: missing semicolon")));
    }

    @Test
    public void testErrorWhenInvalidNamedEntity() {
        when(reader.consumeLetterThenDigitSequence()).thenReturn("ABCDEF");
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.error(TokeniserState.StartTagBegin);
        verify(errors).add(eq(new ParseError(anyInt(), "Invalid named reference '%ABCDEF'")));
    }
}