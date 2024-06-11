
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for dummy implementation of {@link ExternalPayloadStorage}
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.DATA, "path/"
        );
        assertNull(location);
    }

    @Test
    public void testUpload() {
        InputStream input = new ByteArrayInputStream("test-payload".getBytes());
        dummyPayloadStorage.upload("test/path", input, 12);
        // since there's no side-effect, there is no assert here. We're testing to ensure no exceptions are thrown.
    }

    @Test
    public void testDownload() {
        InputStream resultStream = dummyPayloadStorage.download("test/path");
        assertNull(resultStream);
    }
}
