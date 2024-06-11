package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        assertNull(dummy.getLocation(Operation.SOME_OPERATION, PayloadType.SOME_PAYLOAD_TYPE, "somePath"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        dummy.upload("somePath", new ByteArrayInputStream(new byte[10]), 10);
        // Add assertions if necessary
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        assertNull(dummy.download("somePath"));
    }
}