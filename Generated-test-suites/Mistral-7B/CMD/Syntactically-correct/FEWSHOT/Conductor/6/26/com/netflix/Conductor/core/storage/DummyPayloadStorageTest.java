package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        assertNull(dummyPayloadStorage.download(path));
    }

    @Test
    public void testGetLocationWithMockedInputs() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";
        ExternalStorageLocation expectedLocation = new ExternalStorageLocation("test-bucket", "test-key");

        when(operation.getExternalStorageLocation(ArgumentMatchers.eq(payloadType))).thenReturn(expectedLocation);

        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedLocation, actualLocation);
    }
}