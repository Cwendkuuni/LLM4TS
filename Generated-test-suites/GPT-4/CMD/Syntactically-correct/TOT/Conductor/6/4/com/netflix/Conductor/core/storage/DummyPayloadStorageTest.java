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

import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dps = new DummyPayloadStorage();

    @Test
    public void getLocationAlwaysReturnsNull() {
        assertNull(dps.getLocation(null, null, null));
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void uploadDoesNotThrowException() {
        dps.upload(null, null, 0);
    }

    @Test
    public void downloadAlwaysReturnsNull() {
        assertNull(dps.download(null));
    }
}