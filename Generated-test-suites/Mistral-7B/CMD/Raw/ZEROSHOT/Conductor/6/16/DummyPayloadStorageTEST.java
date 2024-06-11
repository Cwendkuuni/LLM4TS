package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    /**
     * Test method for getLocation
     */
    @Test
    public void testGetLocation() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedLocation, actualLocation);
    }

    /**
     * Test method for upload
     */
    @Test
    public void testUpload() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Nothing to verify here as the method does not have any side effects
    }

    /**
     * Test method for download
     */
    @Test
    public void testDownload() {
        String path = "testPath";

        InputStream expectedInputStream = null;
        InputStream actualInputStream = dummyPayloadStorage.download(path);

        assertEquals(expectedInputStream, actualInputStream);
    }
}