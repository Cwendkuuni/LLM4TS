import static org.junit.Assert.assertEquals;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        String[] keywords = {"if", "else", "for", "while", "switch", "case", "new", "class", "return", "int", "float", "double", "boolean", "void", "true", "false"};
        for (String keyword : keywords) {
            boolean result = TokenStream.isKeyword(keyword);
            assertEquals(true, result);
        }
    }
}