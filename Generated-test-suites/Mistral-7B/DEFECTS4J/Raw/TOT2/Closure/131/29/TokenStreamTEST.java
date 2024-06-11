import static com.google.javascript.rhino.TokenStream.*;
import static org.junit.Assert.*;

public class TokenStreamTest {
    @Test
    public void testIsKeyword_ValidKeywords() {
        assertTrue(isKeyword("if"));
        assertTrue(isKeyword("else"));
        assertTrue(isKeyword("else if"));
        assertTrue(isKeyword("for"));
        assertTrue(isKeyword("while"));
        assertTrue(isKeyword("new"));
        assertTrue(isKeyword("return"));
        assertTrue(isKeyword("void"));
        assertTrue(isKeyword("int"));
        assertTrue(isKeyword("float"));
        assertTrue(isKeyword("double"));
        assertTrue(isKeyword("boolean"));
        assertTrue(isKeyword("char"));
        assertTrue(isKeyword("byte"));
        assertTrue(isKeyword("short"));
        assertTrue(isKeyword("long"));
        assertTrue(isKeyword("void"));
        assertTrue(isKeyword("null"));
        assertTrue(isKeyword("class"));
        assertTrue(isKeyword("package"));
        assertTrue(isKeyword("abstract"));
        assertTrue(isKeyword("interface"));
        assertTrue(isKeyword("extends"));
        assertTrue(isKeyword("implements"));
        assertTrue(isKeyword("import"));
        assertTrue(isKeyword("public"));
        assertTrue(isKeyword("protected"));
        assertTrue(isKeyword("static"));
        assertTrue(isKeyword("final"));
        assertTrue(isKeyword("native"));
        assertTrue(isKeyword("const"));
        assertTrue(isKeyword("transient"));
        assertTrue(isKeyword("synchronized"));
    }

    @Test
    public void testIsKeyword_InvalidStrings() {
        assertFalse(isKeyword(""));
        assertFalse(isKeyword("123"));
        assertFalse(isKeyword("abc"));
        assertFalse(isKeyword("_abc"));
        assertFalse(isKeyword("abc_"));
        assertFalse(isKeyword("1a2b"));
        assertFalse(isKeyword("ab_c"));
        assertFalse(isKeyword("__ab"));
        assertFalse(isKeyword("ab__"));
        assertFalse(isKeyword("__a_b"));
        assertFalse(isKeyword("ab____"));
        assertFalse(isKeyword("___a_b"));
        assertFalse(isKeyword("____a_b"));
    }

    @Test(expected = NullPointerException.class)
    public void testIsKeyword_NullInput() {
        isKeyword(null);
    }
}