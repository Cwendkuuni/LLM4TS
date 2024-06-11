package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BeanConfigurationTest {
    private BeanConfiguration subject;
    private SensitivePropertyHandler mockSensitivePropertyHandler;

    @Before
    public void setUp() {
        subject = new BeanConfiguration();
        mockSensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(subject.passwordEncoder());
    }

    @Test
    public void testObjectMapperNotNullSensitivePropertyHandler() {
        assertNotNull(subject.objectMapper(mockSensitivePropertyHandler));
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperNullSensitivePropertyHandler() {
        subject.objectMapper(null);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(subject.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(subject.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(subject.cookieSerializer());
    }
}