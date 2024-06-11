import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Keyword tests
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("else"));
        assertTrue(TokenStream.isKeyword("while"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("switch"));
        assertTrue(TokenStream.isKeyword("case"));
        assertTrue(TokenStream.isKeyword("break"));
        assertTrue(TokenStream.isKeyword("return"));
        assertTrue(TokenStream.isKeyword("class"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("boolean"));
        assertTrue(TokenStream.isKeyword("null"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("public"));
        assertTrue(TokenStream.isKeyword("private"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("const"));
        assertTrue(TokenStream.isKeyword("final"));
        assertTrue(TokenStream.isKeyword("import"));
        assertTrue(TokenStream.isKeyword("export"));
        assertTrue(TokenStream.isKeyword("extends"));
        assertTrue(TokenStream.isKeyword("implements"));
        assertTrue(TokenStream.isKeyword("instanceof"));
        assertTrue(TokenStream.isKeyword("native"));
        assertTrue(TokenStream.isKeyword("abstract"));
        assertTrue(TokenStream.isKeyword("interface"));
        assertTrue(TokenStream.isKeyword("package"));
        assertTrue(TokenStream.isKeyword("protected"));
        assertTrue(TokenStream.isKeyword("static"));
        assertTrue(TokenStream.isKeyword("transient"));
        assertTrue(TokenStream.isKeyword("synchronized"));
        assertTrue(TokenStream.isKeyword("throw"));
        assertTrue(TokenStream.isKeyword("catch"));
        assertTrue(TokenStream.isKeyword("try"));
        assertTrue(TokenStream.isKeyword("finally"));
        assertTrue(TokenStream.isKeyword("new"));
        assertTrue(TokenStream.isKeyword("this"));
        assertTrue(TokenStream.isKeyword("super"));

        // Non-keyword tests
        assertFalse(TokenStream.isKeyword("test"));
        assertFalse(TokenStream.isKeyword("MyClass"));
        assertFalse(TokenStream.isKeyword("_private"));
        assertFalse(TokenStream.isKeyword("myVariable"));
    }

    @Test
    public void testIsJSIdentifier() {
        // JS Identifier tests
        assertTrue(TokenStream.isJSIdentifier("identifier1"));
        assertTrue(TokenStream.isJSIdentifier("identifier_1"));
        assertTrue(TokenStream.isJSIdentifier("identifier123"));
        assertTrue(TokenStream.isJSIdentifier("identifier_123"));
        assertTrue(TokenStream.isJSIdentifier("identifier_with_dollar"));
        assertTrue(TokenStream.isJSIdentifier("identifier_with_dollar_1"));
        assertTrue(TokenStream.isJSIdentifier("identifier_with_underscore_and_dollar"));
        assertTrue(TokenStream.isJSIdentifier("identifier_with_underscore_and_dollar_1"));

        // Invalid JS Identifier tests
        assertFalse(TokenStream.isJSIdentifier("1identifier"));
        assertFalse(TokenStream.isJSIdentifier("identifier1_"));
        assertFalse(TokenStream.isJSIdentifier("identifier-1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_1-"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_space"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_space_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_tab"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_tab_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_newline"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_newline_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_carriage_return"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_carriage_return_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_backspace"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_backspace_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_vertical_tab"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_vertical_tab_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_form_feed"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_form_feed_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_backslash"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_backslash_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_double_quote"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_double_quote_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_single_quote"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_single_quote_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_single_quote_single_quote"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_single_quote_single_quote_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_semicolon"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_semicolon_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_comma"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_comma_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_colon"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_colon_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_equal"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_equal_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_less_than"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_less_than_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_greater_than"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_greater_than_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_question_mark"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_question_mark_1"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_at"));
        assertFalse(TokenStream.isJSIdentifier("identifier_with_at_1"));
    }
}