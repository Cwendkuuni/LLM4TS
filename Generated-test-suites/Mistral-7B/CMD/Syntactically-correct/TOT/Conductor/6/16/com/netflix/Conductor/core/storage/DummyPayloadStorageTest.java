package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage storage;
    private ExternalStorageLocation location;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationShouldReturnNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        assertNull(storage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadShouldDoNothing() {
        String path = "test-path";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024;

        storage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadShouldReturnNull() {
        String path = "test-path";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(storage.getLocation(mock(Operation.class), mock(PayloadType.class), path)).thenReturn(location);

        assertNull(storage.download(path));
    }

    @Test(expected = IOException.class)
    public void testDownloadShouldThrowIOExceptionWhenGetLocationReturnsNull() {
        String path = "test-path";

        when(storage.getLocation(mock(Operation.class), mock(PayloadType.class), path)).thenReturn(null);

        storage.download(path);
    }

    @Test
    public void testDownloadShouldReturnExpectedPayload() {
        String path = "test-path";
        byte[] payload = "test payload".getBytes();
        InputStream inputStream = new ByteArrayInputStream(payload);
        ExternalStorageLocation location = new ExternalStorageLocation("test-bucket", "test-key");

        when(storage.getLocation(mock(Operation.class), mock(PayloadType.class), path)).thenReturn(location);
        when(location.getInputStream()).thenReturn(inputStream);

        InputStream actualStream = storage.download(path);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedPayload = "test payload".getBytes();
        int read = -1;
        while ((read = actualStream.read()) != -1) {
            outputStream.write(read);
        }

        assertArrayEquals(expectedPayload, outputStream.toByteArray());
    }
}