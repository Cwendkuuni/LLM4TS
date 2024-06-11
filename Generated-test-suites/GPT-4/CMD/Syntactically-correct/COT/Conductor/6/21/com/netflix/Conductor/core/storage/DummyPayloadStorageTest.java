package com.netflix.conductor.core.storage;


import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocation_ReturnsNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, "path");
        assertNull(location);
    }

    @Test
    public void upload_DoesNotThrow() {
        InputStream testStream = new ByteArrayInputStream("test payload".getBytes());
        dummyPayloadStorage.upload("path", testStream, "test payload".length());
    }

    @Test
    public void download_ReturnsNull() {
        InputStream resultStream = dummyPayloadStorage.download("path");
        assertNull(resultStream);
    }

    /* Edge case of path being null for all methods */

    @Test
    public void getLocationWithNullPath_ReturnsNull() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, null);
        assertNull(location);
    }

    @Test
    public void uploadWithNullPath_DoesNotThrow() {
        InputStream testStream = new ByteArrayInputStream("test payload".getBytes());
        dummyPayloadStorage.upload(null, testStream, "test payload".length());
    }

    @Test
    public void downloadWithNullPath_ReturnsNull() {
        InputStream resultStream = dummyPayloadStorage.download(null);
        assertNull(resultStream);
    }

    /* Edge case of payload and payload size being null for 'upload' method */

    @Test
    public void uploadWithNullPayload_DoesNotThrow() {
        dummyPayloadStorage.upload("path", null, 0);
    }
}

