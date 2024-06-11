
package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocationReturnsNull() {
        assertNull(dummyPayloadStorage.getLocation(Operation.WRITE, PayloadType.TASK_INPUT, "dummyPath"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationThrowsNullPointerExceptionForNullOperation() {
        dummyPayloadStorage.getLocation(null, PayloadType.TASK_INPUT, "dummyPath");
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetLocationThrowsNullPointerExceptionForNullPayloadType() {
        dummyPayloadStorage.getLocation(Operation.WRITE, null, "dummyPath");
    }
    
    @Test
    public void testUpload() {
        // Testing that no exception is thrown
        InputStream dummyPayload = new ByteArrayInputStream("dummyPayload".getBytes());
        dummyPayloadStorage.upload("dummyPath", dummyPayload, dummyPayload.available());
    }
    
    @Test(expected = NullPointerException.class)
    public void testUploadThrowsNullPointerExceptionForNullPayload() {
        dummyPayloadStorage.upload("dummyPath", null, 0);
    }    

    @Test
    public void testDownloadReturnsNull() {
        assertNull(dummyPayloadStorage.download("dummyPath"));
    }    

    @Test(expected = NullPointerException.class)
    public void testDownloadThrowsNullPointerExceptionForNullPath() {
        dummyPayloadStorage.download(null);
    }
}
