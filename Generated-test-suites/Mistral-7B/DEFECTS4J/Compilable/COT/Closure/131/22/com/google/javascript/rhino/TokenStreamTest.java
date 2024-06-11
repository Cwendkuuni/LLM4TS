package com.google.javascript.rhino;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_NullInput() {
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsKeyword_EmptyString() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeyword_FalseCases() {
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("_"));
        assertFalse(TokenStream.isKeyword("__"));
        assertFalse(TokenStream.isKeyword("__ABC"));
    }

    @Test
    public void testIsKeyword_TrueCases() {
        // Test 2-character keywords
        for (String keyword : new String[]{"if", "else", "for", "while", "in", "new", "try", "throw", "catch", "var", "switch", "case", "default", "break", "return", "function", "true", "false", "null", "this", "super"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 3-character keywords
        for (String keyword : new String[]{"int", "float", "double", "void", "long", "byte", "short", "boolean", "true", "false", "null", "this", "super", "goto", "case", "default", "if", "else", "for", "while", "in", "new", "try", "throw", "catch", "var", "switch", "break", "return", "function"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 4-character keywords
        for (String keyword : new String[]{"package", "import", "export", "class", "interface", "extends", "implements", "private", "protected", "public", "static", "final", "const", "native", "abstract", "static final", "synchronized"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 5-character keywords
        for (String keyword : new String[]{"native int", "native float", "native double", "native long", "native boolean", "native byte", "native short", "native char", "native int[]", "native long[]", "native float[]", "native double[]", "native boolean[]", "native Object", "native String", "native Function", "native Object[]", "native int[][]", "native int[][], int", "native int[][], int[]"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 6-character keywords
        for (String keyword : new String[]{"public static", "private static", "protected static", "final static", "abstract static", "native static", "synchronized static", "default int", "default float", "default long", "default double", "default char", "default boolean", "default void", "default Object", "default String", "instanceof", "new", "throw", "throws", "default return"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 7-character keywords
        for (String keyword : new String[]{"import java.lang.", "export default function", "export class MyClass {...}", "package com.mycompany.mypackage;", "public interface MyInterface {...}", "private static final int MY_CONSTANT = 0;", "protected static final String MY_CONSTANT_STRING = \"My String\";", "public abstract class MyAbstractClass {...}", "public static void main(String[] args) {...}", "public class MyClass implements MyInterface {...}"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 8-character keywords
        for (String keyword : new String[]{"public abstract static", "public abstract final", "public abstract synchronized"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 9-character keywords
        for (String keyword : new String[]{"public abstract static class", "public final static"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 10-character keywords
        for (String keyword : new String[]{"public abstract static final int"}) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        // Test 12-character keyword
        assertEquals(true, TokenStream.isKeyword("synchronized final"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_InvalidInput() {
        TokenStream.isKeyword("invalidInput");
    }
}