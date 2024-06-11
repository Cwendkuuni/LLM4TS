package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "testPath";
        InputStream payload = new ByteArrayInputStream("testPayload".getBytes());
        long payloadSize = payload.available();

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Assertions not applicable as the method does not modify the state of the object
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "testPath";

        assertNull(dummyPayloadStorage.download(path));
    }
}