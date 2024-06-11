
package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OAuthConfigTest {
  
  @Test
  public void testConstructorWithKeyAndSecret() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret");
    
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertEquals("oob", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertNull(config.getScope());
    assertFalse(config.hasScope());
  }
  
  @Test
  public void testConstructorWithAllParameters() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Query, "scope");
    
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertEquals("callback", config.getCallback());
    assertEquals(SignatureType.Query, config.getSignatureType());
    assertEquals("scope", config.getScope());
    assertTrue(config.hasScope());
  }
  
  @Test
  public void testConstructorWithNullCallbackAndScope() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", null, null, null);
    
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertEquals("oob", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertNull(config.getScope());
    assertFalse(config.hasScope());
  }
  
  @Test
  public void testGetters() {
    OAuthConfig config = new OAuthConfig("apiKey", "apiSecret", "callback", SignatureType.Header, "scope");
    
    assertEquals("apiKey", config.getApiKey());
    assertEquals("apiSecret", config.getApiSecret());
    assertEquals("callback", config.getCallback());
    assertEquals(SignatureType.Header, config.getSignatureType());
    assertEquals("scope", config.getScope());
    assertTrue(config.hasScope());
  }
}
