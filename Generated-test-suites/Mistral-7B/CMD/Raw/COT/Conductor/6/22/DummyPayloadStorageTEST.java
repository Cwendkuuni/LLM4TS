package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No assertion needed as the method is void
    }

    @Test(expected = IOException.class)
    public void testDownloadNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;

        InputStream result = dummyPayloadStorage.download(path);

        // Assertion to check if IOException is thrown
    }

    @Test
    public void testDownloadEmptyInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNull(inputStream);
    }

    @Test
    public void testDownloadNonEmptyInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        byte[] inputData = "testData".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputData);

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNotNull(inputStream);
    }
}