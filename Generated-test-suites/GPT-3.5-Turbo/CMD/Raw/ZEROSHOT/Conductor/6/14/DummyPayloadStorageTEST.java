package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {
    
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.get, PayloadType.json, "testPath");
        assertNull(location);
    }
    
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        dummyPayloadStorage.upload("testPath", null, 0L);
        // No exceptions expected
    }
    
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream inputStream = dummyPayloadStorage.download("testPath");
        assertNull(inputStream);
    }
}