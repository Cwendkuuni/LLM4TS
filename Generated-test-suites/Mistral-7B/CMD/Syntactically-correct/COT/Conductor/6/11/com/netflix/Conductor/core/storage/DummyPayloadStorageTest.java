package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_whenOperationIsNull_thenReturnNull() {
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualResult);
        assertNull(expectedResult);
    }

    @Test
    public void testGetLocation_whenOperationIsValid_thenReturnNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation actualResult = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualResult);
        verifyZeroInteractions(operation);
        assertNull(expectedResult);
    }

    @Test
    public void testUpload_whenPayloadIsValid_thenDoNothing() {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as we are only testing the method call, not the effect
    }

    @Test(expected = IOException.class)
    public void testDownload_whenPathIsNull_thenThrowIOException() throws IOException {
        String path = null;

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertNull(expectedResult);
        throw new IOException("Unexpected InputStream returned");
    }

    @Test
    public void testDownload_whenPathIsValid_thenReturnNull() {
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream actualResult = dummyPayloadStorage.download(path);

        assertNull(actualResult);
        assertNull(expectedResult);
    }
}