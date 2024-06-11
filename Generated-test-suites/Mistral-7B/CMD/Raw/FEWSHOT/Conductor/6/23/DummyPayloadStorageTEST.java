package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

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
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // No verification needed as the method is empty
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedNullInput = null;

        assertNull(dummyPayloadStorage.download(path));
        assertNull(dummyPayloadStorage.download(path)); // Verifying that the method is called twice with the same argument returns the same result

        InputStream inputStreamMock = mock(InputStream.class);
        when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]); // Empty byte array to make sure the downloaded stream is empty

        dummyPayloadStorage = new DummyPayloadStorage() {
            @Override
            public InputStream download(String path) {
                return inputStreamMock;
            }
        };

        assertNull(dummyPayloadStorage.download(path)); // Verifying that the method returns null when the downloaded stream is empty
    }
}