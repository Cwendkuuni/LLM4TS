package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_OUTPUT, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test/path";
        byte[] payloadData = {1, 2, 3, 4, 5};
        InputStream payload = new ByteArrayInputStream(payloadData);
        dummyPayloadStorage.upload(path, payload, payloadData.length);
        // No exceptions expected
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyPayloadStorage.download("test/path");
        assertNull(downloadedPayload);
    }
    
    @Test
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, null);
        assertNull(location);
    }

    @Test
    public void testUploadWithEmptyPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test/path";
        byte[] payloadData = {};
        InputStream payload = new ByteArrayInputStream(payloadData);
        dummyPayloadStorage.upload(path, payload, payloadData.length);
        // No exceptions expected
    }

    @Test
    public void testDownloadWithNonExistentPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyPayloadStorage.download("non_existent_path");
        assertNull(downloadedPayload);
    }

}