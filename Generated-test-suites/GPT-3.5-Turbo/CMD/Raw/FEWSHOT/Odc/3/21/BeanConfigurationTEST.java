package com.oceanbase.odc.config; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

public class BeanConfigurationTest {
  
  @Mock
  private SensitivePropertyHandler sensitivePropertyHandler;
  
  @Test
  public void testPasswordEncoder() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    assertNotNull(passwordEncoder);
  }
  
  @Test
  public void testObjectMapper() {
    CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
      .addSerializer(Internationalizable.class, new I18nOutputSerializer());
    ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
      .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
      .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
    assertNotNull(objectMapper);
  }
  
  @Test
  public void testDefaultHttpFirewall() {
    HttpFirewall httpFirewall = new DefaultHttpFirewall();
    assertNotNull(httpFirewall);
    assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
  }
  
  @Test
  public void testCloudMetadataClient() {
    CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
    
    // Testing overridden methods
    assertFalse(cloudMetadataClient.supportsCloudMetadata());
    assertFalse(cloudMetadataClient.supportsTenantInstance());
    assertFalse(cloudMetadataClient.needsOBTenantName());
    
    try {
      cloudMetadataClient.listInstances();
    } catch (UnsupportedException e) {
        assertEquals("CloudMetadata not supported", e.getMessage());
    }
    
    try {
      cloudMetadataClient.listTenants("instanceId");
    } catch (UnsupportedException e) {
        assertEquals("CloudMetadata not supported", e.getMessage());
    }
    
    try {
      cloudMetadataClient.getTenant("instanceId", "tenantId");
    } catch (UnsupportedException e) {
        assertEquals("getTenant not supported in non-cloud", e.getMessage());
    }
    
    try {
      cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    } catch (UnsupportedException e) {
        assertEquals("CloudMetadata not supported", e.getMessage());
    }
    
    OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
    assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    
    try {
      cloudMetadataClient.getSysTenantUser("instanceId");
    } catch (UnsupportedException e) {
        assertEquals("CloudMetadata not supported", e.getMessage());
    }
  }
  
  @Test
  public void testCookieSerializer() {
    CookieSerializer cookieSerializer = new DefaultCookieSerializer();
    assertNotNull(cookieSerializer);
    assertEquals("JSESSIONID", cookieSerializer.getCookieName());
  }  

}