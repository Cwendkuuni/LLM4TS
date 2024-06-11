package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import mockit.Mocked;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

  private BeanConfiguration beanConfiguration;

  @Mocked
  private SensitivePropertyHandler sensitivePropertyHandler;

  @Before
  public void setUp() throws Exception {
    beanConfiguration = new BeanConfiguration();
  }

  @Test
  public void passwordEncoder() {
    PasswordEncoder result = beanConfiguration.passwordEncoder();
    assertNotNull(result);
  }

  @Test
  public void objectMapper() {
    ObjectMapper result = beanConfiguration.objectMapper(sensitivePropertyHandler);
    assertNotNull(result);
  }

  @Test
  public void defaultHttpFirewall() {
    HttpFirewall result = beanConfiguration.defaultHttpFirewall();
    assertNotNull(result);
  }

  @Test
  public void cloudMetadataClient() {
    CloudMetadataClient result = beanConfiguration.cloudMetadataClient();
    assertNotNull(result);
  }

  @Test
  public void cookieSerializer() {
    CookieSerializer result = beanConfiguration.cookieSerializer();
    assertNotNull(result);
  }

  // Test cases would continue for the other helper methods in the class...
}
