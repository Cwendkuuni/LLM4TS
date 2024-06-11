package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "/test/path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        // DummyPayloadStorage does not implement this method, so no test case is needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "/test/path";

        assertNull(dummyPayloadStorage.download(path));
    }
}