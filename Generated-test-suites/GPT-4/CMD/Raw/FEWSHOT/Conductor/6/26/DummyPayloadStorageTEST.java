package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.io.InputStream;

public class DummyPayloadStorageTest {
    private final DummyPayloadStorage storage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalPayloadStorage.Operation operation = ExternalPayloadStorage.Operation.READ;
        ExternalPayloadStorage.PayloadType payloadType = ExternalPayloadStorage.PayloadType.TASK_INPUT;
        String path = "testPath";
        assertNull(storage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0L;
        storage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        String path = "testPath";
        assertNull(storage.download(path));
    }
}