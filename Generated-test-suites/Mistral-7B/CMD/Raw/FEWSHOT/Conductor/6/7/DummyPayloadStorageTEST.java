package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
        // no verification needed as the method has no side effects
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "test-path";

        InputStream expectedNullInput = null;
        InputStream actualInput = dummyPayloadStorage.download(path);

        assertEquals(expectedNullInput, actualInput);
    }
}