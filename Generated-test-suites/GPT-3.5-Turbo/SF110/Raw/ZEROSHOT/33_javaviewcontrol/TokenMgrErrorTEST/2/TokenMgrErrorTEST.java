package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

  @Test
  public void testAddEscapes() {
    String input = "abc\ndef\tghi\\jkl\"";
    String expectedOutput = "abc\\ndef\\tghi\\\\jkl\\\"";
    String output = TokenMgrError.addEscapes(input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testLexicalError() {
    boolean EOFSeen = false;
    int lexState = 0;
    int errorLine = 1;
    int errorColumn = 2;
    String errorAfter = "xyz";
    char curChar = 'a';
    String expectedOutput = "Lexical error at line 1, column 2. Encountered: \"a\" (97), after : \"xyz\"";
    String output = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testGetMessage() {
    TokenMgrError error = new TokenMgrError("Test Message", 0);
    String expectedOutput = "Test Message";
    String output = error.getMessage();
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testConstructor1() {
    TokenMgrError error = new TokenMgrError();
    assertNull(error.getMessage());
  }

  @Test
  public void testConstructor2() {
    String message = "Test Message";
    int reason = 1;
    TokenMgrError error = new TokenMgrError(message, reason);
    assertEquals(message, error.getMessage());
    assertEquals(reason, error.errorCode);
  }

  @Test
  public void testConstructor3() {
    boolean EOFSeen = true;
    int lexState = 2;
    int errorLine = 3;
    int errorColumn = 4;
    String errorAfter = "abc";
    char curChar = 'b';
    int reason = 3;
    String expectedMessage = "Lexical error at line 3, column 4. Encountered: \"<EOF> \" after : \"abc\"";
    TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
    assertEquals(expectedMessage, error.getMessage());
    assertEquals(reason, error.errorCode);
  }

}