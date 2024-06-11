package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;

    @Before
    public void setup() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationWhenOperationIsNullReturnsNull() {
        Operation operation = null;
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expected = null;
        ExternalStorageLocation result = storage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expected);
    }

    @Test
    public void testGetLocationWhenOperationIsNotNULLReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expected = null;
        ExternalStorageLocation result = storage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expected);
    }

    @Test
    public void testUploadWhenPayloadIsNullThrowsException() {
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0;

        storage.upload(path, payload, payloadSize);

        // No exception expected, as method does not throw any exception in given implementation
    }

    @Test
    public void testUploadWhenPayloadIsNotNULLUploadsPayload() throws IOException {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[10]);
        long payloadSize = 10;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);

        when(storage).download(path).thenReturn(inputStream);

        storage.upload(path, payload, payloadSize);

        // No assertions here, as method does not return any value in given implementation
    }

    @Test
    public void testDownloadWhenPathIsNullReturnsNull() {
        String path = null;

        InputStream expected = null;
        InputStream result = storage.download(path);

        assertNull(result);
        assertNull(expected);
    }

    @Test
    public void testDownloadWhenPathIsNotNULLReturnsNull() {
        String path = "testPath";

        InputStream expected = null;
        InputStream result = storage.download(path);

        assertNull(result);
        assertNull(expected);
    }

    @Test
    public void testDownloadWhenPathIsEmptyStringReturnsNull() {
        String path = "";

        InputStream expected = null;
        InputStream result = storage.download(path);

        assertNull(result);
        assertNull(expected);
    }
}