package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {
    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.getLocation(Operation.DELETE, PayloadType.TASK_INPUT, "test"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.upload("test", new ByteArrayInputStream("dummy payload".getBytes()), 12);
      
        // No specific assertions for upload method as it does not return anything
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download("test"));
    }
}