import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_SimpleCases() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("else if"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("continue"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("int"));
        assertTrue(TokenStream.isKeyword("float"));
        assertTrue(TokenStream.isKeyword("double"));
        assertTrue(TokenStream.isKeyword("void"));
        assertTrue(TokenStream.isKeyword("short"));
        assertTrue(TokenStream.isKeyword("long"));
        assertTrue(TokenStream.isKeyword("byte"));
        assertTrue(TokenStream.isKeyword("char"));
        assertTrue(TokenStream.isKeyword("String"));
        assertTrue(TokenStream.isKeyword("Class"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("true"));
        assertTrue(TokenStream.isKeyword("false"));
    }

    @Test
    public void testIsKeyword_LongerIdentifiers() {
        assertTrue(TokenStream.isKeyword("ifelse"));
        assertTrue(TokenStream.isKeyword("switchcase"));
        assertTrue(TokenStream.isKeyword("elseifelse"));
        assertTrue(TokenStream.isKeyword("forin"));
        assertTrue(TokenStream.isKeyword("whilefalse"));
        assertTrue(TokenStream.isKeyword("forintot"));
        assertTrue(TokenStream.isKeyword("ifnew"));
        assertTrue(TokenStream.isKeyword("elseifnew"));
        assertTrue(TokenStream.isKeyword("booleanboolean"));
        assertTrue(TokenStream.isKeyword("intint"));
        assertTrue(TokenStream.isKeyword("floatfloat"));
        assertTrue(TokenStream.isKeyword("doubledouble"));
        assertTrue(TokenStream.isKeyword("voidvoid"));
        assertTrue(TokenStream.isKeyword("shortshort"));
        assertTrue(TokenStream.isKeyword("longlong"));
        assertTrue(TokenStream.isKeyword("bytebyte"));
        assertTrue(TokenStream.isKeyword("charchar"));
        assertTrue(TokenStream.isKeyword("StringString"));
        assertTrue(TokenStream.isKeyword("ClassClass"));
        assertTrue(TokenStream.isKeyword("AbstractAbstract"));
        assertTrue(TokenStream.isKeyword("interfaceinterface"));
        assertTrue(TokenStream.isKeyword("implementsimplements"));
        assertTrue(TokenStream.isKeyword("protectedprotected"));
        assertTrue(TokenStream.isKeyword("transienttransient"));
        assertTrue(TokenStream.isKeyword("synchronizedsynchronized"));
        assertTrue(TokenStream.isKeyword("packagepackage"));
        assertTrue(TokenStream.isKeyword("publicpublic"));
        assertTrue(TokenStream.isKeyword("staticstatic"));
        assertTrue(TokenStream.isKeyword("nativenative"));
        assertTrue(TokenStream.isKeyword("importimport"));
        assertTrue(TokenStream.isKeyword("exportexport"));
        assertTrue(TokenStream.isKeyword("throwsthrows"));
        assertTrue(TokenStream.isKeyword("catchcatch"));
        assertTrue(TokenStream.isKeyword("deletedelete"));
        assertTrue(TokenStream.isKeyword("finallyfinally"));
        assertTrue(TokenStream.isKeyword("returnreturn"));
        assertTrue(TokenStream.isKeyword("typeoftypeof"));
        assertTrue(TokenStream.isKeyword("exportdefault"));
        assertTrue(TokenStream.isKeyword("exportnamespace"));
        assertTrue(TokenStream.isKeyword("exportdeclare"));
        assertTrue(TokenStream.isKeyword("exportinterface"));
        assertTrue(TokenStream.isKeyword("exports"));
        assertTrue(TokenStream.isKeyword("exportclass"));
        assertTrue(TokenStream.isKeyword("export default function foo"));
        assertTrue(TokenStream.isKeyword("import foo from 'bar'"));
        assertTrue(TokenStream.isKeyword("import * as foo from 'bar'"));
        assertTrue(TokenStream.isKeyword("import { foo } from 'bar'"));
        assertTrue(TokenStream.isKeyword("import { foo as bar } from 'bar'"));
        assertTrue(TokenStream.isKeyword("import { foo as bar, bar as baz } from 'bar'"));
        assertTrue(TokenStream.isKeyword("import { foo, bar } from 'bar'"));
    }

    @Test
    public void testIsJSIdentifier_SimpleCases() {
        assertTrue(TokenStream.isJSIdentifier("foo"));
        assertTrue(TokenStream.isJSIdentifier("bar_baz"));
        assertTrue(TokenStream.isJSIdentifier("fooBar"));
        assertTrue(TokenStream.isJSIdentifier("foo_bar"));
        assertTrue(TokenStream.isJSIdentifier("foo__bar"));
    }

    @Test
    public void testIsJSIdentifier_EdgeCases() {
        assertFalse(TokenStream.isJSIdentifier(""));
        assertFalse(TokenStream.isJSIdentifier(" "));
        assertFalse(TokenStream.isJSIdentifier("foo "));
        assertFalse(TokenStream.isJSIdentifier("foo_"));
        assertFalse(TokenStream.isJSIdentifier("foo-bar"));
        assertFalse(TokenStream.isJSIdentifier("foo_123"));
        assertFalse(TokenStream.isJSIdentifier("foo_123_"));
        assertFalse(TokenStream.isJSIdentifier("foo_123_bar"));
        assertFalse(TokenStream.isJSIdentifier("foo_bar_123"));
        assertFalse(TokenStream.isJSIdentifier("foo_bar_123_"));
        assertFalse(TokenStream.isJSIdentifier("foo_bar_123__"));
        assertFalse(TokenStream.isJSIdentifier("foo_bar_123__1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_NullInput() {
        TokenStream.isKeyword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_EmptyInput() {
        TokenStream.isKeyword("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_LongerInput() {
        TokenStream.isKeyword("longerInputString");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_NullInput() {
        TokenStream.isJSIdentifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_EmptyInput() {
        TokenStream.isJSIdentifier("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_LongerInput() {
        TokenStream.isJSIdentifier("longerInputString");
    }
}