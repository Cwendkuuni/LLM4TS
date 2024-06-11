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

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage storage;

    @Before
    public void setup() {
        storage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationNullValues() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = null;

        assertNull(storage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testGetLocationValidValues() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedLocation = new ExternalStorageLocation("testBucket", "testKey");
        when(storage.getLocation(operation, payloadType, path)).thenReturn(expectedLocation);

        ExternalStorageLocation actualLocation = storage.getLocation(operation, payloadType, path);

        assertNull(expectedLocation); // This is a dummy implementation, so getLocation should always return null
        assertNull(actualLocation);
    }

    @Test
    public void testUploadNullValues() {
        String path = null;
        InputStream payload = null;
        long payloadSize = 1024;

        storage.upload(path, payload, payloadSize);

        // No-op since this is a dummy implementation
    }

    @Test
    public void testUploadValidValues() {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[1024]);
        long payloadSize = 1024;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedBytes = "testData".getBytes(StandardCharsets.UTF_8);
        outputStream.write(expectedBytes);

        when(storage.upload(path, payload, payloadSize)).thenReturn(true);

        InputStream actualInputStream = storage.download(path);

        assertNull(actualInputStream); // No data should be uploaded since this is a dummy implementation
    }

    @Test
    public void testDownloadNullValues() {
        String path = null;

        InputStream actualInputStream = storage.download(path);

        assertNull(actualInputStream);
    }

    @Test
    public void testDownloadValidValues() throws IOException {
        String path = "testPath";
        String expectedData = "testData";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedBytes = expectedData.getBytes(StandardCharsets.UTF_8);
        outputStream.write(expectedBytes);
        InputStream expectedInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        when(storage.download(path)).thenReturn(expectedInputStream);

        InputStream actualInputStream = storage.download(path);

        byte[] actualBytes = toByteArray(actualInputStream);
        String actualData = new String(actualBytes, StandardCharsets.UTF_8);

        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(expectedData, actualData);
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }
}