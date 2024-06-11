package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream payload = null;
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Check if the method call does not throw any exception
        // As the method is empty, we cannot check for any result or edge cases
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        dummyPayloadStorage.download(path);
    }
}