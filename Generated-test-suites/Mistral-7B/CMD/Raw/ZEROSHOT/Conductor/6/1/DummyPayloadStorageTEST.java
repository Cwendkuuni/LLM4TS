package com.netflix.conductor.core.storage;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUploadDoesNotDoAnything() throws IOException {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // assertNothingElseHappens(); // not supported in JUnit 4
    }

    @Test
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);
        assertNull(inputStream);
    }

    @Test
    public void testDownloadReturnsEmptyInputStreamWhenCalled() throws IOException {
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedBytes = new byte[0];

        int read = -1;
        while ((read = inputStream.read()) != -1) {
            outputStream.write(read);
        }

        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}