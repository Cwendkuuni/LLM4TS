package com.netflix.conductor.core.storage;

import java.io.InputStream;
import static org.junit.Assert.assertNull;
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
        ExternalStorageLocation storageLocation = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test/path");
        assertNull(storageLocation);
    }

    @Test
    public void testUpload() {
        try (InputStream emptyStream = InputStream.nullInputStream()) {
            dummyPayloadStorage.upload("test/path", emptyStream, 0L);
        } catch (Exception ex) {
            throw new AssertionError("Upload operation resulted in an error.", ex);
        }
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("test/path"));
    }
}