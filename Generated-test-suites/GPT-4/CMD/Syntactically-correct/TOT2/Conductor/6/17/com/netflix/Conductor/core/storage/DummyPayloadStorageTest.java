package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp(){
        dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = 
                   dummyPayloadStorage.getLocation(
                       ExternalPayloadStorage.Operation.READ, 
                       ExternalPayloadStorage.PayloadType.TASK_INPUT, 
                       "sample/path");
        assertNull(location);
    }
    
    @Test
    public void uploadTest() {
        InputStream fakeInputStream = new InputStream() {
            @Override
            public int read() {
                return -1;  // end of stream
            }
        };
        
        dummyPayloadStorage.upload("sample/path", fakeInputStream, 0L);
        
        // there aren't any actual state changes in DummyPayloadStorage to check,
        // but we can ensure the method ran without throwing exceptions  
        // by getting here without errors
    }
    
    @Test
    public void downloadTest() {
        InputStream result = dummyPayloadStorage.download("sample/path");
        assertNull(result);
    }
}