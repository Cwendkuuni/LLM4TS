package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    private static final DummyPayloadStorage DUMMY_PAYLOAD_STORAGE = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = DUMMY_PAYLOAD_STORAGE.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadThrowsException() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1L;

        DUMMY_PAYLOAD_STORAGE.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream result = DUMMY_PAYLOAD_STORAGE.download(path);

        assertEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadThrowsException() {
        String path = "testPath";

        DUMMY_PAYLOAD_STORAGE.download(path);
    }
}