package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(
            ExternalPayloadStorage.Operation.READ, 
            ExternalPayloadStorage.PayloadType.TASK_INPUT, 
            "mockPath"
        );

        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream mockStream = mock(InputStream.class);
        storage.upload("mockPath", mockStream, 1024);

        // Since this is a dummy implementation and do nothing, 
        // there's nothing to assert here. This case just ensures the method 
        // does not produce an error.
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream stream = storage.download("mockPath");

        assertNull(stream);
    }
}