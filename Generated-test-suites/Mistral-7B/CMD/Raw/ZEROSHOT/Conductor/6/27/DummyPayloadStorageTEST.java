package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUpload() {
        String path = "testPath";
        byte[] payload = new byte[1024];
        long payloadSize = 1024L;
        InputStream inputStream = new ByteArrayInputStream(payload);
        dummyPayloadStorage.upload(path, inputStream, payloadSize);
        // As this is a dummy implementation, no verification is required
    }

    @Test
    public void testDownload() {
        String path = "testPath";
        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);
        assertEquals(expectedResult, actualResult);
    }
}