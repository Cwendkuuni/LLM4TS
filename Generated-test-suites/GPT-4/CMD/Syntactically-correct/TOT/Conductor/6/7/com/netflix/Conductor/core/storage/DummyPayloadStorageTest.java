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

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage storage;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void getLocation_ReturnsNull() {
        assertNull(storage.getLocation(Operation.ADD, PayloadType.TASK_INPUT, "/path/to/file"));
    }

    @Test
    public void upload_DoesNotThrowException() {
        try {
            storage.upload("/path", null, 0);
        } catch (Exception e) {
            fail("Upload operation threw an exception: " + e);
        }
    }

    @Test
    public void download_ReturnsNull() {
        assertNull(storage.download("/path"));
    }

}