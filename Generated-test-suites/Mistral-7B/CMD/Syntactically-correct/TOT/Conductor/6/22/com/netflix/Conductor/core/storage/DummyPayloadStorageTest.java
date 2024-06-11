package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestUtils;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_whenOperationNull_thenReturnNull() {
        // Given
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "test.json";

        // When
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetLocation_whenOperationNotNull_thenReturnNull() {
        // Given
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test.json";

        // When
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        // Then
        assertNull(result);
        verify(operation, times(1)).getRuntimeId();
    }

    @Test
    public void testUpload_whenPayloadIsNull_thenDoNothing() {
        // Given
        String path = "test.json";
        InputStream payload = null;
        long payloadSize = 1024;

        // When
        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Then
        // No verification needed as no interaction with the mocks
    }

    @Test
    public void testDownload_whenPathIsNull_thenReturnNull() {
        // Given
        String path = null;

        // When
        InputStream result = dummyPayloadStorage.download(path);

        // Then
        assertNull(result);
    }

    @Test
    public void testDownload_whenPathIsEmpty_thenReturnNull() {
        // Given
        String path = "";

        // When
        InputStream result = dummyPayloadStorage.download(path);

        // Then
        assertNull(result);
    }

    @Test
    public void testDownload_whenPathIsValid_thenReturnNull() {
        // Given
        String path = "test.json";

        // When
        InputStream result = dummyPayloadStorage.download(path);

        // Then
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testUpload_whenPayloadIsNull_thenThrowException() {
        // Given
        String path = "test.json";
        InputStream payload = null;
        long payloadSize = 1024;

        // When
        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Then
        // Asserting that NullPointerException is thrown
    }

    @Test
    public void testUpload_whenPayloadSizeIsNegative_thenThrowException() {
        // Given
        String path = "test.json";
        InputStream payload = TestUtils.getResourceAsStream("/test.json");
        long payloadSize = -1;

        // When

        // Then
        try {
            dummyPayloadStorage.upload(path, payload, payloadSize);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}