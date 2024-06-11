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
import com.netflix.conductor.common.operation.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;
    private Operation operation;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    @Test
    public void testGetLocation_NullLocation_ReturnsNull() {
        assertNull(dummyPayloadStorage.getLocation(operation, PayloadType.FILE, null));
    }

    @Test
    public void testUpload_NullPath_DoesNotThrowException() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            dummyPayloadStorage.upload(null, inputStream, 0);
        } catch (IOException e) {
            // Should not throw an exception
            assert false;
        }
    }

    @Test
    public void testDownload_NullPath_ReturnsNull() {
        assertNull(dummyPayloadStorage.download(null));
    }

    @Test
    public void testUpload_ValidPath_DoesNotThrowException() {
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        when(operation.getPath()).thenReturn(path);

        try {
            dummyPayloadStorage.upload(path, inputStream, 1024);
        } catch (IOException e) {
            // Should not throw an exception
            assert false;
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDownload_ValidPath_ReturnsNull_ThrowsException() {
        String path = "testPath";
        when(operation.getPath()).thenReturn(path);

        InputStream expectedInputStream = new ByteArrayInputStream(new byte[1024]);
        when(dummyPayloadStorage.download(path)).thenReturn(expectedInputStream);

        assertNull(dummyPayloadStorage.download(null));
    }
}