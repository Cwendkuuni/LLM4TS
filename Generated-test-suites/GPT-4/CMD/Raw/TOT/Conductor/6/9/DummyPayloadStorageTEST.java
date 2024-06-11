// Copyright 2022 Netflix, Inc.

package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import org.junit.Test;

/**
 * Tests the dummy implementation of {@link ExternalPayloadStorage}
 */
public class DummyPayloadStorageTest {

    /**
     * Test when no external payload is configured
     */
    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        try {
            InputStream payload = null;
            storage.upload("path", payload, 10L);
        } catch (Exception e) {
            fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream result = storage.download("path");
        assertNull(result);
    }
}