import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_case1() {
        String keyword = "if";
        boolean expectedResult = true;
        boolean result = TokenStream.isKeyword(keyword);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsKeyword_case2() {
        String keyword = "invalid";
        boolean expectedResult = false;
        boolean result = TokenStream.isKeyword(keyword);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsJSIdentifier_case1() {
        String identifier = "validIdentifier";
        boolean expectedResult = true;
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsJSIdentifier_case2() {
        String identifier = "1number";
        boolean expectedResult = false;
        boolean result = TokenStream.isJSIdentifier(identifier);
        assertEquals(expectedResult, result);
    }

}