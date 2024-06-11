import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList parseErrorListMock;

    @Before
    public void setUp() {
        parseErrorListMock = mock(ParseErrorList.class);
        tokeniser = new Tokeniser(new CharacterReader("test"), parseErrorListMock);
    }

    @Test
    public void testRead() {
        Token expectedToken = new Token.Character("test");

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");

        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        Token expectedToken = new Token.Character(str);

        tokeniser.emit(str);

        assertEquals(expectedToken, tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        Token expectedToken = new Token.Character(String.valueOf(c));

        tokeniser.emit(c);

        assertEquals(expectedToken, tokeniser.read());
    }

    @Test
    public void testState() {
        TokeniserState expectedState = TokeniserState.TagName;
        tokeniser.transition(expectedState);

        TokeniserState actualState = tokeniser.getState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState expectedState = TokeniserState.CommentData;
        tokeniser.advanceTransition(expectedState);

        TokeniserState actualState = tokeniser.getState();

        assertEquals(expectedState, actualState);
        verify(tokeniser.reader).advance();
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        boolean acknowledgeFlag = tokeniser.selfClosingFlagAcknowledged;

        assertEquals(true, acknowledgeFlag);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character expectedCharacter = 't';
        when(tokeniser.reader.isEmpty()).thenReturn(false);
        when(tokeniser.reader.current()).thenReturn('t');

        Character actualCharacter = tokeniser.consumeCharacterReference(null, false);

        assertEquals(expectedCharacter, actualCharacter);
        verify(tokeniser.reader).mark();
    }

    @Test
    public void testConsumeCharacterReferenceWithNullAllowedCharacter() {
        Character expectedCharacter = null;
        when(tokeniser.reader.isEmpty()).thenReturn(true);

        Character actualCharacter = tokeniser.consumeCharacterReference(null, false);

        assertEquals(expectedCharacter, actualCharacter);
    }

    @Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
        Character expectedCharacter = null;
        when(tokeniser.reader.isEmpty()).thenReturn(false);
        when(tokeniser.reader.current()).thenReturn('t');

        Character actualCharacter = tokeniser.consumeCharacterReference('t', false);

        assertEquals(expectedCharacter, actualCharacter);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag expectedTag = new Token.StartTag();

        Token.Tag actualTag = tokeniser.createTagPending(start);

        assertEquals(expectedTag, actualTag);
        assertEquals(tokeniser.tagPending, actualTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag expectedTag = new Token.StartTag();
        tokeniser.createTagPending(true);

        tokeniser.emitTagPending();

        assertEquals(expectedTag, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        Token.Comment expectedComment = new Token.Comment();

        tokeniser.createCommentPending();

        assertEquals(expectedComment, tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment expectedComment = new Token.Comment();
        tokeniser.createCommentPending();

        tokeniser.emitCommentPending();

        assertEquals(expectedComment, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        Token.Doctype expectedDoctype = new Token.Doctype();

        tokeniser.createDoctypePending();

        assertEquals(expectedDoctype, tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype expectedDoctype = new TokenType.Doctype();
        tokeniser.createDoctypePending();

        tokeniser.emitDoctypePending();

        assertEquals(expectedDoctype, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        StringBuilder expectedBuffer = new StringBuilder();

        tokeniser.createTempBuffer();

        assertEquals(expectedBuffer, tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.StartTag();

        boolean isAppropriateEndTag = tokeniser.isAppropriateEndTagToken();

        assertEquals(true, isAppropriateEndTag);
    }

    @Test
    public void testAppropriateEndTagName() {
        String expectedTagName = "tag";
        tokeniser.lastStartTag = new DataType.StartTag();
        tokeniser.lastStartTag.tagName = expectedTagName;

        String actualTagName = tokeniser.appropriateEndTagName();

        assertEquals(expectedTagName, actualTagName);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        String errorMsg = "Unexpected character 't' in input state [Data]";

        tokeniser.error(state);

        verify(parseErrorListMock).add(any(ParseError.class));
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        String errorMsg = "Unexpectedly reached end of file (EOF) in input state [Data]";

        tokeniser.eofError(state);

        verify(parseErrorListMock).add(any(ParseError.class));
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: numeric reference with no numerals";
        String errorMsg = "Invalid character reference: " + message;

        tokeniser.characterReferenceError(message);

        verify(parseErrorListMock).add(any(ParseError.class));
    }

    @Test
    public void testErrorWithCustomMessage() {
        String errorMsg = "Custom error message";

        tokeniser.error(errorMsg);

        verify(parseErrorListMock).add(any(ParseError.class));
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertEquals(true, currentNodeInHtmlNS);
    }
}