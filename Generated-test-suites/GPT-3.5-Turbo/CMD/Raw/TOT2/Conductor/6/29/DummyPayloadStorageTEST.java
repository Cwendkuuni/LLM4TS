package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummy/path");

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "test/path";
        InputStream payload = new ByteArrayInputStream("test".getBytes());
        long payloadSize = 4;

        storage.upload(path, payload, payloadSize);

        // No assertion, just testing if the method runs without errors
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        String path = "test/path";

        InputStream downloadedPayload = storage.download(path);

        assertNull(downloadedPayload);
    }
}