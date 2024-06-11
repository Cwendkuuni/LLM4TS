import static org.junit.Assert.assertEquals;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        String[] keywords = {"if", "else", "for", "while", "switch", "case", "int", "float", "double", "boolean", "true", "false", "null"};
        String[] nonKeywords = {"identifier1", "identifier2", "identifier3"};

        for (String keyword : keywords) {
            assertEquals(true, TokenStream.isKeyword(keyword));
        }

        for (String nonKeyword : nonKeywords) {
            assertEquals(false, TokenStream.isKeyword(nonKeyword));
        }
    }
}