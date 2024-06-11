package com.netflix.conductor.core.storage;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DummyPayloadStorageTest {
    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void getLocationShouldReturnNull() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void uploadShouldNotThrowException() {
        try {
            dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Upload method threw an exception");
        }
    }

    @Test
    public void downloadShouldReturnNull() {
        assertNull(dummyPayloadStorage.download(null));
    }
}