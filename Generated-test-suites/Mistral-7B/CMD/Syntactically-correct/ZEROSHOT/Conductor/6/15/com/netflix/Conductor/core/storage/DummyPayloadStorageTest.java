package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No action is expected
        // Therefore, no assertions needed
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertSame(expectedResult, actualResult);
    }
}