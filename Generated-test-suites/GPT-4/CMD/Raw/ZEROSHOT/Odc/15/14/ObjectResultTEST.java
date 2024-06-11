
/*
 * Copyright (c) 2023 Tester.
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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private String schema = "schema";
    private String name = "name";
    private String type = "type";

    @Before
    public void setUp() {
        objectResult = new ObjectResult(schema, name, type);
    }

    @Test
    public void testOf(){
        ObjectStatus that = new ObjectStatus(name, new AtomicLong(0), new AtomicLong(0), null);
        that.setSchema(schema);
        that.setType(type);
        ObjectResult result = ObjectResult.of(that);
        assertEquals(result.getSchema(), that.getSchema());
        assertEquals(result.getName(), that.getName());
        assertEquals(result.getType(), that.getType());
        assertEquals(result.getCount().get(), that.getCount().get());
        assertEquals(result.getTotal().get(), that.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertNotEquals("", objectResult.getSummary());
    }

    @Test
    public void testExportPathsHandling() {
        assertNull(objectResult.getExportPaths());

        objectResult.setExportPaths(Arrays.asList(new URL("http://localhost/foo"), new URL("http://localhost/bar")));
        assertNotNull(objectResult.getExportPaths());
        assertEquals(2, objectResult.getExportPaths().size());
    }
}
