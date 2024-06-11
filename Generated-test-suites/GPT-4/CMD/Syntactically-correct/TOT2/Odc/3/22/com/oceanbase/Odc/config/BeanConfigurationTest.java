package com.oceanbase.odc.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import org.junit.Test;

import org.jasypt.encryption.StringEncryptor;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.encode("romanz").startsWith("$2a$10$"));
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
    }

    @Test
    public void testHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = 
            new BeanConfiguration.NullCloudMetadataClient();

        assertNotNull(nullClient);
        assertTrue(nullClient.oceanbaseAccessMode() == OceanBaseAccessMode.DIRECT);
        assertTrue(nullClient.includeClusterNameForJdbcUsername());

        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());

        try {
            nullClient.listInstances();
        } catch (UnsupportedException e) {
            assertThat(e.getMessage(), is("CloudMetadata not supported"));
        }

        try {
            nullClient.listTenants("");
        } catch (UnsupportedException e) {
            assertThat(e.getMessage(), is("CloudMetadata not supported"));
        }

        try {
            nullClient.getTenant("", "");
        } catch (UnsupportedException e) {
            assertThat(e.getMessage(), is("getTenant not supported in non-could"));
        }

        try {
            nullClient.listDatabaseUsers("", "");
        } catch (UnsupportedException e) {
            assertThat(e.getMessage(), is("CloudMetadata not supported"));
        }

        try {
            nullClient.getSysTenantUser("");
        } catch (UnsupportedException e) {
            assertThat(e.getMessage(), is("CloudMetadata not supported"));
        }
    }
}