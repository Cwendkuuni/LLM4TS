package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.Operation;
import com.netflix.conductor.common.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.core.storage.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.BINARY;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertNull(result);
        assertNull(expectedResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.BINARY;
        String path = "testPath";
        Operation operation = null;

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.BINARY;
        String path = null;

        dummyPayloadStorage.getLocation(operation, payloadType, path);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadWithNullPayload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 0;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUploadWithNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        long payloadSize = -1;

        dummyPayloadStorage.upload(path, payload, payloadSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDownloadWithNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = null;

        dummyPayloadStorage.download(path);
    }
}