import static org.junit.Assert.assertEquals;

public class TokenStreamTest {
    // ...

    @Test
    public void testIsKeyword() {
        assertEquals(true, TokenStream.isKeyword("if"));
        assertEquals(true, TokenStream.isKeyword("else"));
        assertEquals(true, TokenStream.isKeyword("else if"));
        // Add more true cases

        assertEquals(false, TokenStream.isKeyword("myVariable"));
        // Add more false cases
    }
}