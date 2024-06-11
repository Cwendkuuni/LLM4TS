package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummy_path");
        
        assertNull(location);
    }
    
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        InputStream inputStream = new ByteArrayInputStream("test payload".getBytes());
        dummyPayloadStorage.upload("dummy_path", inputStream, 12L);
        
        // Add assertions here if required
    }
    
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        
        InputStream downloadedPayload = dummyPayloadStorage.download("dummy_path");
        
        assertNull(downloadedPayload);
    }
}