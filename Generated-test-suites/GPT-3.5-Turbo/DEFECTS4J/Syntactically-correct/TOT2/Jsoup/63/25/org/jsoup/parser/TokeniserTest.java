import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = mock(Tokeniser.class);
    }
    
    @Test
    public void testRead() {
        when(tokeniser.read()).thenReturn(new Token.Character().data("test"));
        
        Token token = tokeniser.read();
        
        verify(tokeniser, times(1)).read();
        assertEquals("test", token.toString());
    }
    
    @Test
    public void testEmit() {
        Token token = new Token.StartTag().tagName("test").selfClosing(false);
        tokeniser.emit(token);
        
        verify(tokeniser, times(1)).emit(token);
    }
    
    @Test
    public void testConsumeCharacterReference() {
        when(tokeniser.consumeCharacterReference(null, false)).thenReturn(new int[]{65});
        
        int[] codepoint = tokeniser.consumeCharacterReference(null, false);
        
        verify(tokeniser, times(1)).consumeCharacterReference(null, false);
        assertEquals(65, codepoint[0]);
    }
    
    @Test
    public void testUnescapeEntities() {
        when(tokeniser.unescapeEntities(false)).thenReturn("test&amp;test");
        
        String unescapedString = tokeniser.unescapeEntities(false);
        
        verify(tokeniser, times(1)).unescapeEntities(false);
        assertEquals("test&test", unescapedString);
    }
    
    // Include more test cases for other public methods
    
}