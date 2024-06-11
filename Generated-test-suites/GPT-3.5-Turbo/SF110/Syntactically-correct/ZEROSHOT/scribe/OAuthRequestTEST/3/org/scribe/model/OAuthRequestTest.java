package org.scribe.model;

import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class OAuthRequestTest {

    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        Assert.assertEquals(verb, request.getVerb());
        Assert.assertEquals(url, request.getUrl());
        Assert.assertNotNull(request.getOauthParameters());
        Assert.assertTrue(request.getOauthParameters().isEmpty());
    }

    @Test
    public void testAddOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        request.addOAuthParameter("oauth_consumer_key", "consumer_key");
        Map<String, String> oauthParameters = request.getOauthParameters();

        Assert.assertEquals(1, oauthParameters.size());
        Assert.assertEquals("consumer_key", oauthParameters.get("oauth_consumer_key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidOAuthParameter() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        request.addOAuthParameter("invalid_key", "value");
    }

    @Test
    public void testGetOauthParameters() {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://example.com");

        Map<String, String> oauthParameters = request.getOauthParameters();

        Assert.assertNotNull(oauthParameters);
        Assert.assertTrue(oauthParameters.isEmpty());
    }

    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "http://example.com";
        OAuthRequest request = new OAuthRequest(verb, url);

        String expectedString = String.format("@OAuthRequest(%s, %s)", verb, url);
        String toStringResult = request.toString();

        Assert.assertEquals(expectedString, toStringResult);
    }
}