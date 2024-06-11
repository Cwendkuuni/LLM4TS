package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() throws Exception {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, ""));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.ADD, ExternalPayloadStorage.PayloadType.S3, "/path"));
    }

    @Test
    public void testUpload() {
        // Here we simply call the method, as it's expected to do nothing, we have no assertions to make
        dummyPayloadStorage.upload("", null, 0);
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download(""));
        assertNull(dummyPayloadStorage.download("/path"));
    }
}