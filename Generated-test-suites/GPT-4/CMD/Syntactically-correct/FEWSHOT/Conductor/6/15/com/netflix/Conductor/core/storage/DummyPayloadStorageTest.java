package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.WRITE,
            ExternalPayloadStorage.PayloadType.WORKFLOW_INPUT,
            "dummy-path"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        // nothing to assert as the method does not have a return type, it does nothing
        InputStream is = new ByteArrayInputStream("test".getBytes());
        dummyPayloadStorage.upload("dummy-path", is, is.available());
        // test passed if no exception thrown
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummyPayloadStorage = new DummyPayloadStorage();
        assertNull(dummyPayloadStorage.download("dummy-path"));
    }
}