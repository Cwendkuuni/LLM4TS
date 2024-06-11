package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));

        verifyZeroInteractions(operation);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "test-path";
        InputStream inputStream = mock(InputStream.class);
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        verifyZeroInteractions(inputStream);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "test-path";

        assertNull(dummyPayloadStorage.download(path));
    }
}