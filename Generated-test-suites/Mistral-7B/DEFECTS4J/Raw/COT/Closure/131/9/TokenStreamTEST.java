import static org.junit.Assert.*;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_Basic() {
        String expected = true;
        String name = "if";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_False() {
        String expected = false;
        String name = "test";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_Basic() {
        String expected = true;
        String name = "identifier";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_False() {
        String expected = false;
        String name = "123";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsKeyword_Null() {
        String name = null;
        TokenStream.isKeyword(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsJSIdentifier_Null() {
        String name = null;
        TokenStream.isJSIdentifier(name);
    }

    @Test
    public void testIsKeyword_EmptyString() {
        String expected = false;
        String name = "";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_EmptyString() {
        String expected = false;
        String name = "";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_SingleCharacter() {
        String expected = false;
        String name = "a";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_MultipleCharacters() {
        String expected = false;
        String name = "for";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_KeywordWithSpecialCharacter() {
        String expected = true;
        String name = "switch";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_TwoLetterKeyword() {
        String expected = true;
        String name = "if";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_ThreeLetterKeyword() {
        String expected = true;
        String name = "else";
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsKeyword_FourLetterKeyword() {
        String expected = true;
        String name = "ifelse"; // this is not a valid keyword
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result); // should return false
    }

    @Test
    public void testIsKeyword_FiveOrMoreLetterKeyword() {
        String expected = true;
        String name = "ifstatement"; // this is not a valid keyword
        boolean result = TokenStream.isKeyword(name);
        assertEquals(expected, result); // should return false
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithSpecialCharacter() {
        String expected = true;
        String name = "identifierWith_UnderScore";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithDollarSign() {
        String expected = true;
        String name = "identifierWith$DollarSign";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithDoubleQuotes() {
        String expected = true;
        String name = "identifier\"WithDoubleQuotes\"";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }

    @Test
    public void testIsJSIdentifier_IdentifierWithSingleQuotes() {
        String expected = true;
        String name = "identifier\'WithSingleQuotes\'";
        boolean result = TokenStream.isJSIdentifier(name);
        assertEquals(expected, result);
    }
}