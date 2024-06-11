/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "dummyPath");
        assertNull(result);
    }

    @Test
    public void testUpload() {
        InputStream dummyPayload = new ByteArrayInputStream("dummyPayload".getBytes());
        dummyPayloadStorage.upload("dummyPath", dummyPayload, 12);
        // No assertions needed as method does not perform any operations
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download("dummyPath");
        assertNull(result);
    }
}