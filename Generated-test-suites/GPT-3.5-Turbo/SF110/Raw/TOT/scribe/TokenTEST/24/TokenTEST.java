import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token("exampleToken", "exampleSecret", "exampleRawResponse");
    }
    
    @Test
    public void testGetToken() {
        assertEquals("exampleToken", token.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("exampleSecret", token.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("exampleRawResponse", token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithNull() {
        Token nullToken = new Token("nullToken", "nullSecret");
        nullToken.getRawResponse();
    }

    @Test
    public void testToString() {
        assertEquals("Token[exampleToken , exampleSecret]", token.toString());
    }

}