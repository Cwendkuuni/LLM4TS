package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNothing() throws IOException {
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream mockedInputStream = mock(InputStream.class);
        when(mockedInputStream.available()).thenReturn(0);

        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
    }
}