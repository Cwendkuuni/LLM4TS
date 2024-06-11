
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new StartTag();
        assertEquals("StartTag", token.tokenType());
    }

    // ... Add other test cases for each method and edge cases here ...

    @Test
    public void testEmptyTagName() {
        StartTag token = new StartTag().name("");
        assertEquals("", token.name());
    }

    // ...
}
