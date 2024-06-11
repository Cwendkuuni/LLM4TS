package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.PayloadType.Type;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.DummyPayloadStorage.DummyPayloadStorageException;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = new PayloadType(Type.JSON);
        String path = "/test-path";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test(expected = DummyPayloadStorageException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = new PayloadType(Type.JSON);
        String path = "/test-path";

        Operation operation = null;
        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test-path";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = DummyPayloadStorageException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;
        String path = null;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test-path";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testDownloadWithEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test
    public void testDownloadWithNonEmptyPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "/test-path";

        InputStream expectedResult = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };
        outputStream.write(bytes, 0, bytes.length);
        expectedResult = new ByteArrayInputStream(outputStream.toByteArray());

        InputStream result = dummyPayloadStorage.download(path);

        assertNotNull(result);
        try {
            byte[] expectedBytes = new byte[bytes.length];
            int read = result.read(expectedBytes, 0, bytes.length);
            assertArrayEquals(bytes, expectedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(expected = DummyPayloadStorageException.class)
    public void testDownloadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;

        dummyPayloadStorage.download(path);
    }
}