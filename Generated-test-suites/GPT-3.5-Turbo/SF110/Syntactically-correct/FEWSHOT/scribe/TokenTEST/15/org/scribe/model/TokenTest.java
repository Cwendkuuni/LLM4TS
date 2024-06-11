package org.scribe.model;
    
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
    
import java.io.Serializable;
    
import org.junit.Before;
import org.junit.Test;
    
public class TokenTest {
    
    private Token token;
    private String testTokenValue;
    private String testSecretValue;
    private String testRawResponseValue;
    
    @Before
    public void setup() {
        testTokenValue = "testToken";
        testSecretValue = "testSecret";
        testRawResponseValue = "testRawResponse";
        
        token = new Token(testTokenValue, testSecretValue, testRawResponseValue);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(token);
    }
    
    @Test
    public void testGetToken() {
        assertNotNull(token.getToken());
        assertEquals(testTokenValue, token.getToken());
    }
    
    @Test
    public void testGetSecret() {
        assertNotNull(token.getSecret());
        assertEquals(testSecretValue, token.getSecret());
    }
    
    @Test
    public void testGetRawResponse() {
        assertNotNull(token.getRawResponse());
        assertEquals(testRawResponseValue, token.getRawResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponseInvalid() {
        Token invalidToken = new Token(testTokenValue, testSecretValue);
        invalidToken.getRawResponse();
    }
    
    @Test
    public void testToString() {
        String expectedString = "Token[" + testTokenValue + " , " + testSecretValue + "]";
        assertEquals(expectedString, token.toString());
    }
    
}