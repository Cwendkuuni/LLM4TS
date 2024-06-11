package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OAuthRequestTest {
    private OAuthRequest request;

    @Before
    public void setUp() {
        request = new OAuthRequest(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddOAuthParameter() {
        request.addOAuthParameter("key1", "value1");
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("key1", "value1");
        assertEquals(expectedParameters, request.getOauthParameters());
        
        request.addOAuthParameter("key2", "value2");
        expectedParameters.put("key2", "value2");
        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOAuthParameterWithInvalidKey() {
        request.addOAuthParameter("invalid", "value");
    }

    @Test
    public void testGetOauthParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        assertEquals(expectedParameters, request.getOauthParameters());
        
        request.addOAuthParameter("key1", "value1");
        expectedParameters.put("key1", "value1");
        assertEquals(expectedParameters, request.getOauthParameters());
        
        request.addOAuthParameter("key2", "value2");
        expectedParameters.put("key2", "value2");
        assertEquals(expectedParameters, request.getOauthParameters());
    }

    @Test
    public void testToString() {
        assertEquals("@OAuthRequest(GET, http://example.com)", request.toString());

        request = new OAuthRequest(Verb.POST, "http://example.com");
        assertEquals("@OAuthRequest(POST, http://example.com)", request.toString());

        request = new OAuthRequest(Verb.PUT, "http://example.com");
        assertEquals("@OAuthRequest(PUT, http://example.com)", request.toString());
    }
}