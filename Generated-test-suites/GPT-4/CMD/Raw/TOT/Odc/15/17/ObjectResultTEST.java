
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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.any;


public class ObjectResultTest
{
    ObjectResult objectResult;
    ObjectStatus objectStatus;
    AtomicLong count;
    AtomicLong total;
    String type;

    @BeforeEach
    void setUp() {
        count = new AtomicLong(100);
        total = new AtomicLong(1000);
        type = "Table";
        objectStatus = new ObjectStatus("Test Name", count, total, ObjectStatus.Status.OK);
        objectResult = new ObjectResult("Test Schema", "Test Name", type);
    }

    @Test
    void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(count.get(), result.getCount().get());
        Assertions.assertEquals(total.get(), result.getTotal().get());
    }

    @Test
    void testGetSummary() {
        String summary = objectResult.getSummary();
        Assertions.assertEquals("Test Schema.Test Name[Table]", summary);
    }
}
