package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
        assertNull(dummyPayloadStorage.getLocation(null, PayloadType.JSON, null));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, null));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), null, null));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, ""));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, "path"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = null;
        try {
            dummyPayloadStorage.upload("path", inputStream, 10L);
        } catch (Exception e) {
            // Expected, as the method does nothing in this implementation
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = dummyPayloadStorage.download("path");
        assertNull(inputStream);
    }
}