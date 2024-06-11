
/*
 * Copyright 2022 Netflix, Inc.
 */

package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;


public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream dummyPayload = new ByteArrayInputStream("dummyPayload".getBytes());
        
        try {
            storage.upload("dummyPath", dummyPayload, longSize);
            assertTrue(true); 
        } catch (Exception e) {
            fail("Exception should not have been thrown.");
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        assertNull(storage.download("dummyPath"));
    }
}
