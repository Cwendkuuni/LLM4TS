package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.ExternalStorageLocation.Operation;
import com.netflix.conductor.common.run.ExternalStorageLocation.PayloadType;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.DOWNLOAD, PayloadType.TASK_INPUT, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream("test payload".getBytes());
        dummyPayloadStorage.upload("test/path", inputStream, 12);
        // No assertion as the method is void
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream downloadedPayload = dummyPayloadStorage.download("test/path");
        assertNull(downloadedPayload);
    }
}