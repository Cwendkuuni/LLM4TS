
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "");
        assertNull(location);
    }
    
    @Test
    public void uploadTest() {
        dummyPayloadStorage.upload("", null, 0);
        // No exception thrown verifies successful upload as upload in DummyPayloadStorage does nothing.
    }
    
    @Test
    public void downloadTest() {
        InputStream stream = dummyPayloadStorage.download("");
        assertNull(stream);
    }
}
