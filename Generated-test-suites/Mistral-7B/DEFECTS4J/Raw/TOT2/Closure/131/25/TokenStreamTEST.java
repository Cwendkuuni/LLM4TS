import static org.junit.Assert.assertEquals;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertEquals(true, TokenStream.isKeyword("if"));
        assertEquals(true, TokenStream.isKeyword("else"));
        assertEquals(true, TokenStream.isKeyword("while"));
        // Add more test cases for other keywords
    }
}