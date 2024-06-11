package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 123L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as the method is empty.
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream inputStream = mock(InputStream.class);
        when(dummyPayloadStorage.download(path)).thenReturn(inputStream);

        assertNull(dummyPayloadStorage.download(path));
    }
}