
/*
 * Copyright 2022 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest() {
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(result);
    }

    @Test
    public void uploadTest() {
        try {
            dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Upload method should not throw any exception.");
        }
    }

    @Test
    public void downloadTest() {
        InputStream result = dummyPayloadStorage.download(null);
        assertNull(result);
    }

    @Test
    public void uploadTest_NegativePayloadSize() {
        try {
            dummyPayloadStorage.upload("pathToPayload", null, -10);
        } catch (Exception e) {
            fail("Upload method should not throw any exception for negative payload size.");
        }
    }
}
