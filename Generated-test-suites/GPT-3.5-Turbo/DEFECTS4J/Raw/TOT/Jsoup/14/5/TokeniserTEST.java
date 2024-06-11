import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_shouldReturnCharacterToken_whenCharBufferIsNotEmpty() {
        // Setup
        when(reader.current()).thenReturn('A');
        when(reader.isEmpty()).thenReturn(false);
        when(reader.matchesAny(any(Character.class))).thenReturn(false);
        StringBuilder charBuffer = new StringBuilder();
        charBuffer.append("Hello");
        tokeniser.charBuffer = charBuffer;

        // Execute
        Token token = tokeniser.read();

        // Verify
        assertThat(token).isInstanceOf(Token.Character.class);
        StringBuilder expectedCharBuffer = new StringBuilder();
        assertThat(tokeniser.charBuffer.toString()).isEqualTo(expectedCharBuffer.toString());
    }

    @Test
    public void testRead_shouldReturnEmitPendingToken_whenCharBufferIsEmpty() {
        // Setup
        Token expectedToken = new Token();
        tokeniser.isEmitPending = true;
        tokeniser.emitPending = expectedToken;
        when(reader.current()).thenReturn('A');
        when(reader.isEmpty()).thenReturn(false);
        when(reader.matchesAny(any(Character.class))).thenReturn(false);
        tokeniser.charBuffer = new StringBuilder();

        // Execute
        Token token = tokeniser.read();

        // Verify
        assertThat(token).isEqualTo(expectedToken);
        assertThat(tokeniser.charBuffer.length()).isEqualTo(0);
        assertThat(tokeniser.isEmitPending).isFalse();
    }

    @Test
    public void testEmit_shouldSetEmitPendingToken_andIsEmitPendingFlag() {
        // Setup
        Token expectedToken = new Token();

        // Execute
        tokeniser.emit(expectedToken);

        // Verify
        assertThat(tokeniser.emitPending).isEqualTo(expectedToken);
        assertThat(tokeniser.isEmitPending).isTrue();
    }

    @Test
    public void testEmit_shouldSetEmitPendingToken_whenStartTagTokenIsEmitted() {
        // Setup
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        // Execute
        tokeniser.emit(startTag);

        // Verify
        assertThat(tokeniser.emitPending).isEqualTo(startTag);
        assertThat(tokeniser.isEmitPending).isTrue();
        assertThat(tokeniser.selfClosingFlagAcknowledged).isFalse();
    }

    @Test
    public void testEmit_shouldNotSetEmitPendingTokenAndIsEmitPendingFlag_whenEndTagTokenHasAttributes() {
        // Setup
        Token.EndTag endTag = new Token.EndTag();
        endTag.attributes.add("id");

        // Execute
        tokeniser.emit(endTag);

        // Verify
        assertThat(tokeniser.emitPending).isNull();
        assertThat(tokeniser.isEmitPending).isFalse();
    }

    @Test
    public void testAdvanceTransition_shouldAdvanceReaderAndSetState() {
        // Setup
        TokeniserState expectedState = TokeniserState.Comment;
        when(reader.pos()).thenReturn(5);

        // Execute
        tokeniser.advanceTransition(expectedState);

        // Verify
        verify(reader, times(1)).advance();
        assertThat(tokeniser.getState()).isEqualTo(expectedState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_shouldSetSelfClosingFlagAcknowledged() {
        // Execute
        tokeniser.acknowledgeSelfClosingFlag();

        // Verify
        assertThat(tokeniser.selfClosingFlagAcknowledged).isTrue();
    }
}