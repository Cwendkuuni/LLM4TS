package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummy = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummy.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummy_path"));
    }

    @Test
    public void testUpload() {
        InputStream dummyStream = new ByteArrayInputStream("dummy_payload".getBytes());
        dummy.upload("dummy_path", dummyStream, 12);
        // No exception means test passed because there's no specific implementation for 'upload' in DummyPayloadStorage
    }

    @Test
    public void testDownload() {
        assertNull(dummy.download("dummy_path"));
    }
}
