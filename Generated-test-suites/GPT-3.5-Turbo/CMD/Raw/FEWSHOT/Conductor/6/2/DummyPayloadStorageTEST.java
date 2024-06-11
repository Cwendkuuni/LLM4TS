package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test/path"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        storage.upload("test/path", inputStream, 10);
        // Add assertions if needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download("test/path"));
    }
}