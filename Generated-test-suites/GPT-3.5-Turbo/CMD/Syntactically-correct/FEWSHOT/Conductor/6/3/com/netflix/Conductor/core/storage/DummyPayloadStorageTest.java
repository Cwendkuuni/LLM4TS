package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import org.junit.Test;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        ExternalStorageLocation location = dummy.getLocation(Operation.GET, PayloadType.TASK_INPUT, "testPath");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        InputStream payload = mock(InputStream.class);
        dummy.upload("testPath", payload, 100);
        // Add assertion if needed
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dummy = new DummyPayloadStorage();
        InputStream downloadedPayload = dummy.download("testPath");
        assertNull(downloadedPayload);
    }
}