import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeName() {
        Token doctype = new Token.Doctype();
        doctype.getName().equals("Doctype");
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.getPublicIdentifier().equals("");
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.getSystemIdentifier().equals("");
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.isForceQuirks(); // default value is false

        doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }
}