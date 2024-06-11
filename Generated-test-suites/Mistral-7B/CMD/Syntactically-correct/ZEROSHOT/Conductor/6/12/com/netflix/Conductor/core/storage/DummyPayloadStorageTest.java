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

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage payloadStorage;

    @Before
    public void setup() {
        payloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(payloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() throws IOException {
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 10L;

        // Mock the getLocation method to return null, as the DummyPayloadStorage implementation does
        Operation operation = mock(Operation.class);
        when(operation.getId()).thenReturn("testOperationId");
        when(payloadStorage.getLocation(operation, PayloadType.JSON, path)).thenReturn(null);

        payloadStorage.upload(path, payload, payloadSize);

        // The upload method does not throw any exceptions, so no assertions are needed
    }

    @Test
    public void testDownloadReturnsNull() throws IOException {
        String path = "testPath";

        InputStream expectedInputStream = new ByteArrayInputStream(new byte[0]);

        when(payloadStorage.download(path)).thenReturn(expectedInputStream);

        InputStream actualInputStream = payloadStorage.download(path);

        assertNull(actualInputStream);
    }
}