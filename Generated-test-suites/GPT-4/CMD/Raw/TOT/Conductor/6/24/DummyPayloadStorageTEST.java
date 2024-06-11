/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        ExternalStorageLocation esl = dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.HTTP_OUTPUT, "/path");
        assertNull(esl);
    }

    @Test
    public void testUpload() {
        // Execute the method being tested
        dummyPayloadStorage.upload("/path", null, 0);

        // There is no state to assert here because upload() doesn't do anything
        // We just verify that no exceptions were thrown
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download("/path");
        assertNull(result);
    }
}