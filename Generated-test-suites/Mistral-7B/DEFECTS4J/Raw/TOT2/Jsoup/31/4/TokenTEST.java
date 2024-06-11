import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctype_Valid() {
        Token doctype = new Token.Doctype();
        doctype.getName().equals("doctype");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        assertFalse(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_EmptyName() {
        Token doctype = new Token.Doctype();
        doctype.newName(); // should throw AssertionError since name is initially empty
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_NullName() {
        Token doctype = new Token.Doctype();
        doctype.newName("null"); // should throw AssertionError since name cannot be null
    }
}