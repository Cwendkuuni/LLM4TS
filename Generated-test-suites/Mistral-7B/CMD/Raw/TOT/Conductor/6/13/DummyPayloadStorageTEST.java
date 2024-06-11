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

public class DummyPayloadStorageTest {

    private DummyPayloadStorage payloadStorage;
    private Operation operation;

    @Before
    public void setup() {
        payloadStorage = new DummyPayloadStorage();
        operation = mock(Operation.class);
    }

    @Test
    public void testGetLocationReturnsNull() {
        assertNull(payloadStorage.getLocation(operation, PayloadType.EVENT, "testPath"));
    }

    @Test
    public void testUploadDoesNotDoAnything() throws IOException {
        String path = "testPath";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        payloadStorage.upload(path, inputStream, payloadSize);

        // no assertions needed, the method does not do anything
    }

    @Test
    public void testDownloadReturnsNull() throws IOException {
        String path = "testPath";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testBytes = "testData".getBytes();
        inputStream.readAllBytes(); // read all bytes to close the stream

        when(payloadStorage.download(path)).thenReturn(inputStream);

        InputStream result = payloadStorage.download(path);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDownloadThrowsIOException() throws IOException {
        String path = "testPath";

        when(payloadStorage.download(path)).thenThrow(new IOException("Test error message"));

        payloadStorage.download(path);
    }

    @Test
    public void testDownloadReturnsExpectedData() throws IOException {
        String path = "testPath";
        byte[] testBytes = "testData".getBytes();
        InputStream expectedInputStream = new ByteArrayInputStream(testBytes);

        when(payloadStorage.download(path)).thenReturn(expectedInputStream);

        InputStream result = payloadStorage.download(path);

        byte[] resultBytes = toByteArray(result);

        assertArrayEquals(testBytes, resultBytes);
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}