package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.Operation;
import com.netflix.conductor.common.run.PayloadType;
import com.netflix.conductor.common.run.ExternalStorageLocation;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.ADD, PayloadType.INPUT, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String testPath = "test/path";
        byte[] payloadBytes = {1, 2, 3};
        InputStream payload = new ByteArrayInputStream(payloadBytes);
        dummyStorage.upload(testPath, payload, payloadBytes.length);
        // Add assertions if necessary
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String testPath = "test/path";
        InputStream downloadedPayload = dummyStorage.download(testPath);
        assertNull(downloadedPayload);
    }
}