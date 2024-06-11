
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0020", TokenMgrError.addEscapes(" "));
        assertEquals("\\u0021", TokenMgrError.addEscapes("!"));
        assertEquals("\\u0030", TokenMgrError.addEscapes("0"));
        assertEquals("\\u007F", TokenMgrError.addEscapes("\u007F"));
        assertEquals("\\u00A0", TokenMgrError.addEscapes("\u00A0"));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "error";
        char curChar = 'c';
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF>  after: \"error\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));

        EOFSeen = false;
        lexState = 2;
        errorLine = 3;
        errorColumn = 4;
        errorAfter = "error2";
        curChar = 'd';
        expected = "Lexical error at line 3, column 4.  Encountered: \"d\" (100), after: \"error2\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());

        TokenMgrError error2 = new TokenMgrError("", 2);
        assertNotNull(error2.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Test Message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "error";
        char curChar = 'c';
        int reason = 2;
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF>  after: \"error\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);

        EOFSeen = false;
        lexState = 2;
        errorLine = 3;
        errorColumn = 4;
        errorAfter = "error2";
        curChar = 'd';
        expected = "Lexical error at line 3, column 4.  Encountered: \"d\" (100), after: \"error2\"";
        TokenMgrError error2 = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expected, error2.getMessage());
        assertEquals(reason, error2.errorCode);
    }
}
