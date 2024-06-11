import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    private Token testToken;

    @Before
    public void setUp() throws Exception {
        testToken = new Token("testToken", "testSecret", "testRawResponse");
    }

    @Test
    public void testConstructor1() {
        Token token = new Token("constructorTest", "constructorSecret");
        assertEquals("constructorTest", token.getToken());
        assertEquals("constructorSecret", token.getSecret());
        assertNull(token.getRawResponse());
    }

    @Test
    public void testConstructor2() {
        assertEquals("testToken", testToken.getToken());
        assertEquals("testSecret", testToken.getSecret());
        assertEquals("testRawResponse", testToken.getRawResponse());
    }

    @Test
    public void testGetToken() {
        assertEquals("testToken", testToken.getToken());
    }

    @Test
    public void testGetSecret() {
        assertEquals("testSecret", testToken.getSecret());
    }

    @Test
    public void testGetRawResponse() {
        assertEquals("testRawResponse", testToken.getRawResponse());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseException() {
        Token token = new Token("exceptionToken", "exceptionSecret");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        assertEquals("Token[testToken , testSecret]", testToken.toString());
    }
}