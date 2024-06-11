package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }
    
    @Test
    public void testUpload() {
        dummyPayloadStorage.upload(null, null, 0);
        // Add assert statements to test the upload method
    }
    
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download(null));
        // Add assert statements to test the download method
    }
}