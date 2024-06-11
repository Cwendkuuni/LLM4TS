package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        
        ExternalStorageLocation location = dummy.getLocation(Operation.GET, PayloadType.TASK_INPUT, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        
        String path = "test/upload";
        InputStream payload = new ByteArrayInputStream("test payload".getBytes());
        long payloadSize = payload.available();
        
        dummy.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        
        String path = "test/download";
        InputStream downloadedPayload = dummy.download(path);
        assertNull(downloadedPayload);
    }
}