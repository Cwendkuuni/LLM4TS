
package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(Operation.GET, PayloadType.TASK_INPUT, "test_path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream inputStream = new ByteArrayInputStream("test payload".getBytes());
        storage.upload("test_path", inputStream, 12);
        // No assert as method has void return type
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream downloadedPayload = storage.download("test_path");
        assertNull(downloadedPayload);
    }
}
