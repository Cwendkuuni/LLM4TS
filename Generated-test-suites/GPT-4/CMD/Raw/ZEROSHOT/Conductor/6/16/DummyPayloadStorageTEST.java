
/*
 * Copyright 2022 Netflix, Inc.
 */

package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    ExternalPayloadStorage.Operation operation = ExternalPayloadStorage.Operation.READ;
    ExternalPayloadStorage.PayloadType payloadType = ExternalPayloadStorage.PayloadType.TASK_INPUT;
    String path = "path";

    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(location);
    }

    @Test
    public void uploadTest() {
        String payloadString = "test payload";
        InputStream payload = new ByteArrayInputStream(payloadString.getBytes());
        long payloadSize = payloadString.length();
        dummyPayloadStorage.upload(path, payload, payloadSize);
        //Since upload doesn't do anything in DummyPayloadStorage, there's no action here.
    }

    @Test
    public void downloadTest() {
        InputStream inputStream = dummyPayloadStorage.download(path);
        assertNull(inputStream);
    }
}
