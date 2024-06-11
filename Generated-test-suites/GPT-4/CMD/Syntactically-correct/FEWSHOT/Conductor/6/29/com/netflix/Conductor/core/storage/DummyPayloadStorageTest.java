package com.netflix.conductor.core.storage;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.InputStream;

import org.junit.Test;

public class DummyPayloadStorageTest {

    @Test
    public void testGetLocation() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        assertNull(dps.getLocation(null, null, null));
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.ADD, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        InputStream dummyPayload = new InputStream() { @Override public int read() { return 0; } };
        dps.upload("dummyPath", dummyPayload, 1);
        // Since upload doesn't do anything in this implementation, no exception or assertion needed.
    }

    @Test
    public void testDownload() {
        DummyPayloadStorage dps = new DummyPayloadStorage();
        assertNull(dps.download("dummyPath"));
        assertNull(dps.download(null));
    }
}