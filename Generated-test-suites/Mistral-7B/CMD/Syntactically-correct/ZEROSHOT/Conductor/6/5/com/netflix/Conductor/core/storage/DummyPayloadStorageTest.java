package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link DummyPayloadStorage}
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        Operation operation = Mockito.mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedLocation = Mockito.mock(ExternalStorageLocation.class);
        Mockito.when(expectedLocation.getPath()).thenReturn(path);

        ExternalStorageLocation actualLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(actualLocation);
        Mockito.verify(expectedLocation).getPath();
        Mockito.verifyNoMoreInteractions(expectedLocation);
    }

    @Test
    public void testUpload() {
        String path = "testPath";
        InputStream payload = Mockito.mock(InputStream.class);
        long payloadSize = 100L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        Mockito.verifyNoInteractions(payload);
    }

    @Test(expected = IOException.class)
    public void testDownload() {
        String path = "testPath";

        InputStream expectedInputStream = new ByteArrayInputStream(new byte[0]);
        Mockito.when(dummyPayloadStorage.download(path)).thenReturn(expectedInputStream);

        InputStream actualInputStream = dummyPayloadStorage.download(path);

        assertNull(actualInputStream);
        Mockito.verify(dummyPayloadStorage).download(path);
        Mockito.verify(expectedInputStream).close();

        // Throws IOException to test expected exception
        actualInputStream.read();
    }
}