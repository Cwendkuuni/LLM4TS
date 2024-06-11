package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.*;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        ExternalStorageLocation expectedResult = null;
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(operation, payloadType, path);

        assertEquals(expectedResult, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocation_ThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        dummyPayloadStorage.getLocation(operation, PayloadType.XML, path);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload(path, payload, payloadSize);

        // As this is a dummy implementation, there is no actual verification needed.
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpload_ThrowsException() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream payload = null;
        long payloadSize = 1024L;

        dummyPayloadStorage.upload("", payload, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        InputStream expectedResult = null;
        InputStream result = dummyPayloadStorage.download(path);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDownload_NullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        InputStream result = dummyPayloadStorage.download(null);

        assertNull(result);
    }
}