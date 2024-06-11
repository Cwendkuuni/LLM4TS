package com.netflix.conductor.core.storage;

import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        ExternalStorageLocation location = dummyStorage.getLocation(Operation.READ, PayloadType.TASK_INPUT, "test_path");
        Assert.assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String testPath = "test_path";
        byte[] payloadData = "test payload data".getBytes();
        ByteArrayInputStream payloadStream = new ByteArrayInputStream(payloadData);
        dummyStorage.upload(testPath, payloadStream, payloadData.length);
        // Add assertion if needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyStorage = new DummyPayloadStorage();
        String testPath = "test_path";
        InputStream downloadedPayload = dummyStorage.download(testPath);
        Assert.assertNull(downloadedPayload);
    }

}