import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        String expectedName = "doctype";
        assertEquals(expectedName, doctypeToken.tokenType());
    }

    // Edge case: Test for Doctype token with name, publicIdentifier, systemIdentifier and forceQuirks set
    @Test
    public void testDoctypeTokenWithAttributes() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName().append("html");
        doctypeToken.getPublicIdentifier().append("publicId");
        doctypeToken.getSystemIdentifier().append("systemId");
        doctypeToken.isForceQuirks(true);

        String expectedName = "Doctype";
        String expectedPublicIdentifier = "publicId";
        String expectedSystemIdentifier = "systemId";
        boolean expectedForceQuirks = true;

        assertEquals(expectedName, doctypeToken.tokenType());
        assertEquals(expectedPublicIdentifier, doctypeToken.getPublicIdentifier());
        assertEquals(expectedSystemIdentifier, doctypeToken.getSystemIdentifier());
        assertEquals(expectedForceQuirks, doctypeToken.isForceQuirks());
    }

    // Exception handling: Test for null name, publicIdentifier and systemIdentifier in Doctype token
    @Test(expected = NullPointerException.class)
    public void testDoctypeTokenWithNullAttributes() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName() = null;
        doctypeToken.getPublicIdentifier() = null;
        doctypeToken.getSystemIdentifier() = null;
        doctypeToken.isForceQuirks(true);
    }
}