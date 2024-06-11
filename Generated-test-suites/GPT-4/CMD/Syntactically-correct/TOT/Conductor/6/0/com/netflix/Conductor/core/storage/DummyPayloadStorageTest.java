package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = InputStream.nullInputStream();
        // As the upload method doesn't return anything, we are just checking if no exception is thrown
        try {
           dummyPayloadStorage.upload("path", payload, 0);
        } catch(Exception e){
           fail("No exception should be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream stream = dummyPayloadStorage.download("path");
        assertNull(stream);
    }
}