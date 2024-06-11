package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "testPath";
        InputStream payload = Mockito.mock(InputStream.class);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        Mockito.verifyNoInteractions(payload);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        assertNull(dummyPayloadStorage.download(path));
    }
}