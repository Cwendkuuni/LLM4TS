import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

    private TokenStream tokenStream;

    @Before
    public void setUp() {
        tokenStream = new TokenStream();
    }

    @Test
    public void testIsKeyword_withValidKeyword_shouldReturnTrue() {
        boolean result = tokenStream.isKeyword("if");
        assertTrue(result);
    }

    @Test
    public void testIsKeyword_withInvalidKeyword_shouldReturnFalse() {
        boolean result = tokenStream.isKeyword("myKeyword");
        assertFalse(result);
    }

    @Test
    public void testIsKeyword_withEmptyString_shouldReturnFalse() {
        boolean result = tokenStream.isKeyword("");
        assertFalse(result);
    }

    @Test
    public void testIsJSIdentifier_withValidIdentifier_shouldReturnTrue() {
        boolean result = tokenStream.isJSIdentifier("myIdentifier");
        assertTrue(result);
    }

    @Test
    public void testIsJSIdentifier_withInvalidIdentifier_shouldReturnFalse() {
        boolean result = tokenStream.isJSIdentifier("123identifier");
        assertFalse(result);
    }

    @Test
    public void testIsJSIdentifier_withEmptyString_shouldReturnFalse() {
        boolean result = tokenStream.isJSIdentifier("");
        assertFalse(result);
    }

}