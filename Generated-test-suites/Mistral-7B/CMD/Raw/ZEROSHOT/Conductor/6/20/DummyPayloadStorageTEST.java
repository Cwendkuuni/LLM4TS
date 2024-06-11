package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "test-path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));

        verifyNoInteractions(operation);
    }

    @Test
    public void testUpload() {
        String path = "test-path";
        InputStream inputStream = mock(InputStream.class);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        verifyNoInteractions(inputStream);
    }

    @Test
    public void testDownload() {
        String path = "test-path";

        assertNull(dummyPayloadStorage.download(path));
    }
}