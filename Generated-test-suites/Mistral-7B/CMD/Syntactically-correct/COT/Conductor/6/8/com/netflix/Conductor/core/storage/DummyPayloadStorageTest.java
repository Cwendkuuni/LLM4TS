package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as method is void
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDownloadReturnsExpectedInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        byte[] testData = "Test Data".getBytes();

        // Setup mock InputStream
        InputStream inputStreamMock = new MockInputStream(testData);
        InputStream expectedResult = inputStreamMock;

        InputStream actualResult = dummyPayloadStorage.download(path);

        assertSame(expectedResult, actualResult);
    }
}