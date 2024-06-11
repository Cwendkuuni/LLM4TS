import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        // Test false cases
        assertEquals(false, TokenStream.isKeyword("abc"));
        assertEquals(false, TokenStream.isKeyword("def"));
        assertEquals(false, TokenStream.isKeyword("123"));
        assertEquals(false, TokenStream.isKeyword("_abc"));

        // Test true cases (keywords)
        assertEquals(true, TokenStream.isKeyword("if"));
        assertEquals(true, TokenStream.isKeyword("else"));
        assertEquals(true, TokenStream.isKeyword("while"));
        assertEquals(true, TokenStream.isKeyword("for"));
        assertEquals(true, TokenStream.isKeyword("switch"));
        assertEquals(true, TokenStream.isKeyword("case"));
        assertEquals(true, TokenStream.isKeyword("try"));
        assertEquals(true, TokenStream.isKeyword("catch"));
        assertEquals(true, TokenStream.isKeyword("throw"));
        assertEquals(true, TokenStream.isKeyword("return"));
        assertEquals(true, TokenStream.isKeyword("break"));
        assertEquals(true, TokenStream.isKeyword("continue"));
        assertEquals(true, TokenStream.isKeyword("class"));
        assertEquals(true, TokenStream.isKeyword("package"));
        assertEquals(true, TokenStream.isKeyword("import"));
        assertEquals(true, TokenStream.isKeyword("interface"));
        assertEquals(true, TokenStream.isKeyword("extends"));
        assertEquals(true, TokenStream.isKeyword("implements"));
        assertEquals(true, TokenStream.isKeyword("static"));
        assertEquals(true, TokenStream.isKeyword("public"));
        assertEquals(true, TokenStream.isKeyword("private"));
        assertEquals(true, TokenStream.isKeyword("protected"));
        assertEquals(true, TokenStream.isKeyword("abstract"));
        assertEquals(true, TokenStream.isKeyword("native"));
        assertEquals(true, TokenStream.isKeyword("const"));
        assertEquals(true, TokenStream.isKeyword("final"));
        assertEquals(true, TokenStream.isKeyword("double"));
        assertEquals(true, TokenStream.isKeyword("float"));
        assertEquals(true, TokenStream.isKeyword("short"));
        assertEquals(true, TokenStream.isKeyword("long"));
        assertEquals(true, TokenStream.isKeyword("byte"));
        assertEquals(true, TokenStream.isKeyword("boolean"));
        assertEquals(true, TokenStream.isKeyword("null"));
        assertEquals(true, TokenStream.isKeyword("void"));
        assertEquals(true, TokenStream.isKeyword("with"));
        assertEquals(true, TokenStream.isKeyword("new"));
        assertEquals(true, TokenStream.isKeyword("let"));
        assertEquals(true, TokenStream.isKeyword("const"));
        assertEquals(true, TokenStream.isKeyword("await"));
        assertEquals(true, TokenStream.isKeyword("yield"));
        assertEquals(true, TokenStream.isKeyword("import"));
        assertEquals(true, TokenStream.isKeyword("export"));
        assertEquals(true, TokenStream.isKeyword("from"));
        assertEquals(true, TokenStream.isKeyword("as"));
        assertEquals(true, TokenStream.isKeyword("default"));
        assertEquals(true, TokenStream.isKeyword("true"));
        assertEquals(true, TokenStream.isKeyword("false"));
        assertEquals(true, TokenStream.isKeyword("null"));
    }
}