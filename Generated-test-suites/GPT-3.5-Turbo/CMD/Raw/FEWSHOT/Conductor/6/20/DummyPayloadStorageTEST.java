package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.GET, PayloadType.TASK_INPUT, "dummy/path");
        
        assertNull(location);
    }
    
    @Test
    public void testUploadDoesNotThrowException() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream("dummy payload".getBytes());
        
        dummyStorage.upload("dummy/path", inputStream, 12);
    }
    
    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        
        InputStream inputStream = dummyStorage.download("dummy/path");
        
        assertNull(inputStream);
    }
}