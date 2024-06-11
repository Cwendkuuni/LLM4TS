package com.google.javascript.rhino;
import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        Assert.assertTrue(TokenStream.isKeyword("if"));
        Assert.assertTrue(TokenStream.isKeyword("else"));
        Assert.assertTrue(TokenStream.isKeyword("else if"));
        Assert.assertTrue(TokenStream.isKeyword("for"));
        Assert.assertTrue(TokenStream.isKeyword("while"));
        Assert.assertTrue(TokenStream.isKeyword("switch"));
        Assert.assertTrue(TokenStream.isKeyword("break"));
        Assert.assertTrue(TokenStream.isKeyword("continue"));
        Assert.assertTrue(TokenStream.isKeyword("return"));
        Assert.assertTrue(TokenStream.isKeyword("void"));
        Assert.assertTrue(TokenStream.isKeyword("null"));
        Assert.assertTrue(TokenStream.isKeyword("this"));
        Assert.assertTrue(TokenStream.isKeyword("true"));
        Assert.assertTrue(TokenStream.isKeyword("false"));
        Assert.assertTrue(TokenStream.isKeyword("boolean"));
        Assert.assertTrue(TokenStream.isKeyword("int"));
        Assert.assertTrue(TokenStream.isKeyword("long"));
        Assert.assertTrue(TokenStream.isKeyword("float"));
        Assert.assertTrue(TokenStream.isKeyword("double"));
        Assert.assertTrue(TokenStream.isKeyword("byte"));
        Assert.assertTrue(TokenStream.isKeyword("short"));
        Assert.assertTrue(TokenStream.isKeyword("char"));
        Assert.assertTrue(TokenStream.isKeyword("String"));
        Assert.assertTrue(TokenStream.isKeyword("Object"));
        Assert.assertTrue(TokenStream.isKeyword("class"));
        Assert.assertTrue(TokenStream.isKeyword("extends"));
        Assert.assertTrue(TokenStream.isKeyword("implements"));
        Assert.assertTrue(TokenStream.isKeyword("interface"));
        Assert.assertTrue(TokenStream.isKeyword("package"));
        Assert.assertTrue(TokenStream.isKeyword("import"));
        Assert.assertTrue(TokenStream.isKeyword("static"));
        Assert.assertTrue(TokenStream.isKeyword("public"));
        Assert.assertTrue(TokenStream.isKeyword("private"));
        Assert.assertTrue(TokenStream.isKeyword("protected"));
        Assert.assertTrue(TokenStream.isKeyword("transient"));
        Assert.assertTrue(TokenStream.isKeyword("native"));
        Assert.assertTrue(TokenStream.isKeyword("synchronized"));
        Assert.assertTrue(TokenStream.isKeyword("final"));
        Assert.assertTrue(TokenStream.isKeyword("const"));
        Assert.assertTrue(TokenStream.isKeyword("new"));
        Assert.assertTrue(TokenStream.isKeyword("try"));
        Assert.assertTrue(TokenStream.isKeyword("catch"));
        Assert.assertTrue(TokenStream.isKeyword("throw"));
        Assert.assertTrue(TokenStream.isKeyword("throws"));
        Assert.assertTrue(TokenStream.isKeyword("finally"));
        Assert.assertTrue(TokenStream.isKeyword("delete"));
        Assert.assertTrue(TokenStream.isKeyword("export"));
        Assert.assertTrue(TokenStream.isKeyword("import"));
        Assert.assertTrue(TokenStream.isKeyword("as"));
        Assert.assertTrue(TokenStream.isKeyword("from"));
        Assert.assertTrue(TokenStream.isKeyword("let"));
        Assert.assertTrue(TokenStream.isKeyword("const"));
        Assert.assertTrue(TokenStream.isKeyword("await"));
        Assert.assertFalse(TokenStream.isKeyword("test")); // should be false since 'test' is not a keyword
    }

    @Test
    public void testIsJSIdentifier() {
        Assert.assertTrue(TokenStream.isJSIdentifier(""));
        Assert.assertTrue(TokenStream.isJSIdentifier("a"));
        Assert.assertTrue(TokenStream.isJSIdentifier("_"));
        Assert.assertTrue(TokenStream.isJSIdentifier("abc"));
        Assert.assertTrue(TokenStream.isJSIdentifier("123"));
        Assert.assertTrue(TokenStream.isJSIdentifier("isJSIdentifier"));
        Assert.assertTrue(TokenStream.isJSIdentifier("my_variable_name"));
        Assert.assertTrue(TokenStream.isJSIdentifier("myVariableName"));
        Assert.assertTrue(TokenStream.isJSIdentifier("camelCase"));
        Assert.assertTrue(TokenStream.isJSIdentifier("PascalCase"));
        Assert.assertTrue(TokenStream.isJSIdentifier("UNDERSCORE_CASE"));
        Assert.assertTrue(TokenStream.isJSIdentifier("ABCdef_456"));
        Assert.assertTrue(TokenStream.isJSIdentifier("my_long_identifier_name_with_numbers_and_underscores"));
        Assert.assertFalse(TokenStream.isJSIdentifier("123abc")); // should be false since '123' is not an identifier start
        Assert.assertFalse(TokenStream.isJSIdentifier("abc123")); // should be false since '123' is not an identifier part after the first character
        Assert.assertFalse(TokenStream.isJSIdentifier("js_keyword")); // should be false since 'js_keyword' is a reserved keyword
    }
}