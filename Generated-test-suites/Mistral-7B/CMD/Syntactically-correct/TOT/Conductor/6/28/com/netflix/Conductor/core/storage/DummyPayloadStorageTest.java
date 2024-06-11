package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.payload.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocationWithNullOperation() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        assertNull(payloadStorage.getLocation(null, PayloadType.FILE, "testPath"));
    }

    @Test
    public void testGetLocationWithNullPath() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        assertNull(payloadStorage.getLocation(operation, PayloadType.FILE, null));
    }

    @Test
    public void testGetLocationWithValidInput() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        Operation operation = mock(Operation.class);
        assertNull(payloadStorage.getLocation(operation, PayloadType.FILE, "testPath"));
    }

    @Test(expected = NullPointerException.class)
    public void testUploadWithNullPath() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        InputStream payload = new ByteArrayInputStream(new byte[0]);
        payloadStorage.upload(null, payload, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testUploadWithNullPayload() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        payloadStorage.upload(path, null, 0);
    }

    @Test
    public void testUploadWithValidInput() throws IOException {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = new ByteArrayInputStream(new byte[10]);
        payloadStorage.upload(path, payload, 10);
    }

    @Test
    public void testDownloadWithNullPath() {
        DummyPayloadStorage payloadStorage = new DummyPayloadStorage();
        InputStream expectedResult = null;
        assertNull(payloadStorage.download(null));
    }
}