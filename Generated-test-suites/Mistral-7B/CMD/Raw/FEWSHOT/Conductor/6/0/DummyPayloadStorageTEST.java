package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import org.junit.Before;
import org.junit.Test;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage payloadStorage;

    @Before
    public void setup() {
        payloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(payloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() {
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 1024L;

        payloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        assertNull(payloadStorage.download(path));
    }
}