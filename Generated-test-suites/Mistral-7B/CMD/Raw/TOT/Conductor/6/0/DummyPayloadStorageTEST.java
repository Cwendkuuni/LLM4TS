package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.payloads.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;
import com.netflix.conductor.core.storage.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.FILE;
        String path = "test-path";

        ExternalStorageLocation expectedResult = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(expectedResult);
    }

    @Test
    public void testUploadDoesNothing() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream(new byte[1]);
        long payloadSize = 1;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No assertions needed, as this method does not modify any state
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream expectedResult = dummyPayloadStorage.download(path);
        assertNull(expectedResult);
    }

    @Test
    public void testUploadDownloadRoundtrip() throws IOException {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream(new byte[1]);
        long payloadSize = 1;

        dummyPayloadStorage.upload(path, payload, payloadSize);
        InputStream downloadedPayload = dummyPayloadStorage.download(path);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = downloadedPayload.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] actualResult = outputStream.toByteArray();

        // Compare the uploaded and downloaded payloads
        byte[] expectedResult = new byte[1];
        for (int i = 0; i < payload.available(); i++) {
            expectedResult[i] = (byte) payload.read();
        }
        assertArrayEquals(expectedResult, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        dummyPayloadStorage.delete(path);
    }
}