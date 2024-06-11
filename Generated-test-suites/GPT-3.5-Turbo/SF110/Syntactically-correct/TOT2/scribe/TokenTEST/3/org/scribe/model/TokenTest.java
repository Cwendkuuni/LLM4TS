package org.scribe.model;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token("testToken", "testSecret", "testRawResponse");
    }
    
    // Test constructor 1
    @Test
    public void testConstructor1() {
        Token newToken = new Token("newToken", "newSecret");
        assertEquals("newToken", newToken.getToken());
        assertEquals("newSecret", newToken.getSecret());
        assertNull(newToken.getRawResponse());
    }
    
    // Test constructor 2
    @Test
    public void testConstructor2() {
        assertEquals("testToken", token.getToken());
        assertEquals("testSecret", token.getSecret());
        assertEquals("testRawResponse", token.getRawResponse());
    }
    
    // Test getToken method
    @Test
    public void testGetToken() {
        assertEquals("testToken", token.getToken());
    }
    
    // Test getSecret method
    @Test
    public void testGetSecret() {
        assertEquals("testSecret", token.getSecret());
    }
    
    // Test getRawResponse with rawResponse
    @Test
    public void testGetRawResponse() {
        assertEquals("testRawResponse", token.getRawResponse());
    }
    
    // Test getRawResponse without rawResponse (exception case)
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseWithoutRawResponse() {
        Token newToken = new Token("newToken", "newSecret");
        newToken.getRawResponse();
    }
    
    // Test toString method
    @Test
    public void testToString() {
        assertEquals("Token[testToken , testSecret]", token.toString());
    }
}