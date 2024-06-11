package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanConfigurationTest {
    private ApplicationContext context;

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    }

    @Test
    public void passwordEncoderBeanTest() {
        assertNotNull(context.getBean(PasswordEncoder.class));
    }

    @Test
    public void objectMapperBeanTest() {
        assertNotNull(context.getBean(ObjectMapper.class));
    }

    @Test
    public void defaultHttpFirewallBeanTest() {
        assertNotNull(context.getBean(HttpFirewall.class));
    }

    @Test
    public void cloudMetadataClientBeanTest() {
        assertNotNull(context.getBean(CloudMetadataClient.class));
    }

    // Test NullCloudMetadataClient methods
    @Test(expected = UnsupportedException.class)
    public void listInstancesTest() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        client.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void listTenantsTest() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        client.listTenants("");
    }

    @Test(expected = UnsupportedException.class)
    public void getTenantTest() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        client.getTenant("", "");
    }

    @Test(expected = UnsupportedException.class)
    public void listDatabaseUsersTest() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        client.listDatabaseUsers("", "");
    }

    // Another case - defined behavior
    @Test
    public void getTenantEndpointTest() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        OBTenantEndpoint endpoint = client.getTenantEndpoint("", "");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }
}