package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test error message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test error message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError("Test error message", TokenMgrError.LEXICAL_ERROR);
        assertNotNull(error);
    }
    
}
