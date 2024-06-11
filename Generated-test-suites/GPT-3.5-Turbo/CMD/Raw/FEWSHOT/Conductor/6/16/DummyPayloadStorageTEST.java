package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test/path"));
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_OUTPUT, "test/path"));
    }
    
    @Test
    public void testUpload() {
        // Since the upload method does not have any implementation, we cannot test its functionality
    }
    
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("test/path"));
        assertNull(dummyPayloadStorage.download(""));
        assertNull(dummyPayloadStorage.download(null));
    }
    
}