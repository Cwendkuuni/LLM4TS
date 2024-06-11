
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path"));
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        InputStream inputStream = new ByteArrayInputStream("hello world".getBytes());
        // this method doesn't throw any exception or error so executing this just to complete coverage
        dummyPayloadStorage.upload("path", inputStream, 11);
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}
