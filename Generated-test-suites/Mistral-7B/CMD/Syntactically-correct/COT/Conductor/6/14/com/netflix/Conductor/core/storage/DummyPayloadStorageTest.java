package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
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
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationWithException() {
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
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as the method is empty
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        dummyPayloadStorage.download(path);
    }
}