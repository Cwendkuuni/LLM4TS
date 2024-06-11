package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = mock(InputStream.class);
        long payloadSize = 10L;

        try (MockedStatic<System> systemMockedStatic = Mockito.mockStatic(System.class)) {
            when(systemMockedStatic.getCurrentInput())
                    .thenReturn(inputStream);
            dummyPayloadStorage.upload(path, inputStream, payloadSize);
            systemMockedStatic.verifyNoInteractions();
        }
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        assertNull(dummyPayloadStorage.download(path));
    }
}