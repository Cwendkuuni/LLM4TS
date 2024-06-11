package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.ADD, ExternalPayloadStorage.PayloadType.TASK_INPUT, "path"));
        assertNull(dummyPayloadStorage.getLocation(ExternalPayloadStorage.Operation.GET, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "path"));
    }

    @Test
    public void testUpload() {
        InputStream input = null;
        dummyPayloadStorage.upload("testPath", input, 0L); 
        // As the upload method doesn't return anything, there isn't anything to assert here. We are just checking if the method runs without throwing an exception.
    }

    @Test
    public void testDownload() {
        assertNull(dummyPayloadStorage.download("path"));
    }
}