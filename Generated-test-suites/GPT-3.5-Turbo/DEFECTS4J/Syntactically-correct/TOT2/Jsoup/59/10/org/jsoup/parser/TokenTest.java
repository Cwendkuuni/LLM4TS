import org.junit.Test;

public class TokenTest {
    
    // Test tokenType() method
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        String tokenType = token.tokenType();
        assertEquals("Doctype", tokenType);
    }
    
    // Test reset() method of Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.pubSysKey = "TestKey";
        doctype.publicIdentifier.append("TestPublic");
        doctype.systemIdentifier.append("TestSystem");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    // Add more test cases here for other methods
    
}