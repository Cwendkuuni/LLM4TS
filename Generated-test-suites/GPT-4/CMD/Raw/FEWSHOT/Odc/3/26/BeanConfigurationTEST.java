package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HttpFirewall defaultHttpFirewall;

    @Autowired
    @Qualifier("cloudMetadataClient")
    CloudMetadataClient cloudMetadataClient;
    
    @Autowired
    SensitivePropertyHandler sensitivePropertyHandler;
    
    @Test
    public void testPasswordEncoder() {
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
        assertNotNull(sensitivePropertyHandler);
        assertTrue(objectMapper.canSerialize(OBTenant.class));
        assertTrue(objectMapper.canSerialize(OBInstance.class));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListInstancesException() {
        cloudMetadataClient.listInstances();
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListTenantsException() {
        cloudMetadataClient.listTenants("dummyInstanceID");
    }
    
    @Test(expected = UnsupportedException.class)
    public void testGetTenantException() {
        cloudMetadataClient.getTenant("dummyInstanceID", "dummyTenantID");
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersException() {
        cloudMetadataClient.listDatabaseUsers("dummyInstanceID", "dummyTenantID");
    }
    
    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserException() {
        cloudMetadataClient.getSysTenantUser("dummyInstanceID");
    }
}