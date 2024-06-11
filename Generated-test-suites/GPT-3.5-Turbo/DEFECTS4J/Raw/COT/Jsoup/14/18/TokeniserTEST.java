
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokeniserTest {

    @Mock
    private CharacterReader reader;

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokeniser = new Tokeniser(reader);
    }
    
    @Test
    public void testRead() {
        // Implement test for read() method
        // ...
    }

    @Test
    public void testEmit() {
        // Implement test for emit() method
        // ...
    }

    @Test
    public void testAdvanceTransition() {
        // Implement test for advanceTransition() method
        // ...
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testRead_EmptyCharBuffer_ShouldReturnCharacterToken() {
        when(reader.isEmpty()).thenReturn(true);
        when(reader.current()).thenReturn('a');

        Token result = tokeniser.read();

        assertEquals(Token.Character.class, result.getClass());
        assertEquals("a", result.toString());
        verify(reader).advance();
    }

    @Test
    public void testRead_WithCharBuffer_ShouldReturnCharacterToken() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('a');

        tokeniser.emit("b");
        tokeniser.emit("c");

        Token result = tokeniser.read();

        assertEquals(Token.Character.class, result.getClass());
        assertEquals("bc", result.toString());
        verify(reader, never()).advance();
    }

    @Test
    public void testEmit_TagToken_ShouldSetLastStartTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "test";

        tokeniser.emit(tag);

        assertEquals(tag, tokeniser.lastStartTag);
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    // ...

}

