package com.netflix.conductor.core.storage.test;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.utils.ExternalStorageLocation;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

/**
 * Test class for DummyPayloadStorage
 */
public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dp = new DummyPayloadStorage();
        Operation operation = new Operation();
        PayloadType payloadType = PayloadType.JSON;
        String path = "testPath";
        ExternalStorageLocation expectedLocation = null;
        ExternalStorageLocation actualLocation = dp.getLocation(operation, payloadType, path);
        assertNull(actualLocation);
        assertNull(expectedLocation);
    }

    @Test
    public void testUpload() throws IOException {
        DummyPayloadStorage dp = new DummyPayloadStorage();
        String path = "testPath";
        byte[] payload = new byte[1];
        long payloadSize = 1;
        dp.upload(path, new ByteArrayInputStream(payload), payloadSize);
        // No assertions needed as the method does not return anything and does not throw any checked exception
    }

    @Test(expected = IOException.class)
    public void testUploadWithException() throws IOException {
        DummyPayloadStorage dp = new DummyPayloadStorage();
        String path = "testPath";
        byte[] payload = new byte[1];
        long payloadSize = 1;
        dp.upload(path, null, payloadSize);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dp = new DummyPayloadStorage();
        String path = "testPath";
        InputStream inputStream = dp.download(path);
        assertNull(inputStream);
    }
}