package com.netflix.conductor.core.storage;
import static org.junit.Assert.*;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import com.netflix.conductor.core.storage.DummyPayloadStorage;

import org.junit.Test;

public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void getLocationTest() {
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(null, null, null);
        assertNull(location); // Since the method returns null, check if it is actually null
    }

    @Test
    public void uploadTest() {
        try {
            dummyPayloadStorage.upload(null, null, 0);
        } catch (Exception e) {
            fail("Upload method is not expected to throw an exception, but threw: " + e.getMessage());
        }
    }

    @Test
    public void downloadTest() {
        InputStream stream = dummyPayloadStorage.download(null);
        assertNull(stream); // Since the method returns null, check if it is actually null
    }
}