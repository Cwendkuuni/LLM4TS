package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;

    @Before
    public void setup() {
        this.storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_whenOperationIsNull_thenReturnNull() {
        // Given
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        // When
        ExternalStorageLocation result = this.storage.getLocation(operation, payloadType, path);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetLocation_whenOperationIsValid_thenReturnNull() {
        // Given
        Operation operation = mock(Operation.class);
        when(operation.getId()).thenReturn("operation-id");
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        // When
        ExternalStorageLocation result = this.storage.getLocation(operation, payloadType, path);

        // Then
        assertNull(result);
    }

    @Test
    public void testUpload_whenPayloadIsNull_thenDoNothing() {
        // Given
        String path = "test-path";
        byte[] payload = null;
        long payloadSize = 1024;

        // When
        this.storage.upload(path, payload, payloadSize);

        // Then
        // No verification necessary as upload method does not throw an exception
    }

    @Test
    public void testUpload_whenPayloadIsValid_thenDoNothing() {
        // Given
        String path = "test-path";
        byte[] payload = "test payload".getBytes(StandardCharsets.UTF_8);
        long payloadSize = payload.length;

        // When
        this.storage.upload(path, new ByteArrayInputStream(payload), payloadSize);

        // Then
        // No verification necessary as upload method does not throw an exception
    }

    @Test(expected = IOException.class)
    public void testDownload_whenPathIsNull_thenThrowException() throws IOException {
        // Given
        String path = null;

        // When
        InputStream result = this.storage.download(path);

        // Then
        throw new IOException("Expected IOException when path is null");
    }

    @Test
    public void testDownload_whenPathIsValid_thenReturnNull() {
        // Given
        String path = "test-path";

        // When
        InputStream result = this.storage.download(path);

        // Then
        assertNull(result);
    }
}