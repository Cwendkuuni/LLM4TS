package com.netflix.conductor.core.storage;

import org.junit.Test;
import org.junit.Assert;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import java.io.InputStream;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, 
                ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                "/dummy/path");
                
        Assert.assertNull(location);
    }
    
    @Test
    public void testUpload() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                return -1;  // end of stream
            }
        };
        
        dummyPayloadStorage.upload("/dummy/path", inputStream, 100);
        
        // This is always a no-op, no exceptions should be thrown
    }
    
    @Test
    public void testDownload() {
        InputStream downloadedStream = dummyPayloadStorage.download("/dummy/path");
        
        Assert.assertNull(downloadedStream);
    }
}