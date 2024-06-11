package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    /**
     * Test method for getLocation(Operation, PayloadType, String)
     */
    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "path";
        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(actualLocation);
        // Edge case: Test with null input parameters
        assertNull(dummyPayloadStorage.getLocation(null, payloadType, path));
        assertNull(dummyPayloadStorage.getLocation(operation, null, path));
        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, null));
    }

    /**
     * Test method for upload(String, InputStream, long)
     */
    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "path";
        InputStream payload = null;
        long payloadSize = 10;
        dummyPayloadStorage.upload(path, payload, payloadSize);
        // Check if the method does nothing
        // Edge case: Test with null input parameters
        InputStream nullPayload = null;
        dummyPayloadStorage.upload(path, nullPayload, payloadSize);
        dummyPayloadStorage.upload(path, payload, -1);
    }

    /**
     * Test method for download(String)
     */
    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "path";
        InputStream expectedInputStream = null;
        InputStream actualInputStream = dummyPayloadStorage.download(path);
        assertNull(actualInputStream);
        // Edge case: Test with null input parameter
        assertNull(dummyPayloadStorage.download(null));
    }
}