package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath"));

        assertNull(dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        InputStream is = new ByteArrayInputStream("test".getBytes());
        dummyPayloadStorage.upload("dummyPath", is, 4);
        // No behavior to validate as DummyPayloadStorage does nothing on upload.
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("dummyPath"));
    }
}