package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for the BeanConfiguration class.
 */
@Slf4j
@RunWith(SpringRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotNull(encodedPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapperNullInput() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);
        try {
            objectMapper.writeValueAsString(null);
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testObjectMapperEmptyList() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);
        ObjectMapper emptyListObjectMapper = new ObjectMapper();
        emptyListObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(emptyListObjectMapper);
        List<Object> emptyList = new ArrayList<>();
        assertNotNull(objectMapper.writeValueAsString(emptyList));
    }

    @Test
    public void testObjectMapperBoundaryValues() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);
        ObjectMapper emptyListObjectMapper = new ObjectMapper();
        emptyListObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(emptyListObjectMapper);
        Object testObject = new Object() {{ value = Long.MAX_VALUE; }};
        assertNotNull(objectMapper.writeValueAsString(testObject));
    }

    @Test
    public void testObjectMapperIllegalArgumentException() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);
        try {
            objectMapper.writeValueAsString(new String(""));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testObjectMapperJsonProcessingException() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);
        ObjectMapper emptyListObjectMapper = new ObjectMapper();
        emptyListObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(emptyListObjectMapper);
        Object testObject = new Object() {{ value = null; }};
        try {
            objectMapper.writeValueAsString(testObject);
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testHttpFirewallNullInput() {
        HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
        try {
            firewall.getAllowedHttpMethods().add("X-TEST");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHttpFirewallEmptyList() {
        HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
        List<String> emptyList = new ArrayList<>();
        try {
            firewall.setAllowedHttpMethods(emptyList);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHttpFirewallBoundaryValues() {
        HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
        try {
            firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "CONNECT", "TRACE"));
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testBeanConfiguration() {
        // TODO: Add tests for BeanConfiguration methods
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertFalse(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
        assertNull(cloudMetadataClient.listInstances());
        assertNull(cloudMetadataClient.listTenants(""));
        assertNull(cloudMetadataClient.getTenant("", ""));
        assertNull(cloudMetadataClient.listDatabaseUsers("", ""));
        assertNull(cloudMetadataClient.getTenantEndpoint("", ""));
        assertNull(cloudMetadataClient.getSysTenantUser(""));
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupportedException() {
        NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }
}

