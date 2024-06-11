package com.netflix.conductor.core.storage.test;

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
import com.netflix.conductor.core.storage.DummyPayloadStorage;

public class DummyPayloadStorageTest {

    private ExternalPayloadStorage dummyPayloadStorage;

    @Before
    public void setup() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocationReturnsNull() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.FILE;
        String path = "testPath";

        ExternalStorageLocation externalStorageLocation = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(externalStorageLocation);
    }

    @Test
    public void testUploadDoesNotDoAnything() {
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[5]);
        long payloadSize = 5;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = IOException.class)
    public void testDownloadReturnsNull() {
        String path = "testPath";

        InputStream inputStream = dummyPayloadStorage.download(path);

        assertNull(inputStream);
    }

    @Test
    public void testDownloadReturnsEmptyInputStream() {
        String path = "testPath";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        when(dummyPayloadStorage.download(path)).thenReturn(inputStream);

        InputStream actualInputStream = dummyPayloadStorage.download(path);

        byte[] expectedBytes = {};
        InputStream expectedInputStream = new ByteArrayInputStream(expectedBytes);

        assertSame(expectedInputStream, actualInputStream);
    }

    @Test
    public void testDownloadReturnsInputStreamWithData() {
        String path = "testPath";
        String testData = "test data";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        when(dummyPayloadStorage.download(path)).thenReturn(inputStream);

        InputStream actualInputStream = dummyPayloadStorage.download(path);

        byte[] expectedBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream expectedInputStream = new ByteArrayInputStream(expectedBytes);

        assertSame(expectedInputStream, actualInputStream);
    }
}