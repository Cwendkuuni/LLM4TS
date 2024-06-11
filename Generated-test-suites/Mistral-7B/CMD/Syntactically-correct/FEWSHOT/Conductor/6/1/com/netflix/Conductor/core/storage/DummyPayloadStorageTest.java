package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "/test/path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "/test/path";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as the method is empty
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = "/test/path";

        assertNull(dummyPayloadStorage.download(path));
    }
}