package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private ExternalPayloadStorage mockExternalPayloadStorage;

    @Before
    public void setup() {
        mockExternalPayloadStorage = Mockito.mock(ExternalPayloadStorage.class);
        dummyPayloadStorage = new DummyPayloadStorage();
        when(dummyPayloadStorage.getLocation(any(Operation.class), eq(PayloadType.PAYLOAD), anyString()))
                .thenCallRealMethod();
        when(dummyPayloadStorage.getLocation(any(Operation.class), eq(PayloadType.PAYLOAD), anyString()))
                .thenReturn(mock(ExternalStorageLocation.class));
        doNothing().when(dummyPayloadStorage).upload(anyString(), any(InputStream.class), anyLong());
        doReturn(Mockito.mock(InputStream.class)).when(dummyPayloadStorage).download(anyString());
    }

    @Test
    public void testGetLocation() {
        Operation operation = Mockito.mock(Operation.class);
        PayloadType payloadType = PayloadType.PAYLOAD;
        String path = "testPath";

        ExternalStorageLocation location = dummyPayloadStorage.getLocation(operation, payloadType, path);
        assertNull(location);
    }

    @Test
    public void testUpload() {
        String path = "testPath";
        byte[] payload = new byte[10];
        long payloadSize = 10L;

        InputStream inputStream = new ByteArrayInputStream(payload);
        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        verify(mockExternalPayloadStorage, never()).upload(anyString(), any(InputStream.class), anyLong());
    }

    @Test
    public void testDownload() {
        String path = "testPath";
        byte[] expectedPayload = new byte[10];
        InputStream expectedInputStream = new ByteArrayInputStream(expectedPayload);

        when(dummyPayloadStorage.download(path)).thenReturn(expectedInputStream);

        InputStream actualInputStream = dummyPayloadStorage.download(path);
        assertSame(expectedInputStream, actualInputStream);

        verify(mockExternalPayloadStorage, never()).download(anyString());
    }
}