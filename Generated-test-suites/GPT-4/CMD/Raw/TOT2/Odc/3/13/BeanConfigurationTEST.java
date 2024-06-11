
/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.config;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * The unit test of BeanConfiguration
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2021-02-03 14:08
 */
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration testObject;

    @Test
    public void testPasswordEncoder() {
        Assert.assertNotNull(testObject.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        Assert.assertNotNull(testObject.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        Assert.assertNotNull(testObject.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        Assert.assertNotNull(testObject.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        Assert.assertNotNull(testObject.cookieSerializer());
    }
    
    @Test
    public void testNullCloudMetadataClientListInstancesException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        try {
            nullCloudMetadataClient.listInstances();
            Assert.fail("An UnsupportedException was expected");
        } catch (UnsupportedException ex) {
            Assert.assertEquals("CloudMetadata not supported", ex.getMessage());
        }
    }

    // Similar tests for other NullCloudMetadataClient methods that throw UnsupportedException
    // ...

}

