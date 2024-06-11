package com.netflix.conductor.core.storage;

import org.junit.Test;  
import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dps = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        assertNull(dps.getLocation(null, null, null));
    }
    
    @Test
    public void testUpload() {
        try {
            dps.upload(null, null, 0);
        } catch (Exception e) {
            fail("Upload method threw an exception: "+ e.getMessage());            
        }
    }
    
    @Test
    public void testDownload() {
        assertNull(dps.download(null));
    }   
}
