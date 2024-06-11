import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Test valid keywords
 assertTrue(isKeyword("false"));
 assertTrue(isKeyword("null"));
 assertTrue(isKeyword("break"));
 assertTrue(isKeyword("case"));
 assertTrue(isKeyword("catch"));
 assertTrue(isKeyword("class"));
 assertTrue(isKeyword("const"));
 assertTrue(isKeyword("continue"));
 assertTrue(isKeyword("debugger"));
 assertTrue(isKeyword("default"));
 assertTrue(isKeyword("delete"));
 assertTrue(isKeyword("do"));
 assertTrue(isKeyword("else"));
 assertTrue(isKeyword("export"));
 assertTrue(isKeyword("extends"));
 assertTrue(isKeyword("false"));
 assertTrue(isKeyword("finally"));
 assertTrue(isKeyword("float"));
 assertTrue(isKeyword("for"));
 assertTrue(isKeyword("function"));
 assertTrue(isKeyword("goto"));
 assertTrue(isKeyword("if"));
 assertTrue(isKeyword("import"));
 assertTrue(isKeyword("in"));
 assertTrue(isKeyword("instanceof"));
 assertTrue(isKeyword("int"));
 assertTrue(isKeyword("new"));
 assertTrue(isKeyword("null"));
 assertTrue(isKeyword("package"));
 assertTrue(isKeyword("private"));
 assertTrue(isKeyword("protected"));
 assertTrue(isKeyword("return"));
 assertTrue(isKeyword("short"));
 assertTrue(isKeyword("static"));
 assertTrue(isKeyword("super"));
 assertTrue(isKeyword("switch"));
 assertTrue(isKeyword("synchronized"));
 assertTrue(isKeyword("this"));
 assertTrue(isKeyword("throw"));
 assertTrue(isKeyword("transient"));
 assertTrue(isKeyword("try"));
 assertTrue(isKeyword("typeof"));
 assertTrue(isKeyword("true"));
 assertTrue(isKeyword("var"));
 assertTrue(isKeyword("void"));
 assertTrue(isKeyword("volatile"));
 assertTrue(isKeyword("while"));
 // Test invalid keywords
 assertFalse(isKeyword(""));
 assertFalse(isKeyword("abcd"));
 assertFalse(isKeyword("1234"));
 assertFalse(isKeyword("myVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Test valid JS identifiers
 assertTrue(isJSIdentifier("myVariable"));
 assertTrue(isJSIdentifier("_my_variable"));
 assertTrue(isJSIdentifier("my_variable_123"));
 // Test invalid JS identifiers
 assertFalse(isJSIdentifier("123"));
 assertFalse(isJSIdentifier("myVariable="));
 assertFalse(isJSIdentifier("myVariable!"));
 assertFalse(isJSIdentifier("myVariable?"));
 }
}