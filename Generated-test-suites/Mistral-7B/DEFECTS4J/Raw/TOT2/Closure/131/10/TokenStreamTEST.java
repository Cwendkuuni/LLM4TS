import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        String[] keywords = {"if", "else", "new", "return"};
        for (String keyword : keywords) {
            assertTrue(TokenStream.isKeyword(keyword));
        }
    }
}