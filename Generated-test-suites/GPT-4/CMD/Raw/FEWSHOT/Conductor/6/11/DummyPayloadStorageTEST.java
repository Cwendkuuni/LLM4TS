package com.netflix.conductor.core.storage;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;

public class DummyPayloadStorageTest {

    private final DummyPayloadStorage dps = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.READ, ExternalPayloadStorage.PayloadType.TASK_INPUT, "dummyPath"));
        assertNull(dps.getLocation(ExternalPayloadStorage.Operation.WRITE, ExternalPayloadStorage.PayloadType.TASK_OUTPUT, "dummyPath"));
    }

    @Test
    public void testUpload() {
        // testing that no exceptions are thrown
        dps.upload("dummyPath", null, 0);
    }

    @Test
    public void testDownload() {
        assertNull(dps.download("dummyPath"));
    }
}