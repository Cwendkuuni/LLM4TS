package com.google.javascript.rhino;
import static com.google.javascript.rhino.TokenStream.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 String[] keywords = {"if", "else", "else if", "while", "for", "switch", "case", "default", "try", "catch", "throw", "return", "break", "continue", "new", "class", "public", "private", "protected", "static", "final", "const", "abstract", "interface", "implements", "extends", "package", "import", "this", "super", "if (x) {", "else if (y) {", "else {", "while (z) {", "for (int i = 0; i < 10; i++) {", "switch (char c) {", "case 'a':", "case 'b':", "default:", "try {", "catch (Exception e) {", "throw new Exception();", "return;", "break;", "continue;", "new Object();", "class MyClass {", "public static void main(String[] args) {", "private int x = 10;", "static final int y = 20;", "interface MyInterface {", "abstract class MyAbstractClass {", "if (x > 5) {", "else if (y < 10) {", "else {", "while (true) {", "for (int i = 0; i < 100; i++) {", "switch ('a') {", "case 'a':", "case 'b':", "default:", "try {", "int x = 5;", "throw new Exception();", "}", "return;", "}", "}", "new MyClass() {", "public static void main(String[] args) {", "}", "}", "}", "package com.example;", "import java.util.ArrayList;", "import java.util.List;"};

 for (String keyword : keywords) {
 assertTrue(isKeyword(keyword));
 }
 }

 @Test
 public void testIsJSIdentifier() {
 String[] identifiers = {"identifier1", "_identifier2", "identifier3__", "identifier4_", "identifier5__identifier6", "identifier7_1", "identifier8_2", "identifier9_3", "identifier10_4", "identifier11_identifier12"};

 for (String identifier : identifiers) {
 assertTrue(isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsKeyword_Invalid() {
 String[] invalidKeywords = {"ifx", "elseIf", "while1", "for_", "switchCase", "defaultAnd", "tryCatch", "throwException", "returnValue", "breakContinue", "newObject", "classMy", "publicPrivate", "protectedStatic", "finalConst", "abstractInterface", "implementExtends", "packageImport", "thisSuper", "ifX", "elseIfX", "while1X", "for_X", "switchCaseX", "defaultAndX", "tryCatchX", "throwExceptionX", "returnValueX", "breakContinueX", "newObjectX", "classMyX", "publicPrivateX", "protectedStaticX", "finalConstX", "abstractInterfaceX", "implementExtendsX", "packageImportX", "thisSuperX", "superThis", "returnReturn", "breakBreak", "continueContinue", "newNew", "classClass", "publicPublic", "privatePrivate", "protectedProtected", "staticStatic", "finalFinal", "constConst", "abstractAbstract", "interfaceInterface", "implementsImplements", "extendsExtends", "packagePackage", "importImport", "thisThis", "superSuper"};

 for (String invalidKeyword : invalidKeywords) {
 assertFalse(isKeyword(invalidKeyword));
 }
 }

 @Test
 public void testIsJSIdentifier_Invalid() {
 String[] invalidIdentifiers = {"identifier1!", "identifier2@", "identifier3#", "identifier4$", "identifier5%", "identifier6^", "identifier7&", "identifier8|", "identifier9~", "identifier10`", "identifier11(", "identifier12)", "identifier13{", "identifier14}", "identifier15[", "identifier16]", "identifier17\\", "identifier18/", "identifier19:", "identifier20;", "identifier21,", "identifier22.", "identifier23<", "identifier24>", "identifier25=", "identifier26!="};

 for (String invalidIdentifier : invalidIdentifiers) {
 assertFalse(isJSIdentifier(invalidIdentifier));
 }
 }
}