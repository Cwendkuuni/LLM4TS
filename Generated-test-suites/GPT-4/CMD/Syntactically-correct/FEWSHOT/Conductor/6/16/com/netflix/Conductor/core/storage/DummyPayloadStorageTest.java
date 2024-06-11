
package com.netflix.conductor.core.storage;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "path"));
        assertNull(dummyStorage.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("Test Payload".getBytes());
        // As there's no implementation, this is just to ensure that the method runs without exceptions.
        dummyStorage.upload("path", payload, 12L);
    }

    @Test
    public void testDownload() {
        assertNull(dummyStorage.download("path"));
    }

}
