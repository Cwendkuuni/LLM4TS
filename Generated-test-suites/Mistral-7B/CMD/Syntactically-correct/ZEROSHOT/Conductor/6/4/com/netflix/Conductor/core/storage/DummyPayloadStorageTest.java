package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
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
    public void testUploadDoesNotDoAnything() throws IOException {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[1024]);
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // Nothing should happen
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);

        assertNull(inputStream);
    }
}