package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();

        assertNull(payloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024;

        payloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        assertNull(payloadStorage.download(path));
    }
}