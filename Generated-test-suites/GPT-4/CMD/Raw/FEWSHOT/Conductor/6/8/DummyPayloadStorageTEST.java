package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "path"));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        // As the implementation of the method is empty, we can't really test anything here
        dummyPayloadStorage.upload("path", null, 0);
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}