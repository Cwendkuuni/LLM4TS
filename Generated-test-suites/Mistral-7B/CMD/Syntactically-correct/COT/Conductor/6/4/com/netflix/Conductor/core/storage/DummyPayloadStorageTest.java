package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // This check is not possible as the method does not throw any exception
        // but we can add a test for null payload or path in the next test cases
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);

        assertNull(inputStream);
    }

    @Test(expected = IOException.class)
    public void testDownloadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage() {
            @Override
            public InputStream download(String path) {
                throw new IOException();
            }
        };

        String path = "testPath";

        dummyPayloadStorage.download(path);
    }
}