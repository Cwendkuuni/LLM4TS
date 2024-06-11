package com.google.javascript.rhino;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.TokenStream.*;

import org.junit.Test;

public class TokenStreamTest {

 // Test case for isKeyword method
 @Test
 public void testIsKeyword() {
 String[] testCases = {
 "false", "true", "null", "void", "with", "class", "break", "while",
 "if", "else", "for", "function", "return", "throw", "catch", "try",
 "var", "let", "const", "new", "import", "export", "delete", "this",
 "super", "package", "interface", "implements", "extends", "abstract",
 "protected", "private", "transient", "synchronized", "goto", "case",
 "default", "throw", "instanceof", "yield", "await"
 };
 for (String name : testCases) {
 assertTrue(isKeyword(name));
 }

 String[] invalidKeywords = {
 "fals3", "tru3", "null", "void1", "with1", "class1", "break1",
 "while1", "if1", "else1", "for1", "function1", "return1", "throw1",
 "catch1", "try1", "var1", "let1", "const1", "new1", "import1",
 "export1", "delete1", "this1", "super1", "package1", "interface1",
 "implements1", "extends1", "abstract1", "protected1", "private1",
 "transient1", "synchronized1", "goto1", "case1", "default1",
 "throw1", "instanceof1", "yield1", "await1"
 };
 for (String name : invalidKeywords) {
 assertFalse(isKeyword(name));
 }
 }

 // Test case for isJSIdentifier method
 @Test
 public void testIsJSIdentifier() {
 String[] validIdentifiers = {
 "_invalidIdentifier123", "helloWorld", "MyVariableName_123",
 "_myVariable_name", "_invalidIdentifier-123", "myVariableName__123"
 };
 for (String identifier : validIdentifiers) {
 assertTrue(isJSIdentifier(identifier));
 }

 String[] invalidIdentifiers = {
 "123", "hello_world", "MyVariableName123", "myVariableName-_123",
 "_invalidIdentifier123_", "myVariableName__123__"
 };
 for (String identifier : invalidIdentifiers) {
 assertFalse(isJSIdentifier(identifier));
 }
}

}