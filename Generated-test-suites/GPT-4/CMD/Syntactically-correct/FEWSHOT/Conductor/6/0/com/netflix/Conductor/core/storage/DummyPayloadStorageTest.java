
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path"));
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        String path = "path";
        InputStream payload = new ByteArrayInputStream("dummy payload".getBytes());
        long payloadSize = payload.available();
        dummyPayloadStorage.upload(path, payload, payloadSize);
        // Since the DummyPayloadStorage does nothing on upload, there's nothing to assert here
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}
