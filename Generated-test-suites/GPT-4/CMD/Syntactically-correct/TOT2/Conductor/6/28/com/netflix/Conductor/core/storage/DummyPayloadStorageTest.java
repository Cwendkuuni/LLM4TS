
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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private final DummyPayloadStorage tested = new DummyPayloadStorage();
    private final String path = "/random/path";
    private final InputStream payload = new ByteArrayInputStream(new byte[0]);
    private final long payloadSize = 0L;

    @Test
    public void getLocation_shouldAlwaysReturnNull() {
        ExternalStorageLocation location = tested.getLocation(Operation.READ, PayloadType.TASK_INPUT, path);
        assertNull(location);
    }

    @Test
    public void upload_shouldNotThrowException() {
        try{
            tested.upload(path, payload, payloadSize);
        } catch (Exception e){
            fail("upload() method should not throw any exception");
        }
    }

    @Test
    public void download_shouldAlwaysReturnNull() {
        InputStream downloadedPayload = tested.download(path);
        assertNull(downloadedPayload);
    }
}
