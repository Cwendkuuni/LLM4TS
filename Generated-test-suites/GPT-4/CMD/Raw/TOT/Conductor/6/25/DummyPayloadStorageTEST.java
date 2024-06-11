/*
 * Copyright 2022 Netflix, Inc.
 */

package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import java.io.InputStream;
import org.junit.Test;
import org.junit.Before;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

/**
 * Tests for {@link DummyPayloadStorage}.
 */
public class DummyPayloadStorageTest {

    DummyPayloadStorage storage;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
    }
    
    @Test
    public void testGetLocation() {
        ExternalStorageLocation location = storage.getLocation(
                Operation.WRITE, PayloadType.TASK_INPUT, "dummyPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        try {
            storage.upload("dummyPath", null, 0);
        } catch (Exception ex) {
            fail("Upload method failed with exception: " + ex.getMessage());
        }
    }

    @Test
    public void testDownload() {
        InputStream stream = storage.download("dummyPath");
        assertNull(stream);
    }
}