
/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    
    @Test
    public void getLocationShouldReturnNull() {
        assertNull(dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.WRITE, 
            ExternalPayloadStorage.PayloadType.TASK_INPUT, 
            "/path/to/payload"
        ));
    }

    @Test
    public void uploadShouldNotThrowException() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        try {
            dummyPayloadStorage.upload("/path/to/payload", input, input.available());
        } catch (Exception e) {
            fail("Exception thrown during upload: " + e.getMessage());
        }
    }

    @Test
    public void downloadShouldReturnNull() {
        assertNull(dummyPayloadStorage.download("/path/to/payload"));
    }
}
