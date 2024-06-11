package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.run.Workflow.WorkflowOperation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(WorkflowOperation.UPLOAD, PayloadType.TASK_INPUT, "/path/to/file");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream("test payload".getBytes());
        storage.upload("/path/to/file", payload, payload.available());
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream downloadedPayload = storage.download("/path/to/file");
        assertNull(downloadedPayload);
    }
}