/*
 * Copyright 2022 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.core.storage;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        Assert.assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "somepath"));
    }

    @Test
    public void testUpload() {
        InputStream inputStream = new java.io.ByteArrayInputStream("input".getBytes());
        dummyPayloadStorage.upload("path", inputStream, 5); // As the upload function does nothing, so just testing that it doesn't throw an errors
    }

    @Test
    public void testDownload() {
        Assert.assertNull(dummyPayloadStorage.download("path"));
    }
}