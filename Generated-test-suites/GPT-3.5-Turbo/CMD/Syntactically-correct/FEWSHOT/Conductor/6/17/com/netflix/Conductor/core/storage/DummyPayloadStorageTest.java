package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

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
        storage.upload(null, null, 0);
        // Add assertions or checks for the upload method if needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download(null));
    }
}