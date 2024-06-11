package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        ExternalStorageLocation location = storage.getLocation(Operation.GET, PayloadType.JSON, "test/path");
        assertNull(location);
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        storage.upload("test/path", mock(InputStream.class), 100);
        // No exception should be thrown
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage storage = new DummyPayloadStorage();
        InputStream inputStream = storage.download("test/path");
        assertNull(inputStream);
    }
}