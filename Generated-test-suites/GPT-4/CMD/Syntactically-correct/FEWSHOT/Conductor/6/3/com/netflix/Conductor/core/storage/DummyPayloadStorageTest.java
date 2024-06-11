package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Test for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, ""));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.GET, ExternalPayloadStorage.PayloadType.TASK_INPUT, "test-path"));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.PUT, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "test-path"));
    }

    @Test
    public void testUpload() {
        // DummyPayloadStorage.upload has no implementation to test, it's a no-op function
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("test-path"));
        assertNull(dummyPayloadStorage.download(""));
    }
}