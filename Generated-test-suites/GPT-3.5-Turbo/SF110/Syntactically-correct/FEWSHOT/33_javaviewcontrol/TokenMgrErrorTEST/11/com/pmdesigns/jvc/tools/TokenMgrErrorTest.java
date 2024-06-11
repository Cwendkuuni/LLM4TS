package com.pmdesigns.jvc.tools;

import static org.junit.Assert.*;
import org.junit.Test;

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
		assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
	}

	@Test
	public void testLexicalError() {
		assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"b\"", TokenMgrError.LexicalError(false, 0, 1, 2, "b", 'a'));
		assertEquals("Lexical error at line 2, column 3.  Encountered: \"b\" (98), after : \"c\"", TokenMgrError.LexicalError(false, 0, 2, 3, "c", 'b'));
		assertEquals("Lexical error at line 3, column 4.  Encountered: \"c\" (99), after : \"d\"", TokenMgrError.LexicalError(false, 0, 3, 4, "d", 'c'));
	}

	@Test
	public void testGetMessage() {
		TokenMgrError error = new TokenMgrError("Error message", 1);
		assertEquals("Error message", error.getMessage());
	}

	@Test
	public void testConstructor() {
		TokenMgrError error = new TokenMgrError();
		assertNotNull(error);

		error = new TokenMgrError("Error message", 1);
		assertEquals("Error message", error.getMessage());
		assertEquals(1, error.errorCode);

		error = new TokenMgrError(false, 0, 1, 2, "b", 'a', 0);
		assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"b\"", error.getMessage());
		assertEquals(0, error.errorCode);
	}

}