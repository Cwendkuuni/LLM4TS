package com.google.javascript.rhino;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

 @Test
 public void testIsKeyword_SimpleIdentifiers() {
 for (String keyword : new String[]{"if", "for", "new", "return", "var", "this", "else", "void", "int", "class", "false", "double", "boolean", "package", "abstract", "interface", "public", "protected", "implements", "static", "transient", "synchronized"}) {
 assertTrue(TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_InvalidIdentifiers() {
 for (String identifier : new String[]{"!false", "1if", "if1", "if_else", "if else", "if Else", "if (true)", "if true", "if(true)", "if true(", "if ( true )", "if true )", "if true )", "if ( true ) ", "if ( true ) ", "if ( true ) ", "if ( true ) ", "if ( true ) ", "if ( true )", "if ( true )"}) {
 assertFalse(TokenStream.isKeyword(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_ValidIdentifiers() {
 for (String identifier : new String[]{"identifier1", "Identifier2", "_identifier3", "_Identifier4", "_Identifier5_6", "_Identifier_7"}) {
 assertTrue(TokenStream.isJSIdentifier(identifier));
 }
 }

 @Test
 public void testIsJSIdentifier_InvalidIdentifiers() {
 for (String identifier : new String[]{"1identifier", "identifier1_", "identifier_1", "identifier-1", "identifier_if", "identifier if", "identifier if_else", "identifier if Else", "identifier if ( true )", "identifier if ( true ) ", "identifier if ( true ) ", "identifier if ( true ) ", "identifier if ( true ) ", "identifier if ( true ) ", "identifier if ( true )"}) {
 assertFalse(TokenStream.isJSIdentifier(identifier));
 }
 }
}