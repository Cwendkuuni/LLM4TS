package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage testInstance = new DummyPayloadStorage();
        assertNull(testInstance.getLocation(null, null, null));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage testInstance = new DummyPayloadStorage();
        testInstance.upload(null, null, 0);
        assertTrue(true);
    }
    
    @Test
    public void testDownload() {
        DummyPayloadStorage testInstance = new DummyPayloadStorage();
        assertNull(testInstance.download(null));
    }
}