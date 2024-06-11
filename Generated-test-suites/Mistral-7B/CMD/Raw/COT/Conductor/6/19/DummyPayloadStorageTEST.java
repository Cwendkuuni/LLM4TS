package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.operations.Operation;
import com.netflix.conductor.common.payload.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualLocation);
        assertNull(expectedLocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadThrowsUnsupportedOperationException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedInputStream = null;
        InputStream actualInputStream = dummyPayloadStorage.download(path);

        assertNull(actualInputStream);
        assertNull(expectedInputStream);
    }

    @Test(expected = IOException.class)
    public void testDownloadThrowsIOException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = mock(InputStream.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        when(inputStream.readAllBytes()).thenThrow(new IOException());

        dummyPayloadStorage.download(path) = inputStream;
    }
}