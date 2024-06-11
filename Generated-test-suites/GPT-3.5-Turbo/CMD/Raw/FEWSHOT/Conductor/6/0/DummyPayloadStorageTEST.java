package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test/path"));
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload("test/path", new ByteArrayInputStream(new byte[10]), 10);
        // Add assertions if necessary
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("test/path"));
    }
}