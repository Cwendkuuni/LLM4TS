import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctype_basic() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("_public");
        assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        doctype.systemIdentifier("_system");
        assertEquals("_system", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.forceQuirks(true);
        assertEquals(true, doctype.asDoctype().isForceQuirks());
    }
}