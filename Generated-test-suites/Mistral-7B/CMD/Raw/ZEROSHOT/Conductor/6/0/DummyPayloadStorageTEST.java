package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.test.TestFiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DummyPayloadStorageTest {

    private static final String MOCK_PATH = "mockPath";

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;

        assertNull(payloadStorage.getLocation(operation, payloadType, MOCK_PATH));
    }

    @Test
    public void testUploadDoesNotDoAnything() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = MOCK_PATH;
        InputStream payload = TestFiles.getResourceAsStream("/testData.json");
        long payloadSize = 1024L;

        payloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = MOCK_PATH;

        InputStream expected = null;
        when(payloadStorage.download(path)).thenReturn(expected);

        InputStream actual = payloadStorage.download(path);

        assertNull(actual);
    }

    @Test
    public void testUploadAndDownloadUsingMockedPayload() throws IOException {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = MOCK_PATH;
        String expectedJson = "{\"key\":\"value\"}";
        InputStream payload = new ByteArrayInputStream(expectedJson.getBytes(StandardCharsets.UTF_8));
        long payloadSize = expectedJson.getBytes(StandardCharsets.UTF_8).length;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Upload
        payloadStorage.upload(path, payload, payloadSize);

        // Download
        InputStream downloadedPayload = payloadStorage.download(path);
        byte[] downloadedBytes = toByteArray(downloadedPayload);
        String downloadedJson = new String(downloadedBytes, StandardCharsets.UTF_8);

        // Verify the uploaded and downloaded payloads are the same
        assertExpectedAndDownloadedPayloadsAreEqual(expectedJson, downloadedJson);
    }

    private void assertExpectedAndDownloadedPayloadsAreEqual(String expectedJson, String downloadedJson) {
        if (!expectedJson.equals(downloadedJson)) {
            throw new AssertionError("Expected and downloaded payloads are different");
        }
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}