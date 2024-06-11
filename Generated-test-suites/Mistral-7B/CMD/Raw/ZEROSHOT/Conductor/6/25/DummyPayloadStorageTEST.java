package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;

    @Before
    public void setUp() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(storage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 123L;

        storage.upload(path, payload, payloadSize);

        // No assertions needed as the method does nothing
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream expectedResult = mock(InputStream.class);
        when(storage.download(path)).thenReturn(expectedResult);

        InputStream result = storage.download(path);

        assertNull(result);
    }
}