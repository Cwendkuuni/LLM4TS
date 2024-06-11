package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayload Storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationShouldReturnNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadShouldDoNothing() {
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 10L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadShouldReturnNull() {
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);
        assertNull(inputStream);
    }
}