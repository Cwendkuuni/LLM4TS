package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullCloudMetadataClientTest {

    private BeanConfiguration.NullCloudMetadataClient underTest;

    @Before
    public void setUp() {
        underTest = new BeanConfiguration.NullCloudMetadataClient();
    }

    @Test
    public void doesNotSupportCloudMetadata() {
        assertFalse(underTest.supportsCloudMetadata());
    }

    @Test
    public void doesNotSupportTenantInstance() {
        assertFalse(underTest.supportsTenantInstance());
    }

    @Test
    public void doesNotNeedOBTenantName() {
        assertFalse(underTest.needsOBTenantName());
    }

    @Test
    public void doesNeedSysTenantUser() {
        assertFalse(underTest.needsSysTenantUser());
    }

    @Test
    public void includeClusterNameForJdbcUsername() {
        assertTrue(underTest.includeClusterNameForJdbcUsername());
    }
}
