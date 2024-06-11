
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
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationTest() {
        Assert.assertNull(this.dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ,
                ExternalPayloadStorage.PayloadType.TASK_INPUT,
                "dummy_path"));
    }

    @Test
    public void uploadTest() {
        this.dummyPayloadStorage.upload("dummy_path", null, 0L);
        // There is no behavior implemented in 'upload', hence there is no point checking any post conditions.
    }

    @Test
    public void downloadTest() {
        InputStream inputStream = this.dummyPayloadStorage.download("dummy_path");
        Assert.assertNull(inputStream);
    }
}
