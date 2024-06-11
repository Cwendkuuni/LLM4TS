package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage.Operation;
import com.netflix.conductor.core.storage.DummyPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();

        ExternalStorageLocation location = storage.getLocation(Operation.UPLOAD, PayloadType.TASK_INPUT, "test/path");
        
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream input = new ByteArrayInputStream(new byte[10]);

        storage.upload("test/path", input, 10);

        // Add assertion here if needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();

        InputStream output = storage.download("test/path");

        assertNull(output);
    }
}