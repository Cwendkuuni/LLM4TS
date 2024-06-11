package com.google.javascript.rhino;
import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {
    
    @Test
    public void testIsKeyword() {
        // Test case 1: "if"
        Assert.assertTrue(TokenStream.isKeyword("if"));
        
        // Test case 2: "else"
        Assert.assertTrue(TokenStream.isKeyword("else"));
        
        // Test case 3: "for"
        Assert.assertTrue(TokenStream.isKeyword("for"));
        
        // Test case 4: "while"
        Assert.assertTrue(TokenStream.isKeyword("while"));
        
        // Test case 5: "switch"
        Assert.assertTrue(TokenStream.isKeyword("switch"));
        
        // Test case 6: "case"
        Assert.assertTrue(TokenStream.isKeyword("case"));
        
        // Test case 7: "default"
        Assert.assertTrue(TokenStream.isKeyword("default"));
        
        // Test case 8: "break"
        Assert.assertTrue(TokenStream.isKeyword("break"));
        
        // Test case 9: "continue"
        Assert.assertTrue(TokenStream.isKeyword("continue"));
        
        // Test case 10: "return"
        Assert.assertTrue(TokenStream.isKeyword("return"));
        
        // Test case 11: "try"
        Assert.assertTrue(TokenStream.isKeyword("try"));
        
        // Test case 12: "catch"
        Assert.assertTrue(TokenStream.isKeyword("catch"));
        
        // Test case 13: "finally"
        Assert.assertTrue(TokenStream.isKeyword("finally"));
        
        // Test case 14: "throw"
        Assert.assertTrue(TokenStream.isKeyword("throw"));
        
        // Test case 15: "true"
        Assert.assertTrue(TokenStream.isKeyword("true"));
        
        // Test case 16: "false"
        Assert.assertTrue(TokenStream.isKeyword("false"));
        
        // Test case 17: "null"
        Assert.assertTrue(TokenStream.isKeyword("null"));
        
        // Test case 18: "void"
        Assert.assertTrue(TokenStream.isKeyword("void"));
        
        // Test case 19: "class"
        Assert.assertTrue(TokenStream.isKeyword("class"));
        
        // Test case 20: "interface"
        Assert.assertTrue(TokenStream.isKeyword("interface"));
    }
    
    @Test
    public void testIsJSIdentifier() {
        // Test case 1: Valid JS identifier
        Assert.assertTrue(TokenStream.isJSIdentifier("example1"));
        
        // Test case 2: Valid JS identifier with digits
        Assert.assertTrue(TokenStream.isJSIdentifier("example123"));
        
        // Test case 3: Valid JS identifier with underscore
        Assert.assertTrue(TokenStream.isJSIdentifier("example_1"));
        
        // Test case 4: Invalid JS identifier starting with a digit
        Assert.assertFalse(TokenStream.isJSIdentifier("1example"));
        
        // Test case 5: Invalid JS identifier with special character
        Assert.assertFalse(TokenStream.isJSIdentifier("example*"));
        
        // Test case 6: Empty string
        Assert.assertFalse(TokenStream.isJSIdentifier(""));
    }
}