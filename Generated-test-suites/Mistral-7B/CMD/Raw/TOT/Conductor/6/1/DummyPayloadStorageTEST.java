package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = Mockito.mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        ExternalStorageLocation expectedResult = Mockito.mock(ExternalStorageLocation.class);
        when(expectedResult.getPath()).thenReturn(path);

        ExternalStorageLocation result = this.dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull("Expected null but got: " + result, result);
        verify(this.dummyPayloadStorage, times(1)).getLocation(operation, payloadType, path);
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream("test-data".getBytes(StandardCharsets.UTF_8));
        long payloadSize = 10;

        this.dummyPayloadStorage.upload(path, payload, payloadSize);

        verify(this.dummyPayloadStorage, times(1)).upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "test-path";

        InputStream expectedResult = Mockito.mock(InputStream.class);
        when(expectedResult.available()).thenReturn(0);

        InputStream result = this.dummyPayloadStorage.download(path);

        assertNull("Expected null but got: " + result, result);
        verify(this.dummyPayloadStorage, times(1)).download(path);
    }

    @Test(expected = IOException.class)
    public void testDownloadThrowsIOException() {
        String path = "test-path";

        doThrow(new IOException("Test exception")).when(this.dummyPayloadStorage).download(path);

        this.dummyPayloadStorage.download(path);
    }
}