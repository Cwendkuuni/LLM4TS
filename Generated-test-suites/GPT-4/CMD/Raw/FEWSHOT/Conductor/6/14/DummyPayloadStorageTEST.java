package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;

public class DummyPayloadStorageTest {

    private final DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        ExternalStorageLocation storageLocation = dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.ADD, 
            ExternalPayloadStorage.PayloadType.TASK_INPUT,
            "some_fake_path");
        assertNull(storageLocation);
    }

    @Test
    public void testUpload() {
        // We don't assert anything here as method doesn't return anything nor affects a visible state. 
        // In a real-world scenario, you would use Mocking Framework to verify that certain methods were being called.
        dummyPayloadStorage.upload("some_fake_path", InputStream.nullInputStream(), 123L);
    }

    @Test
    public void testDownload() {
        InputStream result = dummyPayloadStorage.download("some_fake_path");
        assertNull(result);
    }
}