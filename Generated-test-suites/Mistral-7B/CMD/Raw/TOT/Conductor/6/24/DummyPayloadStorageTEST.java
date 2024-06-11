package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;

    @Before
    public void setup() {
        this.storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation_whenNullOperation_thenReturnNull() {
        Operation operation = null;
        PayloadType payloadType = PayloadType.FILE;
        String path = "test.txt";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = this.storage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testGetLocation_whenValidInput_thenReturnNull() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.FILE;
        String path = "test.txt";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = this.storage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testUpload_whenValidInput_thenDoNothing() {
        String path = "test.txt";
        InputStream payload = new ByteArrayInputStream(new byte[10]);
        long payloadSize = 10L;

        this.storage.upload(path, payload, payloadSize);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpload_whenNullPath_thenThrowException() {
        InputStream payload = new ByteArrayInputStream(new byte[10]);
        long payloadSize = 10L;

        this.storage.upload(null, payload, payloadSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpload_whenNullPayload_thenThrowException() {
        String path = "test.txt";
        InputStream payload = null;
        long payloadSize = 10L;

        this.storage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload_whenNullPath_thenReturnNull() {
        String path = null;

        InputStream expectedResult = null;
        InputStream result = this.storage.download(path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testDownload_whenValidInput_thenReturnEmptyInputStream() {
        String path = "test.txt";

        InputStream expectedResult = new ByteArrayInputStream(new byte[0]);
        InputStream result = this.storage.download(path);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDownload_whenPathExists_thenReturnExpectedData() throws IOException {
        String path = "test.txt";
        byte[] expectedData = "Test data".getBytes(StandardCharsets.UTF_8);
        InputStream expectedResult = new ByteArrayInputStream(expectedData);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write("Test data".getBytes(StandardCharsets.UTF_8));
        InputStream mockedInputStream = new ByteArrayInputStream(os.toByteArray());

        when(this.storage.download(path)).thenReturn(mockedInputStream);

        InputStream result = this.storage.download(path);

        assertNotNull(result);
        byte[] actualData = toByteArray(result);
        assertArrayEquals(expectedData, actualData);
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}