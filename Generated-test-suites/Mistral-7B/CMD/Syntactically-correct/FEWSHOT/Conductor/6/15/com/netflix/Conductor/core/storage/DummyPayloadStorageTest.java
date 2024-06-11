package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "test-path";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No assertions needed as this method is a no-op
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "test-path";
        InputStream downloadedPayload = dummyPayloadStorage.download(path);

        assertNull(downloadedPayload);
    }
}