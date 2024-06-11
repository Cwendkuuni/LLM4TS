package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        dummyPayloadStorage.upload(null, null, 0);
        assertNull(dummyPayloadStorage.download(null));
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download(null);
        assertNull(result);
    }
}