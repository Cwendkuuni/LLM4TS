package org.scribe.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

public class OAuthConfigTest {

    private OAuthConfig oAuthConfig;

    @Before
    public void setUp() {
        oAuthConfig = new OAuthConfig("testKey", "testSecret", "testCallback", SignatureType.Header, "testScope");
    }

    @Test
    public void testGetApiKey() {
        assertEquals("testKey", oAuthConfig.getApiKey());
    }

    @Test
    public void testGetApiSecret() {
        assertEquals("testSecret", oAuthConfig.getApiSecret());
    }

    @Test
    public void testGetCallback() {
        assertEquals("testCallback", oAuthConfig.getCallback());
    }

    @Test
    public void testGetSignatureType() {
        assertEquals(SignatureType.Header, oAuthConfig.getSignatureType());
    }

    @Test
    public void testGetScope() {
        assertEquals("testScope", oAuthConfig.getScope());
    }

    @Test
    public void testHasScope() {
        assertTrue(oAuthConfig.hasScope());

        OAuthConfig noScopeConfig = new OAuthConfig("testKey", "testSecret");
        assertFalse(noScopeConfig.hasScope());
    }

}