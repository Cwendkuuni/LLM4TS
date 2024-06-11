import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType_EmptyString() {
        Token token = new Token();
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "Test Key";
        doctype.publicIdentifier.append("Test Pub ID");
        doctype.systemIdentifier.append("Test Sys ID");
        doctype.forceQuirks = false;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());
    }

    // Add additional test cases for other methods in Token class

}