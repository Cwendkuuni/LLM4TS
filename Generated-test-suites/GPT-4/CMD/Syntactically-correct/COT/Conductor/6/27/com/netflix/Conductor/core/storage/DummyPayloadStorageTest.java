package com.netflix.conductor.core.storage;
import static org.junit.Assert.*;

import java.io.InputStream;
import org.junit.Test;
import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;

public class DummyPayloadStorageTest {

    DummyPayloadStorage dummy = new DummyPayloadStorage();

    @Test
    public void testGetLocation() {
        assertNull(dummy.getLocation(Operation.READ, PayloadType.WORKFLOW_INPUT, "path"));
        assertNull(dummy.getLocation(null, null, "path"));
    }

    @Test
    public void testUpload() {
        try {
            dummy.upload("path", null, 0);
        } catch(Exception e) {
            fail("Upload failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testDownload() {
        assertNull(dummy.download("Path"));
        assertNull(dummy.download(null));
    }
}