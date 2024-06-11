package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        byte[] payload = "testPayload".getBytes();
        long payloadSize = payload.length;

        dummyPayloadStorage.upload(path, new ByteArrayInputStream(payload), payloadSize);

        // since this is a dummy implementation, there's no way to verify if the upload was successful
    }

    @Test(expected = IOException.class)
    public void testDownloadWithException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);
        throw new IOException("Expected exception when downloading");
    }

    @Test
    public void testDownloadNullInput() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);
        assertNull(inputStream);
    }

    @Test
    public void testDownloadEmptyInput() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload = "testPayload".getBytes();
        InputStream inputStream = new ByteArrayInputStream(payload);

        dummyPayloadStorage.upload(path, inputStream, payload.length);
        InputStream testInputStream = dummyPayloadStorage.download(path);

        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = new byte[1024];

        int bytesRead = -1;
        while ((bytesRead = testInputStream.read(actualOutput, 0, actualOutput.length)) != -1) {
            System.arraycopy(actualOutput, 0, expectedOutput, 0, bytesRead);
        }

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDownloadPartialInput() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] payload = "testPartialPayload".getBytes();
        InputStream inputStream = new ByteArrayInputStream(payload);

        dummyPayloadStorage.upload(path, inputStream, payload.length);
        InputStream testInputStream = dummyPayloadStorage.download(path);

        byte[] expectedOutput = new byte[]{'t', 'e', 's', 't', 'P', 'a', 'r', 't', 'i', 'a', 'l', ' ', 'p', 'a', 'r', 't', 'i', 'a', 'l'};
        byte[] actualOutput = new byte[1024];

        int bytesRead = -1;
        while ((bytesRead = testInputStream.read(actualOutput, 0, actualOutput.length)) != -1) {
            System.arraycopy(actualOutput, 0, expectedOutput, 0, bytesRead);
        }

        assertArrayEquals(expectedOutput, actualOutput);
    }
}