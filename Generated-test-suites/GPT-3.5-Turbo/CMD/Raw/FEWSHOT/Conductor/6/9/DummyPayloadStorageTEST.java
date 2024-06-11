package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.upload("test-path", null, 0);
        // No validation for upload method as it does not return anything
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download("test-path"));
    }
}