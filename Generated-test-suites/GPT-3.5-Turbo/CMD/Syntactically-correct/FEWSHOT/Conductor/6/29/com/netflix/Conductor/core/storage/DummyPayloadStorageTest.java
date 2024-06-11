package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.PayloadType;

public class DummyPayloadStorageTest {
    
    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
    
    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(null, null, null));
    }
    
    @Test
    public void testUpload() {
        dummyPayloadStorage.upload(null, new ByteArrayInputStream(new byte[0]), 0);
        // Add assertions if needed for upload method
    }
    
    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download(null));
    }
}