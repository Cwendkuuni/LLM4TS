package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.TestUtils;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage storage;

    @Before
    public void setUp() {
        this.storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation result = this.storage.getLocation(operation, payloadType, path);
        assertNull(result);
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[10]);
        long expectedPayloadSize = 10L;

        this.storage.upload(path, payload, expectedPayloadSize);

        // Verify no side effect
        verify(storage, times(0)).download(path);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream result = this.storage.download(path);
        assertNull(result);
    }

    @Test
    public void testUploadAndDownloadRoundtrip() throws IOException {
        String path = "testPath";
        byte[] testData = "test data".getBytes();
        InputStream expectedPayload = new ByteArrayInputStream(testData);
        byte[] expectedDownloadedData = TestUtils.asBytes(expectedPayload);

        this.storage.upload(path, expectedPayload, testData.length);
        InputStream actualDownloadedPayload = this.storage.download(path);

        byte[] actualDownloadedData = TestUtils.toBytes(actualDownloadedPayload);
        assertEquals(expectedDownloadedData, actualDownloadedData);
    }
}