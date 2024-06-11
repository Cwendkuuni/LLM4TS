package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.publicIdentifier.append("testPublicId");
        doctype.systemIdentifier.append("testSystemId");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.publicIdentifier.append("testPublicId");
        doctype.systemIdentifier.append("testSystemId");
        doctype.forceQuirks = true;
        
        assertEquals("testName", doctype.getName());
        assertEquals("testPublicId", doctype.getPublicIdentifier());
        assertEquals("testSystemId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    // Add more test cases for other methods as needed
    
}