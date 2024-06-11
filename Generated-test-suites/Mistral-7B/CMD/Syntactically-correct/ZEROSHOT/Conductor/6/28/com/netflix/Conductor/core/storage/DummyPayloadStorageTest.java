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
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.PayloadType;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private ExternalStorageLocation externalStorageLocation;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
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
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }

    @Test
    public void testDownloadShouldReturnNull() {
        String path = "testPath";

        InputStream inputStream = Mockito.mock(InputStream.class);
        when(dummyPayloadStorage.download(path)).thenReturn(inputStream);

        assertNull(dummyPayloadStorage.download(path));
    }
}