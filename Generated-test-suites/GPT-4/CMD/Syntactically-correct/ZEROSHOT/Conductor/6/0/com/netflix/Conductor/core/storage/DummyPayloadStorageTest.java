
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

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

/**
 * Test class for {@link DummyPayloadStorage}.
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage storage = new DummyPayloadStorage();

    @Test
    public void getLocation_Always_ReturnsNull() {
        assertNull(storage.getLocation(null, null, null));
    }

    @Test
    public void upload_Always_DoesNothing() {
        InputStream payload = InputStream.nullInputStream();
        storage.upload("path", payload, 0);  // No exception is expected.
    }

    @Test
    public void download_Always_ReturnsNull() {
        assertNull(storage.download("path"));
    }
}
