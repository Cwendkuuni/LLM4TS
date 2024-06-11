
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;
import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;


public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "testPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        InputStream payload = new ByteArrayInputStream("Test payload".getBytes(StandardCharsets.UTF_8));
        dummyPayloadStorage.upload("testPath", payload, payload.available());
        // No exception means success as the upload method doesn't return anything
    }

    @Test
    public void testDownload() {
        InputStream downloadStream = dummyPayloadStorage.download("testPath");
        assertNull(downloadStream);
    }
}
