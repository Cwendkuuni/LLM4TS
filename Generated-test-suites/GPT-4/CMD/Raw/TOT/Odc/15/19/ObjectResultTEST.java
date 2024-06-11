
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

package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void createObjectResultTest() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");

        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }
  
    @Test
    public void createObjectResultFromExistingObjectTest() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("testSchema");
        objectStatus.setName("testName");
        objectStatus.setType("testType");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertEquals(10, objectResult.getCount().get());
        assertEquals(20, objectResult.getTotal().get());
    }

    @Test
    public void getSummaryTest() throws MalformedURLException {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        objectResult.setExportPaths(Arrays.asList(new URL("http://test.com")));

        String expectedSummary = "testSchema.testName[testType]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }
}
