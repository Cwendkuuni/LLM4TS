import static org.junit.Assert.*;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = {
 "abstract", "arguments", "await", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger", "default", "delete", "do", "double", "else", "enum", "eval", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throw", "transient", "try", "typeof", "typeof", "var", "void", "volatile", "while", "with", "yield"
 };

 for (String keyword : keywords) {
 boolean expected = isKeyword(keyword);
 boolean actual = isKeyword(keyword);
 assertEquals("Expected isKeyword(\"" + keyword + "\") to return " + expected, expected, actual);
 }

 String nonKeyword = "myVariable";
 boolean expectedNonKeyword = isJSIdentifier(nonKeyword);
 boolean actualNonKeyword = isKeyword(nonKeyword);
 assertFalse("Expected isKeyword(\"" + nonKeyword + "\") to return false", actualNonKeyword);
 assertNotNull("Expected actualNonKeyword to not be null", actualNonKeyword);
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 String name = null;
 boolean result = isKeyword(name);
 fail("Expected isKeyword(null) to throw NullPointerException");
 }

 @Test
 public void testEmptyName() {
 boolean expected = false;
 boolean actual = isKeyword("");
 assertEquals("Expected isKeyword(\"\") to return " + expected, expected, actual);
 }

 @Test
 public void testOneCharacterName() {
 String name = "a";
 boolean expected = false;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"a\") to return " + expected, expected, actual);
 }

 @Test
 public void testTwoCharacterName() {
 String name = "if";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"if\") to return " + expected, expected, actual);
 }

 @Test
 public void testThreeCharacterName() {
 String name = "else";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"else\") to return " + expected, expected, actual);
 }

 @Test
 public void testFourCharacterName() {
 String name = "false";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"false\") to return " + expected, expected, actual);
 }

 @Test
 public void testFiveCharacterName() {
 String name = "break";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"break\") to return " + expected, expected, actual);
 }

 @Test
 public void testSixCharacterName() {
 String name = "while";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"while\") to return " + expected, expected, actual);
 }

 @Test
 public void testSevenCharacterName() {
 String name = "switch";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"switch\") to return " + expected, expected, actual);
 }

 @Test
 public void testEightCharacterName() {
 String name = "public";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"public\") to return " + expected, expected, actual);
 }

 @Test
 public void testNineCharacterName() {
 String name = "protected";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"protected\") to return " + expected, expected, actual);
 }

 @Test
 public void testTenCharacterName() {
 String name = "instanceof";
 boolean expected = true;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"instanceof\") to return " + expected, expected, actual);
 }

 @Test
 public void testLongerName() {
 String name = "myVeryLongVariableName";
 boolean expected = false;
 boolean actual = isKeyword(name);
 assertEquals("Expected isKeyword(\"myVeryLongVariableName\") to return " + expected, expected, actual);
 }
}