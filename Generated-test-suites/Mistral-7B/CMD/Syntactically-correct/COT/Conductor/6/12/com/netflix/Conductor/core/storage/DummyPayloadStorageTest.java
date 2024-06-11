package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.netflix.conductor.core.storage.DummyPayloadStorage;
import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        assertNull(dummyPayloadStorage.getLocation(operation, payloadType, path));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new java.io.ByteArrayInputStream("test data".getBytes());
        long payloadSize = 10L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);

        // Nothing to verify as the method does not have any side effect
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";

        assertNull(dummyPayloadStorage.download(path));
    }

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testGetLocationNullOperation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";

        dummyPayloadStorage.getLocation(null, payloadType, path);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationNullPath() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;

        dummyPayloadStorage.getLocation(operation, payloadType, null);
    }

    @Test
    public void testUploadNullInputStream() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        long payloadSize = 10L;

        dummyPayloadStorage.upload(path, null, payloadSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadNegativePayloadSize() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = new java.io.ByteArrayInputStream("test data".getBytes());
        long payloadSize = -10L;

        dummyPayloadStorage.upload(path, inputStream, payloadSize);
    }
}