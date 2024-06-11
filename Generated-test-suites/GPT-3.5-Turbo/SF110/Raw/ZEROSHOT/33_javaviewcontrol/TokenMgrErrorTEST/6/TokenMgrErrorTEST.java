import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

  @Test
  public void testAddEscapes() {
    // Test case 1
    String str1 = "";
    String result1 = TokenMgrError.addEscapes(str1);
    assertEquals("", result1);

    // Test case 2
    String str2 = "Hello";
    String result2 = TokenMgrError.addEscapes(str2);
    assertEquals("Hello", result2);

    // Test case 3
    String str3 = "Hello\tworld";
    String result3 = TokenMgrError.addEscapes(str3);
    assertEquals("Hello\\tworld", result3);

    // Test case 4
    String str4 = "Hello\nworld";
    String result4 = TokenMgrError.addEscapes(str4);
    assertEquals("Hello\\nworld", result4);

    // Test case 5
    String str5 = "Hello\nworld";
    String result5 = TokenMgrError.addEscapes(str5);
    assertEquals("Hello\\nworld", result5);

    // Test case 6
    String str6 = "Hello\"world";
    String result6 = TokenMgrError.addEscapes(str6);
    assertEquals("Hello\\\"world", result6);

    // Test case 7
    String str7 = "Hello'world";
    String result7 = TokenMgrError.addEscapes(str7);
    assertEquals("Hello\\'world", result7);

    // Test case 8
    String str8 = "Hello\\world";
    String result8 = TokenMgrError.addEscapes(str8);
    assertEquals("Hello\\\\world", result8);

    // Test case 9
    String str9 = "\u0020";
    String result9 = TokenMgrError.addEscapes(str9);
    assertEquals("\\u0020", result9);
  }

  @Test
  public void testLexicalError() {
    // Test case 1
    boolean EOFSeen1 = false;
    int lexState1 = 0;
    int errorLine1 = 1;
    int errorColumn1 = 1;
    String errorAfter1 = "";
    char curChar1 = 'a';
    String result1 = TokenMgrError.LexicalError(EOFSeen1, lexState1, errorLine1, errorColumn1, errorAfter1, curChar1);
    assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"", result1);

    // Test case 2
    boolean EOFSeen2 = true;
    int lexState2 = 1;
    int errorLine2 = 3;
    int errorColumn2 = 5;
    String errorAfter2 = "abc";
    char curChar2 = '\n';
    String result2 = TokenMgrError.LexicalError(EOFSeen2, lexState2, errorLine2, errorColumn2, errorAfter2, curChar2);
    assertEquals("Lexical error at line 3, column 5.  Encountered: <EOF>  after : \"abc\"", result2);
  }

  @Test
  public void testGetMessage() {
    TokenMgrError error = new TokenMgrError("Error message", 1);
    String result = error.getMessage();
    assertEquals("Error message", result);
  }

  @Test
  public void testConstructor1() {
    TokenMgrError error = new TokenMgrError();
    int errorCode = error.errorCode;
    assertEquals(0, errorCode);
  }

  @Test
  public void testConstructor2() {
    TokenMgrError error = new TokenMgrError("Error message", 2);
    String result = error.getMessage();
    int errorCode = error.errorCode;
    assertEquals("Error message", result);
    assertEquals(2, errorCode);
  }

  @Test
  public void testConstructor3() {
    boolean EOFSeen3 = true;
    int lexState3 = 2;
    int errorLine3 = 4;
    int errorColumn3 = 6;
    String errorAfter3 = "def";
    char curChar3 = '\r';
    int reason = 3;
    TokenMgrError error = new TokenMgrError(EOFSeen3, lexState3, errorLine3, errorColumn3, errorAfter3, curChar3, reason);
    String result = error.getMessage();
    assertEquals("Lexical error at line 4, column 6.  Encountered: <EOF>  after : \"def\"", result);
    assertEquals(3, error.errorCode);
  }
}