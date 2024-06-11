
/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest() {
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(Operation.GET, PayloadType.TASK_OUTPUT, "path");
        Assert.assertNull(actual);
    }

    @Test
    public void uploadTest() {
        // A test payload could be created from a byte array like following:
        // InputStream payload = new ByteArrayInputStream("test payload".getBytes(StandardCharsets.UTF_8));
        // but given the implementation of the upload method in DummyPayloadStorage, it actually can't be tested as it has no functionality
        // Technically the test could be considered as passed once we can call the method without an exception
        try {
            InputStream payload = null; // replace null with the real InputStream when necessary
            dummyPayloadStorage.upload("path", payload, payload.length);
        } catch (Exception e) {
            Assert.fail("Exception should not have been thrown: " + e.getMessage());
        }
    }
  
    @Test
    public void downloadTest() {
        InputStream actual = dummyPayloadStorage.download("path");
        Assert.assertNull(actual);
    }
}
