package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_whenOperationIsNull_thenReturnNull() {
        Operation operation = null;
        PayloadType payloadType = PayloadType.PIPELINE_DEFINITION;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testGetLocation_whenPayloadTypeIsNull_thenReturnNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = null;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testGetLocation_whenPathIsNull_thenReturnNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.PIPELINE_DEFINITION;
        String path = null;

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload_whenPayloadIsEmpty_thenDoNothing() {
        String path = "test-path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = IOException.class)
    public void testDownload_whenPathIsNull_thenThrowException() {
        String path = null;
        InputStream expectedException = mock(InputStream.class);
        when(expectedException.available()).thenReturn(1);

        dummyPayloadStorage.download(path);
    }

    @Test
    public void testDownload_whenInputStreamIsNull_thenReturnNull() {
        String path = "test-path";
        InputStream inputStream = null;

        assertNull(dummyPayloadStorage.download(path));
    }
}