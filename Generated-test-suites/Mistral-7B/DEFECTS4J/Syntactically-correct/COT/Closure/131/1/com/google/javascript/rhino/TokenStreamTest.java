import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_nullName() {
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsKeyword_EmptyString() {
        assertFalse(TokenStream.isKeyword(""));
    }

    @Test
    public void testIsKeyword_AllShortKeywords() {
        testIsKeyword("if", true);
        testIsKeyword("else", true);
        testIsKeyword("else if", true);
        testIsKeyword("return", true);
        testIsKeyword("break", true);
        testIsKeyword("continue", true);
        testIsKeyword("void", true);
        testIsKeyword("null", true);
        testIsKeyword("true", true);
        testIsKeyword("false", true);
        testIsKeyword("this", true);
        testIsKeyword("case", true);
        testIsKeyword("default", true);
        testIsKeyword("try", true);
        testIsKeyword("catch", true);
        testIsKeyword("throw", true);
        testIsKeyword("new", true);
        testIsKeyword("class", true);
        testIsKeyword("let", true);
        testIsKeyword("const", true);
        testIsKeyword("function", true);
        testIsKeyword("export", true);
        testIsKeyword("import", true);
        testIsKeyword("package", true);
    }

    @Test
    public void testIsKeyword_LongKeywords() {
        testIsKeyword("boolean", true);
        testIsKeyword("breakpoint", false);
        testIsKeyword("byte", true);
        testIsKeyword("bytecode", false);
        testIsKeyword("catchable", false);
        testIsKeyword("catchall", false);
        testIsKeyword("classname", false);
        testIsKeyword("constantly", false);
        testIsKeyword("continueable", false);
        testIsKeyword("debugger", false);
        testIsKeyword("defaultcase", false);
        testIsKeyword("description", false);
        testIsKeyword("elseifelse", false);
        testIsKeyword("finallyfinally", false);
        testIsKeyword("functionfunction", false);
        testIsKeyword("goto", true);
        testIsKeyword("importfrom", false);
        testIsKeyword("interfaceinterface", false);
        testIsKeyword("instanceofinstanceof", false);
        testIsKeyword("longlong", false);
        testIsKeyword("nativenative", false);
        testIsKeyword("newnew", false);
        testIsKeyword("nullnull", false);
        testIsKeyword("objectobject", false);
        testIsKeyword("packagepackage", false);
        testIsKeyword("protectedprotected", false);
        testIsKeyword("publicpublic", false);
        testIsKeyword("returnreturn", false);
        testIsKeyword("synchronizedsynchronized", false);
        testIsKeyword("supersuper", false);
        testIsKeyword("throwsthrows", false);
        testIsKeyword("transienttransient", false);
        testIsKeyword("typeoftypeof", false);
        testIsKeyword("typeoffunction", false);
        testIsKeyword("volatilevolatile", false);
        testIsKeyword("withwith", false);
        testIsKeyword("yieldyield", false);
    }

    @Test
    public void testIsKeyword_SpecialKeywords() {
        testIsKeyword("false", true);
        testIsKeyword("null", true);
        testIsKeyword("true", true);
        testIsKeyword("new", true);
        testIsKeyword("let", true);
        testIsKeyword("const", true);
        testIsKeyword("function", true);
        testIsKeyword("try", true);
        testIsKeyword("catch", true);
        testIsKeyword("throw", true);
        testIsKeyword("export", true);
        testIsKeyword("import", true);
        testIsKeyword("package", true);
        testIsKeyword("interface", true);
        testIsKeyword("extends", true);
        testIsKeyword("implements", true);
        testIsKeyword("static", true);
        testIsKeyword("public", true);
        testIsKeyword("protected", true);
        testIsKeyword("this", true);
        testIsKeyword("transient", true);
        testIsKeyword("volatile", true);
        testIsKeyword("yield", true);
    }

    @Test
    public void testIsJSIdentifier_EmptyIdentifier() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }

    @Test
    public void testIsJSIdentifier_InvalidIdentifier() {
        assertFalse(TokenStream.isJSIdentifier("#"));
        assertFalse(TokenStream.isJSIdentifier("!"));
        assertFalse(TokenStream.isJSIdentifier("~"));
        assertFalse(TokenStream.isJSIdentifier("$"));
        assertFalse(TokenStream.isJSIdentifier("%"));
        assertFalse(TokenStream.isJSIdentifier("^"));
        assertFalse(TokenStream.isJSIdentifier("&"));
        assertFalse(TokenStream.isJSIdentifier("*"));
        assertFalse(TokenStream.isJSIdentifier("("));
        assertFalse(TokenStream.isJSIdentifier(")"));
        assertFalse(TokenStream.isJSIdentifier("_"));
        assertFalse(TokenStream.isJSIdentifier("+"));
        assertFalse(TokenStream.isJSIdentifier("-"));
        assertFalse(TokenStream.isJSIdentifier("="));
        assertFalse(TokenStream.isJSIdentifier(">"));
        assertFalse(TokenStream.isJSIdentifier("<"));
        assertFalse(TokenStream.isJSIdentifier("{"));
        assertFalse(TokenStream.isJSIdentifier("}"));
        assertFalse(TokenStream.isJSIdentifier("["));
        assertFalse(TokenStream.isJSIdentifier("]"));
        assertFalse(TokenStream.isJSIdentifier("|"));
        assertFalse(TokenStream.isJSIdentifier("\\"));
        assertFalse(TokenStream.isJSIdentifier(";"));
        assertFalse(TokenStream.isJSIdentifier("\""));
        assertFalse(TokenStream.isJSIdentifier("\'"));
        assertFalse(TokenStream.isJSIdentifier("/"));
    }

    @Test
    public void testIsJSIdentifier_ValidIdentifiers() {
        testIsJSIdentifier("identifier1", true);
        testIsJSIdentifier("identifier_2", true);
        testIsJSIdentifier("identifierWithNumber", true);
        testIsJSIdentifier("identifierWithUnderScore", true);
    }

    private void testIsJSIdentifier(String identifier, boolean expected) {
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_ExpectedException() {
        expect(RuntimeException.class, () -> {
            TokenStream.isKeyword("ArrayBuffer");
        });

        expect(RuntimeException.class, () -> {
            TokenStream.isKeyword("module");
        });
    }

    private static void expect(Class<? extends Throwable> expectedExceptionType, Runnable test) {
        try {
            test.run();
            fail("Expected exception: " + expectedExceptionType.getSimpleName());
        } catch (Throwable e) {
            if (expectedExceptionType.isInstance(e)) {
                // pass
            } else {
                fail("Unexpected exception: " + e.getClass().getSimpleName());
            }
        }
    }
}