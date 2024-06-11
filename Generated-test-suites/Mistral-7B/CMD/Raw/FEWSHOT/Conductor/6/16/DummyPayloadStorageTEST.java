package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
        assertNull(dummyPayloadStorage.getLocation(null, null, ""));
        assertNull(dummyPayloadStorage.getLocation(null, PayloadType.JSON, ""));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), null, ""));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, ""));
        assertNull(dummyPayloadStorage.getLocation(null, PayloadType.JSON, "/path"));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), null, "/path"));
        assertNull(dummyPayloadStorage.getLocation(new Operation(), PayloadType.JSON, "/path"));
    }

    @Test
    public void testUpload() {
        // Cannot test this method directly as it does not have any observable side effects
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.download(""));
        assertNull(dummyPayloadStorage.download("/path"));
    }
}