package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage payloadStorage;
    private ExternalStorageLocation storageLocation;
    private byte[] payload = "Dummy Payload".getBytes(StandardCharsets.UTF_8);
    private InputStream inputStream = new ByteArrayInputStream(payload);

    @Before
    public void setup() {
        payloadStorage = new DummyPayloadStorage();
        storageLocation = mock(ExternalStorageLocation.class);
    }

    @Test
    public void testGetLocationReturnsNull() {
        when(storageLocation.getPath()).thenReturn("testPath");
        assertNull(payloadStorage.getLocation(new Operation(), PayloadType.FILE, "testPath"));
    }

    @Test
    public void testUploadDoesNothing() throws IOException {
        payloadStorage.upload("testPath", inputStream, payload.length);
    }

    @Test
    public void testDownloadReturnsNull() {
        when(storageLocation.getPath()).thenReturn("testPath");
        assertNull(payloadStorage.download("testPath"));
    }
}