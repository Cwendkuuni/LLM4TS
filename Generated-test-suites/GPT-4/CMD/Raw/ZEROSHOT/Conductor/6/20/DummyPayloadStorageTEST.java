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
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

/**
 * DummyPayloadStorageTest
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void shouldReturnNullForGetLocation() {
        ExternalStorageLocation storageLocation = 
            dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.DATA,
                "path");
        assertNull(storageLocation);
    }

    @Test
    public void shouldDoNothingWhenUpload() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                return -1; // End of file
            }
        };

        // The method does not throw exception, meaning success
        dummyPayloadStorage.upload("path", inputStream, 1L);
    }

    @Test
    public void shouldReturnNullForDownload() {
        InputStream inputStream = dummyPayloadStorage.download("path");
        assertNull(inputStream);
    }
}