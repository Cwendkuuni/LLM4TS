package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage.OperationType;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation("testOperationId", OperationType.START, null, null, null, null, null);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(expectedResult);
    }

    @Test
    public void testUploadDoesNotDoAnything() {
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

        InputStream expectedResult = dummyPayloadStorage.download(path);
        assertNull(expectedResult);
    }

    @Test
    public void testDownloadReturnsEmptyInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = dummyPayloadStorage.download(path);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertSame(expectedResult, inputStream);
    }

    @Test
    public void testDownloadReturnsMockedInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream expectedResult = mock(InputStream.class);

        InputStream mockedInputStream = mock(InputStream.class);
        when(mockedInputStream).thenReturn(expectedResult);
        when(dummyPayloadStorage.download(path)).thenReturn(mockedInputStream);

        InputStream result = dummyPayloadStorage.download(path);
        assertSame(expectedResult, result);
    }
}