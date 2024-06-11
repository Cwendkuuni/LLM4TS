package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.netflix.conductor.common.operation.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        ExternalStorageLocation expected = null;
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = mock(InputStream.class);
        long payloadSize = 10L;

        dummyPayloadStorage.upload(path, payload, payloadSize);
        // No verification needed as the method does nothing
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expected = null;
        InputStream actual = dummyPayloadStorage.download(path);

        assertEquals(expected, actual);
    }

    @Test
    public void testNullOperation() {
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        Operation operation = null;

        ExternalStorageLocation expected = null;
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expected, actual);
    }

    @Test
    public void testNullPayloadType() {
        Operation operation = mock(Operation.class);
        String path = "testPath";

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        PayloadType payloadType = null;

        ExternalStorageLocation expected = null;
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expected, actual);
    }

    @Test
    public void testNullPath() {
        Operation operation = mock(Operation.class);
        PayloadType payloadType = PayloadType.JSON;

        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        String path = null;

        ExternalStorageLocation expected = null;
        ExternalStorageLocation actual = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expected, actual);
    }
}