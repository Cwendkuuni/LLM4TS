package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "path";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadNull() {
        String path = "path";
        InputStream inputStream = mock(InputStream.class);
        long payloadSize = 1L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // No implementation, so no checks needed
    }

    @Test
    public void testDownloadNull() {
        String path = "path";

        InputStream inputStream = dummyPayloadStorage.download(path);

        assertNull(inputStream);
    }
}