package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.core.run.Operation;
import com.netflix.conductor.core.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;
import org.mockito.Mockito;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
    }

    @Test
    public void testUploadDoesNothing() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = Mockito.mock(InputStream.class);
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // verify that nothing has been done
        Mockito.verifyNoInteractions(payload);
    }

    @Test
    public void testDownloadReturnsNull() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }
}