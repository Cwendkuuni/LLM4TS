package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNull;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        ExternalStorageLocation result;

        result = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "testPath");
        assertNull(result);

        result = dummyPayloadStorage.getLocation(
                ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "testPath");
        assertNull(result);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        // Just ensuring that no exception occurs, as the upload method does not return anything
        dummyPayloadStorage.upload("testPath", null, 0L);
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();

        InputStream result = dummyPayloadStorage.download("testPath");
        assertNull(result);
    }
}