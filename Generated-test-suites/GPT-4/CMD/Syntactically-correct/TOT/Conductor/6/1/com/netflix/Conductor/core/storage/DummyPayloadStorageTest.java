package com.netflix.conductor.core.storage;
import static org.junit.Assert.*;

import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.Operation;
import com.netflix.conductor.core.storage.PayloadType;

import java.io.InputStream;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.ERROR, "/test/");
        assertNull(location);
    }
  
    @Test
    public void testUpload() {
        // nothing to verify as the method has no effect
        dummyPayloadStorage.upload("/test/", null, 0);
    }
  
    @Test
    public void testDownload() {
        InputStream stream = dummyPayloadStorage.download("/test/");
        assertNull(stream);
    }

}