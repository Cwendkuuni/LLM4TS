import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword_positiveCase() {
        assertTrue(TokenStream.isKeyword("if"));
    }
    
    @Test
    public void testIsKeyword_negativeCase() {
        assertFalse(TokenStream.isKeyword("abc"));
    }
    
    @Test
    public void testIsKeyword_edgeCase() {
        assertFalse(TokenStream.isKeyword(""));
    }
    
    @Test
    public void testIsJSIdentifier_positiveCase() {
        assertTrue(TokenStream.isJSIdentifier("variable1"));
    }
    
    @Test
    public void testIsJSIdentifier_negativeCase() {
        assertFalse(TokenStream.isJSIdentifier("123abc"));
    }
    
    @Test
    public void testIsJSIdentifier_edgeCase() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}